package io.senftresearch.openrd;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketTimeoutException;
import java.nio.ByteBuffer;
import java.util.Arrays;

/**
 * Responsible for handling of information via UDP to a Rudia Controller.
 */
public class RDUdpHandler {
    /**
     * How many milliseconds should the socket block for when sending packets, throwing a timeout exception if the
     * threshold is reached.
     */
    public static final int NETWORK_TIMEOUT_MS = 3000;

    private static final String INADDR_ANY_DOTTED = "0.0.0.0";

    /**
     * The default port that data is sent from when communicating with the Rudia Controller.
     */
    private static final int SOURCE_PORT = 40200;

    /**
     * The default port that data is sent to on the Rudia Controller.
     */
    private static final int DESTINATION_PORT = 50200;

    /**
     * The Maximum Transmission Unit of packages to be sent. I.e. how much data can be transmitted in one packet.
     */
    private static final int MTU = 1470;

    /**
     * The default byte that represents that a package has been successfully received by the controller.
     */
    private static final byte ACK = (byte) 0xC6;

    private double chunkPause = 0.0;


    private DatagramSocket socket;
    private InetAddress destinationHost;
    private int destinationPort;

    public RDUdpHandler(String hostName) throws IOException {
        this(hostName, DESTINATION_PORT, SOURCE_PORT);
    }

    public RDUdpHandler(String hostName, int destinationPort, int localPort) throws IOException {
        this.destinationPort = destinationPort;
        this.destinationHost = InetAddress.getByName(hostName);

        InetAddress bindAddress = InetAddress.getByName(INADDR_ANY_DOTTED);
        this.socket = new DatagramSocket(localPort, bindAddress);
        this.socket.setSoTimeout(NETWORK_TIMEOUT_MS);
    }

    public byte[] write(byte[] data) throws IOException, InterruptedException{
        int packetStart = 0;
        int dataLength = data.length;

        while (packetStart < dataLength){
            int chunkSize = dataLength - packetStart;

            if(chunkSize > MTU){
                chunkSize = MTU;
            }

            byte[] bufferArray = ByteBuffer.allocate(2 + chunkSize)
                    .put(initChecksum(data, packetStart, chunkSize))
                    .array();
            System.arraycopy(data, packetStart, bufferArray, 2, chunkSize);

            byte[] replyData = send(bufferArray, packetStart == 0);
            if(replyData.length != 1 || replyData[0] != ACK){
                return replyData;
            }

            packetStart += chunkSize;
        }
        //TODO I am still not happy with this logic.
        return null;
    }

    public byte[] send (byte[] dataToSend, boolean shouldRetry) throws IOException, InterruptedException{
        if(this.chunkPause > 0.0){
            Thread.sleep((long) this.chunkPause*1000);
        }

        double retryDelaySeconds = 0.2;
        double retryDelaySecondsMax = 5.0;

        byte[] receivedPacketBuffer = new byte[8];
        DatagramPacket sendPacket = new DatagramPacket(dataToSend, dataToSend.length, destinationHost, destinationPort);
        socket.send(sendPacket);
        while (true){
            try{
                DatagramPacket receivedPacket = new DatagramPacket(receivedPacketBuffer, receivedPacketBuffer.length);
                socket.receive(receivedPacket);

                return Arrays.copyOfRange(receivedPacket.getData(),0, receivedPacket.getLength());
            }
            catch(SocketTimeoutException e){
                if(!shouldRetry){
                    throw new IOException("Network Timeout!");
                }

                Thread.sleep((long) (retryDelaySeconds * 1000));
                retryDelaySeconds = Math.min(retryDelaySeconds*2, retryDelaySecondsMax);

            }
        }

    }

    public void close(){
        if(socket != null && !socket.isClosed()){
            socket.close();
        }
    }

    public void setChunkPause(double chunkPause) {
        this.chunkPause = chunkPause;
    }

    /**
     * Initializes the checksum for the data to be sent.
     * @param data The data to be checksummed.
     * @param start The start element of the array.
     * @param length The length of the data to be sent (how many bytes)
     * @return The checksum of the given range of the data.
     */
    private byte[] initChecksum(byte[] data, int start, int length){
        int checksum = 0;

        for(int i = start; i < start + length ; i++){
            checksum += (data[i] & 0xFF);
        }

        return new byte[]{
                (byte) ((checksum >> 8) & 0xFF),
                (byte) (checksum & 0xFF)
        };
    }
}
