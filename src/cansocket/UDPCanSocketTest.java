// not updated since using fc2net for testing
//
package cansocket;

import java.io.*;
import java.net.*;
import java.util.*;

public class UDPCanSocketTest
{
    static int barb = 0;	// barberpole for bytes

    /* main entry */
    public static void main(String[] args) throws UnknownHostException
    {
	/* array of Can message ids to use */
	short ids[] = {
	    CanBusIDs.GPSID,      CanBusIDs.GPSHeight, CanBusIDs.GPSLatLon,
	    CanBusIDs.GPSTime,    CanBusIDs.StatusID,  CanBusIDs.PowerID,
	    CanBusIDs.PressValue, CanBusIDs.IMUAccel,  CanBusIDs.IMUGyro,
	    (short) 0xfffe
	};
	int ids_len = ids.length;

	int i;
	int cnt = -1;

	/* components of can message */
	short id;
	short timestamp;
	byte[] body = new byte[CanMessage.MSG_BODY];

	// socket to send on
	UDPCanSocket sock;

	// command line arguments, defaults
	int messCnt = 0;
	boolean sendStopMessage = false;
	StringBuffer hostname = new StringBuffer().append( "localhost" );
	InetAddress hostip;
	int port = UDPCanSocket.PORT_RECV;

	// require at least 2 arguments
	if (args.length < 2) {
	    Usage();
	    return;
	}

	// parse argument array
	int aPnt = 0;
	while (aPnt < args.length) {
	    if (args[aPnt].equals( "-host" )) {
		aPnt ++;
		hostname = new StringBuffer().append( args[aPnt] );
	    }
	    if (args[aPnt].equals( "-port" )) {
		aPnt ++;
		port = Integer.parseInt( args[aPnt] );
	    }
	    if (args[aPnt].equals( "-n" )) {
		aPnt ++;
		messCnt = Integer.parseInt( args[aPnt] );
	    }
	    if (args[aPnt].equals( "-stop" )) {
		sendStopMessage = true;
	    }
	    aPnt ++;
	}

	// printIds();	// debugging

	/* try to resolve host/ip names */
	try {
	    hostip = InetAddress.getByName( hostname.toString() );
	} catch (UnknownHostException uhe) {
	    System.out.println( "... failed to resolve hostname: "
		    + hostname.toString() + " ..." );
	    return;
	}

	try
	{
	    /* create a datagram socket to send to receiver */
	    sock = new UDPCanSocket( hostip, port );

	    // loop to send the given number of messages
	    for (i = 0; i < messCnt; i++)
	    {
		// create a Can message
		cnt = ((cnt + 1) % ids_len);
		id = ids[cnt];
		timestamp = (short) barb;
		fillByt( body );

		// send the Can message
		sock.write( new CanMessage( timestamp, id, 1, 8, body ));

		System.out.println (i + ". id: " + id + "  CanMessage sent");
	    }

	    /* send the terminator */
	    if (sendStopMessage) {
		sock.write( new CanMessage( (short)CanMessage.STOP_ID, 0, body ));
		System.out.println ("STOP CanMessage sent");
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

    static void fillByt( byte[] bod )
    {
	for (int i = 0; i < bod.length; i++) {
	    bod[i] = (byte) barb;
	    barb = ((barb + 1) % 256);
	}
    }

    static void Usage()
    {
	System.out.print( "Usage: java -jar udp.jar " );
	System.out.println( "[-host host] [-port port] [-stop] -n #_of_messages" );
	System.out.print( "  -host hostname or ip address to send to; " );
	System.out.println( "defaults to: localhost" );
	System.out.print( "  -port port to send to; " );
	System.out.println( "defaults to: " + UDPCanSocket.PORT_RECV);
	System.out.println( "  -stop send the terminator message at end" );
	System.out.println( "  -n number of messages to send" );
    }

    static void printIds()
    {

	System.out.println( "UplinkID: " + CanBusIDs.UplinkID );
	System.out.println( "UplinkSleep: " + CanBusIDs.UplinkSleep );
	System.out.println( "UplinkReceived: " + CanBusIDs.UplinkReceived );
	System.out.println( "UplinkExecuted: " + CanBusIDs.UplinkExecuted );

	System.out.println( "StatusID: " + CanBusIDs.StatusID );
	System.out.println( "StatusEvent: " + CanBusIDs.StatusEvent );
	System.out.println( "StatusChange: " + CanBusIDs.StatusChange );
	System.out.println( "StatusCurrent: " + CanBusIDs.StatusCurrent );

	System.out.println( "PowerID: " + CanBusIDs.PowerID );
	System.out.println( "PowerSleep: " + CanBusIDs.PowerSleep );
	System.out.println( "PowerShore: " + CanBusIDs.PowerShore );
	System.out.println( "PowerBusCurrent: " + CanBusIDs.PowerBusCurrent );
	System.out.println( "PowerBusVoltage: " + CanBusIDs.PowerBusVoltage );
	System.out.println( "PowerPowerDown: " + CanBusIDs.PowerPowerDown );

	System.out.println( "IMUID: " + CanBusIDs.IMUID );
	System.out.println( "IMUSleep: " + CanBusIDs.IMUSleep );
	System.out.println( "IMURate: " + CanBusIDs.IMURate );
	System.out.println( "IMURateDiv: " + CanBusIDs.IMURateDiv );
	System.out.println( "IMUAccel: " + CanBusIDs.IMUAccel );
	System.out.println( "IMUGyro: " + CanBusIDs.IMUGyro );

	System.out.println( "GPSID: " + CanBusIDs.GPSID );
	System.out.println( "GPSSleep: " + CanBusIDs.GPSSleep );
	System.out.println( "GPSTransmit: " + CanBusIDs.GPSTransmit );
	System.out.println( "GPSReceive: " + CanBusIDs.GPSReceive );

	/** totally bogus; for compilation only **/
	System.out.println( "GPSHeight: " + CanBusIDs.GPSHeight );
	System.out.println( "GPSLatLon: " + CanBusIDs.GPSLatLon );
	System.out.println( "GPSTime: " + CanBusIDs.GPSTime );
	/** end totally bogus **/

	System.out.println( "PressID: " + CanBusIDs.PressID );
	System.out.println( "PressSleep: " + CanBusIDs.PressSleep );
	System.out.println( "PressRate: " + CanBusIDs.PressRate );
	System.out.println( "PressRateDiv: " + CanBusIDs.PressRateDiv );
	System.out.println( "PressValue: " + CanBusIDs.PressValue );

	System.out.println( "UmbilicalID: " + CanBusIDs.UmbilicalID );
	System.out.println( "UmbilicalSleep: " + CanBusIDs.UmbilicalSleep );
	System.out.println( "UmbilicalStatus: " + CanBusIDs.UmbilicalStatus );
	System.out.println( "UmbilicalReady: " + CanBusIDs.UmbilicalReady );

	System.out.println( "ATVID: " + CanBusIDs.ATVID );
	System.out.println( "ATVSleep: " + CanBusIDs.ATVSleep );
	System.out.println( "ATVStatus: " + CanBusIDs.ATVStatus );
	System.out.println( "ATVAmpPower: " + CanBusIDs.ATVAmpPower );
	System.out.println( "ATVOverlay: " + CanBusIDs.ATVOverlay );
	System.out.println( "ATVTransmit: " + CanBusIDs.ATVTransmit );
	System.out.println( "ATVReceive: " + CanBusIDs.ATVReceive );

	System.out.println( "TempID: " + CanBusIDs.TempID );
	System.out.println( "TempSleep: " + CanBusIDs.TempSleep );
	System.out.println( "TempRate: " + CanBusIDs.TempRate );
	System.out.println( "TempRateDiv: " + CanBusIDs.TempRateDiv );
	System.out.println( "TempValue: " + CanBusIDs.TempValue );

    }





}	// end class UDPCanSocketTest
