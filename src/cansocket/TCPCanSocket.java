package cansocket;

import java.io.*;
import java.net.*;

public class TCPCanSocket implements CanSocket
{
    public static final int DEFAULT_PORT = 4437; // was 5349

	protected Socket s;
	protected DataInputStream din;
	protected DataOutputStream dout;

	/** constructor with a socket opens the data streams */
	public TCPCanSocket(Socket s) throws IOException
	{ 
	    System.out.println(s.getInetAddress() + " " + s.getPort());
		this.s = s;
		System.out.println( "constructor: open socket data streams" );
		din = new DataInputStream(new BufferedInputStream(s.getInputStream()));
		dout = new DataOutputStream(new BufferedOutputStream(s.getOutputStream()));
	}

	/** constructor with no arguments creates a server socket
	 * on the default port.
	 */
	public TCPCanSocket() throws IOException
	{
		// this(new ServerSocket(DEFAULT_PORT).accept());
		this(new ServerSocket(DEFAULT_PORT, 100).accept());
		System.out.println( "constructor: open server socket" );
	}

    public TCPCanSocket(int port, int backlog) throws IOException{
	this(new ServerSocket(port, backlog).accept());
	System.out.println( "constructor: open server socket" );
    }

	/** constructor with host argument opens a client socket
	 * on the default port
	 */
	public TCPCanSocket(String host) throws IOException
	{
		this(new Socket(host, DEFAULT_PORT));
		System.out.println( "open client socket" );
	}

    /**
     * Constructor with host and port arguments opens a client socket on
     * the default port.
     */
	
    public TCPCanSocket(String host, int port) throws IOException {
	this(new Socket(host,port));
	System.out.println("Opening client socket: " + host + " "+ Integer.toString(port));
    }

	public CanMessage read() throws IOException
	{
		return new CanMessage(din);
	}
	
	public void write(CanMessage msg) throws IOException
	{
		msg.putMessage(dout);
	}

	public void close() throws IOException
	{
		dout.close();
		din.close();
		s.close();
	}

	public void flush() throws IOException
	{
		dout.flush();
	}
}
