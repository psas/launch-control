package cansocket;

import java.io.*;
import java.net.*;

public class TCPCanSocket implements CanSocket
{
	Socket s;
	private DataInputStream din;
	private DataOutputStream dout;

	/* constructor with a socket opens the data streams */
	public TCPCanSocket(Socket s) throws IOException
	{ 
		this.s = s;
		System.out.println( "constructor: open socket data streams" );
		din = new DataInputStream(
			new BufferedInputStream(s.getInputStream()));
		dout = new DataOutputStream(
			new BufferedOutputStream(s.getOutputStream()));
		// dout.flush();
	}

	/* constructor with no arguments creates a server socket
	 * on the default port
	 */
	public TCPCanSocket() throws IOException
	{
		// this(new ServerSocket(DEFAULT_PORT).accept());
		this(new ServerSocket(DEFAULT_PORT, 100).accept());
		System.out.println( "constructor: open server socket" );
	}

	/* constructor with host argument opens a client socket
	 * on the default port
	 */
	public TCPCanSocket(String host) throws IOException
	{
		this(new Socket(host, DEFAULT_PORT));
		System.out.println( "open client socket" );
	}
	
	public CanMessage read() throws IOException
	{
		System.out.println( "CanMessage read ");
		// short id = din.readShort();
		short id = 0;
		try {
		    id = din.readShort();
		} catch (IOException ioe) {
		    System.out.print( "io exception caught " );
		    System.out.println( ioe.getMessage() + "\n" );
		    ioe.printStackTrace();
		    return (null);
		}
		System.out.println( id );


		// int timestamp = din.readInt();
		// byte[] body = new byte[id & 0xF];
		// id = (short) ((id >> 5) & 0x7FF);
		// byte[] body = new byte[CanMessage.MSG_LEN];
		// din.read(body);
		// return new CanMessage(id,timestamp,body);
		return new CanMessage(id);
	}
	
	public void write(CanMessage msg) throws IOException
	{
		System.out.println( "CanMessage write " + msg.getId());
		// dout.writeShort((msg.getId() & 0x7FF) << 5 | (msg.getBody().length & 0xF));
		dout.writeShort(msg.getId());
		// dout.writeInt(msg.getTimestamp());
		// dout.write(msg.getBody());
	}

	/*** phony: keep interface definition happy ***/
	public CanMessage recv() 
	{
	    return( new CanMessage( (short)98, 100, new byte[CanMessage.MSG_BODY] ));
	}

	public void send( CanMessage msg )
	{}
	/*** end phony ***/

	public void close() throws IOException
	{
		s.close();
	}

	public void flush() throws IOException
	{
		dout.flush();
	}
}
