import java.io.*;
import java.net.*;

public class TCPCanSocket implements CanSocket
{
	Socket s;
	private DataInputStream din;
	private DataOutputStream dout;

	public static final int DEFAULT_SOCKET_PORT = 5349;
	
	public TCPCanSocket(Socket s) throws IOException
	{ 
		this.s = s;
		din = new DataInputStream(new BufferedInputStream(s.getInputStream()));
		dout = new DataOutputStream(new BufferedOutputStream(s.getOutputStream()));
	}
	
	public CanMessage read() throws IOException
	{
		short id = din.readShort();
		short timestamp = din.readShort();
		byte[] body = new byte[id & 0xF];
		id = (short) ((id >> 5) & 0x7FF);
		din.read(body);
		return new CanMessage(id,timestamp,body);
	}
	
	public void write(CanMessage msg) throws IOException
	{
		dout.writeShort((msg.getId() & 0x7FF) << 5 | (msg.getBody().length & 0xF));
		dout.writeShort(msg.getTimestamp());
		dout.write(msg.getBody());
	}
	
	public void close() throws IOException
	{
		s.close();
	}
}
