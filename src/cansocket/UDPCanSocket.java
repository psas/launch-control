package cansocket;

import java.io.*;
import java.net.*;

/* Goal: read a full UDP packet (~1500 bytes) which may contain
   multiple NetMessages  (ie. perform buffering)
*/

public class UDPCanSocket implements CanSocket
{
    public static final int PORT_SEND = 4439;
    public static final int PORT_RECV = 4441;

    private DatagramSocket sock;	// udp socket
    private final InetAddress sendAddress;	// stored send address
    private final int sendPort;		// stored send port

	// read buffering
	private static final int MAX_PACKET_SIZE = 1500;
	private byte[] buf;
	private DatagramPacket packet;
	private DataInputStream dis;
	
    /** constructor with no arguments creates a datagram socket
     * on the default receive port.
     */
    public UDPCanSocket() throws IOException
    {
	this( PORT_RECV );
    }

    /** constructor with port argument creates a datagram socket
     * on the given port.
     */

    public UDPCanSocket( int localport ) throws IOException
    {
	this( localport, (InetAddress) null, 0);
    }

    /** constructor with address argument opens a sending socket
     * on the default send port and remembers the address for
     * send()
     */

    public UDPCanSocket( String host ) throws IOException
    {
	this( InetAddress.getByName( host ), PORT_SEND );
    }

    /**
     * Construct with address and port. Address and port will be remembered.
     */

    public UDPCanSocket( String host, int port ) throws IOException
    {
	this( InetAddress.getByName( host ), port );
    }

    /** constructor with address & port opens a sending socket
     * on the default send port and remembers the address/port
     * for later send()
     */
    public UDPCanSocket( InetAddress addr, int port ) throws IOException
    {
	this( PORT_RECV, addr, port );
    }

    public UDPCanSocket( int localport, String host, int port )
    throws IOException
    {
	this( localport, InetAddress.getByName( host ), port );
    }


    public UDPCanSocket( int localport, InetAddress remaddr, int remport ) throws IOException
    {
		buf = new byte[MAX_PACKET_SIZE];
		packet = new DatagramPacket(buf, MAX_PACKET_SIZE);
		dis = new DataInputStream(new ByteArrayInputStream( buf ));
		dis.skip(MAX_PACKET_SIZE);		// start empty

	sock = new DatagramSocket( localport );
	sendAddress = remaddr;
	sendPort = remport;
    }

    public CanMessage read() throws IOException
    {
		if (dis.available() <= 0)
		{
			// System.out.println( "Usock: recv ");
			// 
			// get new data
			sock.receive( packet );

			// reset the stream to the actual received data length
			dis = new DataInputStream(new ByteArrayInputStream( packet.getData(), 0, packet.getLength() ));
		}
		try {
			return new CanMessage( dis );
		} catch (IOException e) {
			int i;
			System.out.print("bad UDP packet: ");
			for (i=0; i<packet.getLength(); i++)
				System.out.print(" " + Integer.toHexString(buf[i]));
			System.out.println(".");
			throw e;
		}
    }

    public void write(CanMessage msg) throws IOException
    {
	if (sendAddress == null)
	    //throw new PortUnreachableException( "not configured for writing messages" );
	    throw new SocketException( "not configured for writing messages" );

	// System.out.println( "Usock: send " + msg.getId());

	/* put can message into a byte buffer */
	byte[] buf = msg.toByteArray();

	/* send packet addressed to the receiver address/port */
	sock.send( new DatagramPacket( buf, buf.length, sendAddress, sendPort ));
    }

    public void close() throws IOException
    {
	sock.close();
    }

    public void flush() throws IOException
    {
	// Udp sockets dont flush
    }

} // end class UDPCanSocket
