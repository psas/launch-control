package cansocket;

import java.io.*;
import java.net.*;
import java.util.*;

public class TCPCanSocketServerTest
{

	/* main entry */
	public static void main(String[] args)
	{
	    int cnt = 0;
	    short ids[] = {
		CanBusIDs.UplinkID,    CanBusIDs.StatusID, CanBusIDs.PowerID,
		CanBusIDs.IMUID,       CanBusIDs.GPSID,    CanBusIDs.PressID,
		CanBusIDs.UmbilicalID, CanBusIDs.ATVID,    CanBusIDs.TempID,
		0x0fff
	    };
	    int ids_len = ids.length;
	    String argCnt = args.length > 0 ? args[0] : "10";
	    int messCnt = Integer.parseInt (argCnt);
	    int i;

	    try
	    {
		TCPCanSocket cs = new TCPCanSocket( "localhost" );
		Random rand = new Random();

		for (i = 0; i < messCnt; i++)
		{	// write some phoney messages
		    /** short id = (short)rand.nextInt(100); **/
		    cnt = ((cnt + 1) % ids_len);
		    short id = ids[cnt];
		    
		    short timestamp = (short)rand.nextInt(100);
		    
		    byte[] body = new byte[CanMessage.MSG_BODY];
		    rand.nextBytes(body);
		    
		    cs.write(new CanMessage(id,timestamp,body));
	    
		    System.out.println (i + ". " + cnt + ". " +
			    "CanMessage written");
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
	} /* end main() */
	
}
