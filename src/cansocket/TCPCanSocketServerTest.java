import java.io.*;
import java.net.*;
import java.util.*;

public class TCPCanSocketServerTest
{
	public static void main(String[] args)
	{
		try
		{
			ServerSocket s = new ServerSocket(TCPCanSocket.DEFAULT_SOCKET_PORT);
			Socket incoming = s.accept();
			TCPCanSocket cs = new TCPCanSocket(incoming);
			
			Random rand = new Random();
			
			
			while(true)
			{	//write some phoney messages
				short id = (short)rand.nextInt(100);
				
				short timestamp = (short)rand.nextInt(100);
				
				byte[] body = new byte[CanMessage.MSG_LEN];
				rand.nextBytes(body);
				
				cs.write(new CanMessage(id,timestamp,body));
			
				System.out.println("CanMessage written");
			}
			
	
		}
		catch(UnknownHostException e)
		{
			System.out.println("Caught UnknownHostException " + e);
		}
		catch(IOException e)
		{
			System.out.println("Caught IOException " + e);
		}
		
	}
	
}