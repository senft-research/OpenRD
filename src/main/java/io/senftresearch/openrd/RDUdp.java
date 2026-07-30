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
            // Developer Note: The data loops through the bytes of the message to be sent, and makes it an unsigned
            //                 integer, then adds it to the "cs" (that should be called "checksum"... what is it with
            //                 LLMs and acronyms that remove context?!)
            cs += (data[i] & 0xFF);
        }
        // Developer Note: Byte one is CS converted to a byte (which I assume would take the last 8 bits when cast and discard
        //                 the rest?) and sets the first byte as cs but shifted by 8 bits (hence getting the first byte).
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
            //Developer Note: The checksum is a 16 bit (hence 2 bytes, which is why its in a 2 element array, as each byte
            //                is 8 bits) sum of all the message contents that is used to check the contents of the header / data.
            //                The checksum is a way for the receiver to check the message is the same as sent. I.e. if the
            //                sum is different to that is stated, then the receiver knows the message was corrupted.
            byte[] chksum = checksum(data, start, chunkSize);
            byte[] buf = new byte[2 + chunkSize];

            //Developer Note: I am assuming this buffer array is purely here as a "tidy" way to add the checksum to the
            //                front of the data to be sent, as arrays are typically immutable. So create a buffer with a
            //                size that accounts for the 2 checksum elements, then copy the data directly to this new array
            buf[0] = chksum[0];
            buf[1] = chksum[1];

            System.arraycopy(data, start, buf, 2, chunkSize);

            //TODO sending the buffer data to the laser cutter it seems. Not sure about the retry logic
            byte[] r = send(buf, start == 0);
            //Developer Note: ACK stands for "acknowledgement", so this check is to see if the "r" (the reply) fist element
            //                is the acknowledgement or not. This does also show this code doesn't do much with the reply
            //                other than check for the ACK of the packet being sent. According to the third party Rudia
            //                docs, this ACK is sent after every packet to ensure it has been received.
            if (r.length != 1 || r[0] != ACK) {
                return r;
            }
            start += chunkSize;
        }
        //Developer Note: The whole method returns null if the system seems to fail in terms of reply, this seems quite
        //                brittle though.
        return null;
    }

    //TODO not looking at send logic yet, need to understand checksum first
    public byte[] send(byte[] ary, boolean retry) throws IOException, InterruptedException {
        //Developer Note: The chunkpause option is there to determine the amount of pause to do before sending data? Not
        //                sure as to why though?
        //TODO Investigate the reason for the chunk pause, and why it is separate to the retry delay.
        if (this.chunkpause > 0.0) {
            Thread.sleep((long) (this.chunkpause * 1000));
        }

        double retryDelaySec = 0.2;
        double retryDelaySecMax = 5.0;

        byte[] receiveBuffer = new byte[8];

        //TODO Why the param was named "ary" is not known, but seems rather silly as it is clearly the data to be sent.
        while (true) {
            //TODO need to look into DatagramPacket in terms of Java and UDP, but seems to specify the host destination address and port).
            DatagramPacket sendPacket = new DatagramPacket(ary, ary.length, destHost, destPort);

            //TODO The datagram socket is where the packet will be sent, that is setup during construction?
            sock.send(sendPacket);

            try {
                //TODO tries to recieve the reply to the packet, but does nothing with it, other than take it out for
                //     logic to check if its an ACK or not. Will display the reply for clarity as I want to see what the
                //     contents looks like.
                DatagramPacket receivePacket = new DatagramPacket(receiveBuffer, receiveBuffer.length);

                //TODO the docs say this will block until the packet is recieved. So I can only assume that the packet itself is
                //     just a representation of what it expects to recieve, not the data itself. (So the packet would be
                //     empty until it actually recieves anything, and there seems to be 0 timeout logic?).
                sock.receive(receivePacket);

                //TODO And then of course it will return the packet as an array of bytes, for the main logic to do its
                //     checks.
                return Arrays.copyOfRange(receivePacket.getData(), 0, receivePacket.getLength());

                //Developer Note: `sock.recieve()` does actually throw a timeout exception.
            } catch (SocketTimeoutException e) {

                //TODO at the moment the retry is always set to true, so it never fully times out. This seems odd.
                if (!retry) {
                    throw new IOException("Network timeout or 'F' retry error simulated");
                }
                //Developer Note: Seems I was mistaken and the Retry logic is actually tied to the retry boolean set when
                //                the `RDUdp` instance is set up.
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
