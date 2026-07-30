package io.senftresearch.openrd;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketTimeoutException;
import java.util.Arrays;
//TODO Forgive me god, I used an LLM to very dirtily transfer the python UDP logic to Java (turns out having 10 minutes left
//     to test if it works on the Laser Cutter is an easy way to get tempted to use the devil's tools haha).
//     To use this code in the final product would be unacceptable. Going to redo from scratch.
public class RDUdp {
    public static final int NETWORK_TIMEOUT = 3000; // msecs
    public static final String INADDR_ANY_DOTTED = "0.0.0.0";
    public static final int SOURCE_PORT = 40200;
    public static final int DEST_PORT = 50200;
    public static final int MTU = 1470;

    public boolean verbose = false;
    public double chunkpause = 0.0;
    public static final byte ACK = (byte) 0xC6;

    private DatagramSocket sock;
    private InetAddress destHost;
    private int destPort;

    public RDUdp(String host) throws IOException {
        this(host, DEST_PORT, SOURCE_PORT, false);
    }

    public RDUdp(String host, int port, int localPort, boolean verbose) throws IOException {
        this.verbose = verbose;
        this.destPort = port;
        this.destHost = InetAddress.getByName(host);

        InetAddress bindAddr = InetAddress.getByName(INADDR_ANY_DOTTED);
        this.sock = new DatagramSocket(localPort, bindAddr);
        this.sock.setSoTimeout(NETWORK_TIMEOUT);
    }

    public byte[] checksum(byte[] data, int start, int length) {
        int cs = 0;
        for (int i = start; i < start + length; i++) {
            cs += (data[i] & 0xFF); // Convert to unsigned int before adding
        }
        byte b1 = (byte) (cs & 0xFF);
        byte b0 = (byte) ((cs >> 8) & 0xFF);
        return new byte[]{b0, b1};
    }

    public byte[] write(byte[] data) throws IOException, InterruptedException {
        int start = 0;
        //TODO: Seems to take the length of the byte array and send it in "chunks". Need to look into why MTU is used
        //      as the maximum chunk length
        int l = data.length;

        while (start < l) {
            int chunkSize = l - start;
            if (chunkSize > MTU) {
                chunkSize = MTU;
            }
            //TODO not sure what Checksums do in UDP / communication protocols. Need to research
            byte[] chksum = checksum(data, start, chunkSize);
            byte[] buf = new byte[2 + chunkSize];

            //TODO what is the point of the buffer? Is it the checksum values? If so how do these work. I know from minimal
            //     experience that checksums are used as a way to somewhat verify the data? But thats the extent of my knowledge.
            buf[0] = chksum[0];
            buf[1] = chksum[1];

            //TODO This I do get, it leaves the first 2 values of the buffer as the check sums, and the rest as the data
            //     via copying the values from element 2 onwards. Still need to figure out what the checksum is for.
            System.arraycopy(data, start, buf, 2, chunkSize);
            //TODO sending the buffer data to the laser cutter it seems. Not sure about the retry logic
            byte[] r = send(buf, start == 0);
            //TODO What is the ACK in this instance? Is that the end of the message or?
            if (r.length != 1 || r[0] != ACK) {
                return r;
            }
            start += chunkSize;
        }
        return null;
    }

    //TODO not looking at send logic yet, need to understand checksum first
    public byte[] send(byte[] ary, boolean retry) throws IOException, InterruptedException {
        if (this.chunkpause > 0.0) {
            Thread.sleep((long) (this.chunkpause * 1000));
        }

        double retryDelaySec = 0.2;
        double retryDelaySecMax = 5.0;
        byte[] receiveBuffer = new byte[8];

        while (true) {
            DatagramPacket sendPacket = new DatagramPacket(ary, ary.length, destHost, destPort);
            sock.send(sendPacket);

            try {

                DatagramPacket receivePacket = new DatagramPacket(receiveBuffer, receiveBuffer.length);
                sock.receive(receivePacket);


                return Arrays.copyOfRange(receivePacket.getData(), 0, receivePacket.getLength());

            } catch (SocketTimeoutException e) {

                if (!retry) {
                    throw new IOException("Network timeout or 'F' retry error simulated");
                }

                Thread.sleep((long) (retryDelaySec * 1000));
                retryDelaySec = Math.min(retryDelaySec * 2, retryDelaySecMax);
            }
        }
    }

    public void close() {
        if (sock != null && !sock.isClosed()) {
            sock.close();
        }
    }
}
