package cansocket;

import java.io.*;
import java.net.*;

public class TCPCanSocket implements CanSocket
{
    protected static final int DEFAULT_PORT = 4439; // was 5349

	Socket s;
	protected InputStream din;
	protected OutputStream dout;

	/* constructor with a socket opens the data streams */
	public TCPCanSocket(Socket s) throws IOException
	{ 
		this.s = s;
		System.out.println( "constructor: open socket data streams" );
		din = new BufferedInputStream(s.getInputStream());
		dout = new BufferedOutputStream(s.getOutputStream());
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
		byte buf[] = new byte[CanMessage.MSG_SIZE];
		din.read(buf);
		return new CanMessage(buf);
	}
	
	public void write(CanMessage msg) throws IOException
	{
		dout.write(msg.toByteArray());
	}

	public void close() throws IOException
	{
		s.close();
	}

	public void flush() throws IOException
	{
		dout.flush();
	}
}
