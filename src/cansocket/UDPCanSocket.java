package cansocket;

import java.io.*;
import java.net.*;
import java.nio.ByteBuffer;

public class UDPCanSocket implements CanSocket
{
    private DatagramSocket sock;	// udp socket
    private InetAddress sendAddress;	// stored send address
    private int sendPort;		// stored send port

    /* constructor with no arguments creates a datagram socket
     * on the default receive port
     */
    public UDPCanSocket() throws IOException
    {
	System.out.println( "Usock0: open listen socket, port " + PORT_RECV );
	sock = new DatagramSocket( PORT_RECV );
	System.out.println( "Usock0: local address:port "
		+ InetAddress.getLocalHost() + " : "
		+ sock.getLocalPort() );
    }

    /* constructor with port argument creates a datagram socket
     * on the given port
     */
    public UDPCanSocket( int port ) throws IOException
    {
	System.out.println( "Usockp: open udp socket, port " + port );
	sock = new DatagramSocket( port );
	System.out.println( "Usockp: local address:port "
		+ InetAddress.getLocalHost() + " : "
		+ sock.getLocalPort() );
    }

    /* constructor with address argument opens a sending socket
     * on the default send port and remembers the address for
     * send()
     */
    public UDPCanSocket( String host ) throws IOException
    {
	System.out.println( "Usockh: open socket to " + host);
	sendAddress = InetAddress.getByName( host );
	sock = new DatagramSocket( PORT_SEND );
	System.out.println( "Usockh: local address:port "
		+ InetAddress.getLocalHost() + " : "
		+ sock.getLocalPort() );
    }

    /* constructor with address & port opens a sending socket
     * on the default send port and remembers the address/port
     * for later send()
     */
    public UDPCanSocket( InetAddress addr, int port ) throws IOException
    {
	System.out.print( "Usocka: open socket to " );
	System.out.println( addr + " : " + port);
	sendAddress = addr;
	sendPort = port;
	sock = new DatagramSocket( PORT_SEND );
	System.out.println( "Usocka: local address:port "
		+ InetAddress.getLocalHost() + " : "
		+ sock.getLocalPort() );
    }

    /*** really only used by TCP interface ***/
    public CanMessage read() throws IOException
    {
	System.out.println( "Usock: read ");
	return new CanMessage( (short) 101 );
    }

    public void write(CanMessage msg) throws IOException
    {
	System.out.println( "Usock: write " + msg.getId());
    }
    /*** really only used by TCP interface ***/

    /* send the given Can message on this socket.
     * send-to address must already be established by the constructor.
     * send-to port is the default receiver.
     */
    public void send( CanMessage msg ) throws IOException
    {
	// System.out.println( "Usock: send " + msg.getId());

	/* put can message into a byte buffer */
	byte[] buf = new byte[CanMessage.MSG_SIZE];
	ByteBuffer bytBuf = ByteBuffer.wrap( buf );
	int length = msg.toByteBuf( bytBuf );

	/* send packet addressed to the receiver address/port */
	sock.send( new DatagramPacket( buf, length, sendAddress, sendPort ));
    }

    public CanMessage recv() throws IOException
    {
	// System.out.println( "Usock: recv ");

	/* create a packet to receive Can message */
	byte buf[] = new byte [CanMessage.MSG_SIZE];
	DatagramPacket packet = new DatagramPacket( buf, buf.length );

	/* receive a packet */
	sock.receive( packet );

	/* return a new Can message constructed from receive buffer */
	return( new CanMessage( buf ));
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
