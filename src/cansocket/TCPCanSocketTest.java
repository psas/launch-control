package cansocket;

import java.io.*;
import java.net.*;

public class TCPCanSocketTest
{
	public static void main(String[] args) throws Exception
	{
		String host = args.length > 0 ? args[0] : "localhost";
		TCPCanSocket cs = new TCPCanSocket(host);
		CanMessage cm;

		while(true)
		{
			System.out.println("Attempting to read CanMessage");
			cm = cs.read();

			System.out.print("id: ");
			System.out.println(cm.getId());
			System.out.print("time: ");
			System.out.println(cm.getTimestamp());
			System.out.print("body:");
			byte body[] = cm.getBody();
			for(int i = 0; i < body.length; ++i)
			{
				System.out.print(" ");
				System.out.print(body[i]);
			}
			System.out.println();
		}
	}
}
