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

        // Bind to all interfaces on the local source port
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
        int l = data.length;

        while (start < l) {
            int chunkSize = l - start;
            if (chunkSize > MTU) {
                chunkSize = MTU;
            }

            byte[] chksum = checksum(data, start, chunkSize);
            byte[] buf = new byte[2 + chunkSize];

            // Construct the payload: checksum + data chunk
            buf[0] = chksum[0];
            buf[1] = chksum[1];
            System.arraycopy(data, start, buf, 2, chunkSize);

            byte[] r = send(buf, start == 0);

            // If response is not a single ACK byte, return the raw response
            if (r.length != 1 || r[0] != ACK) {
                return r;
            }
            start += chunkSize;
        }
        return null;
    }

    public byte[] send(byte[] ary, boolean retry) throws IOException, InterruptedException {
        if (this.chunkpause > 0.0) {
            Thread.sleep((long) (this.chunkpause * 1000));
        }

        double retryDelaySec = 0.2;
        double retryDelaySecMax = 5.0;
        byte[] receiveBuffer = new byte[8];

        while (true) {
            // Send the packet
            DatagramPacket sendPacket = new DatagramPacket(ary, ary.length, destHost, destPort);
            sock.send(sendPacket);

            try {
                // Receive the response
                DatagramPacket receivePacket = new DatagramPacket(receiveBuffer, receiveBuffer.length);
                sock.receive(receivePacket);

                // Return exactly the slice of data that was received
                return Arrays.copyOfRange(receivePacket.getData(), 0, receivePacket.getLength());

            } catch (SocketTimeoutException e) {
                // Mimicking Python loop logic. If your original code implemented
                // exponential backoff/retries inside the block, handle it here.
                if (!retry) {
                    throw new IOException("Network timeout or 'F' retry error simulated");
                }

                // Exponential backoff logic based on your variable initializations
                Thread.sleep((long) (retryDelaySec * 1000));
                retryDelaySec = Math.min(retryDelaySec * 2, retryDelaySecMax);
            }
        }
    }

    // Call this to clean up resources when done
    public void close() {
        if (sock != null && !sock.isClosed()) {
            sock.close();
        }
    }
}
