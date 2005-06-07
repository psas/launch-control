package cansocket;

import java.io.*;
import java.net.*;

public class TCPCanSocket implements CanSocket
{
    public static final int DEFAULT_PORT = 4437; // was 5349

	protected final String host;
	protected final int port;

	protected Socket s;
	protected DataInputStream din;
	protected DataOutputStream dout;

	/** constructor with no arguments creates a server socket
	 * on the default port.
	 */
	public TCPCanSocket()
	{
		this(DEFAULT_PORT);
	}

	public TCPCanSocket(int port)
	{
		this.host = null;
		this.port = port;
	}

	/** constructor with host argument opens a client socket
	 * on the default port
	 */
	public TCPCanSocket(String host)
	{
		this(host, DEFAULT_PORT);
	}

	/**
	 * Constructor with host and port arguments opens a client socket on
	 * the default port.
	 */
	public TCPCanSocket(String host, int port)
	{
		this.host = host;
		this.port = port;
	}

	protected synchronized void makeConnection() throws IOException
	{
		if(s != null)
			return;

		if(host != null)
		{
			System.out.println("Opening TCP connection to " + host + ":" + port);
			s = new Socket(host, port);
		}
		else
		{
			System.out.println("Waiting for TCP connection on " + port);
			s = new ServerSocket(port, 1).accept();
		}

		din = new DataInputStream(new BufferedInputStream(s.getInputStream()));
		dout = new DataOutputStream(new BufferedOutputStream(s.getOutputStream()));
	}

	public CanMessage read() throws IOException
	{
		makeConnection();
		try {
			return new CanMessage(din);
		} catch(EOFException e) {
			close();
			throw e;
		}
	}

	public void write(CanMessage msg) throws IOException
	{
		makeConnection();
		try {
			msg.putMessage(dout);
		} catch(EOFException e) {
			close();
			throw e;
		}
	}

	public void close() throws IOException
	{
		dout.close();
		din.close();
		s.close();
		s = null;
	}

	public void flush() throws IOException
	{
		try {
			dout.flush();
		} catch(ConnectException e) {
			close();
			makeConnection();
			dout.flush();
		} catch(SocketException e) {
			close();
			makeConnection();
			dout.flush();
		}
	}
}
