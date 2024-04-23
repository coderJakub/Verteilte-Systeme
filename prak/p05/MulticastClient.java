import java.io.*;
import java.net.*;
import java.util.Date;



class UDPSocket {
    protected DatagramSocket socket;
    private InetAddress address;
    private int port;

    protected UDPSocket(DatagramSocket socket) {
        this.socket = socket;
    }

    public UDPSocket() throws SocketException {
        this(new DatagramSocket()); // Nutzung des überl. Konstr.
    }

    public UDPSocket(int port) throws SocketException {
        this(new DatagramSocket(port)); // s.o.
    }

    public void send(String s, InetAddress rcvrAddress, int rcvrPort) throws IOException {
        byte[] outBuffer = s.getBytes();
        DatagramPacket outPacket = new DatagramPacket(outBuffer, outBuffer.length, rcvrAddress, rcvrPort);
        socket.send(outPacket);
    }
    public String receive(int maxBytes) throws IOException {
        byte[] inBuffer = new byte[maxBytes];
        DatagramPacket inPacket = new DatagramPacket(inBuffer, inBuffer.length);
        socket.receive(inPacket);
        address = inPacket.getAddress(); // addr for reply packet
        port = inPacket.getPort(); // port for reply packet
        return new String(inBuffer, 0, inPacket.getLength());
    }

    public void reply(String s) throws IOException {
        if (address == null) {
            throw new IOException("no one to reply");
        }
        send(s, address, port);
    }

    public InetAddress getSenderAddress() {
        return address;
    }

    public int getSenderPort() {
        return port;
    }

    public void setTimeout(int timeout) throws SocketException {
        socket.setSoTimeout(timeout);
    }

    public void close() {
        socket.close();
    }
}

public class MulticastClient{ 
    public static void main(String[] args) throws Exception{
        UDPSocket udpSocket = null;
        udpSocket = new UDPSocket();
        udpSocket.setTimeout(20000);
        // get inet addr of server
        InetAddress serverAddr = InetAddress.getByName("225.0.0.1");
        for ( int i = 1; i < 10; i++) {
            udpSocket.send("tes2t", serverAddr, 3333);
            while( true) {
                String reply = udpSocket.receive(200); // max. Bytes
                System.out.println( "Nachricht erhalten: "
                + udpSocket.getSenderAddress() + ":"
                + udpSocket.getSenderPort() + ": " + reply);
                break;
            }
        }
    }
}