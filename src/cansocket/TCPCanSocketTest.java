import java.io.*;
import java.net.*;

public class TCPCanSocketTest
{
	public static void main(String[] args)
	{
		try
		{
		
			TCPCanSocket cs = new TCPCanSocket(new Socket("localhost",TCPCanSocket.DEFAULT_SOCKET_PORT));
		
			while(true)
			{
				System.out.println("Attempting to read CanMessage");
		
				CanMessage cm = cs.read();
		
				System.out.println("msg id: " + cm.getId());
				System.out.println("msg time: " + cm.getTimestamp());
				System.out.println("msg body: " + cm.getBody());
				System.out.println("Message received");
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