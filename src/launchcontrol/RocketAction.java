/** Class RocketAction
 *
 * By Larry Leach
 * for the Portland State Aerospace Society
 * 2 Jul 02
 *
 * Last Modified 4 Jul 02
 *
 * This class will send a CAN message to the rocket via the
 * TCPCanSocket class.  This message will be a valid CAN
 * message with an ID ( always 11 bits), a body ( 0 to 8 bytes )
 *
 *
 */  

/** ************* Changelog ****************
 *
 * 2 Jul Created at avionics meeting
 *
 * 4 July :
 * - modified to JavaDoc standard
 * - added commentary (larry's usual stuff that'll likely
 *   get stripped out when deliverable is shipped
 *
 * 20 July :
 * - Wrote the code in Dispatch method
 */

//***********************************
//		Imports
//***********************************
import java.io.*;
import java.net.*;
import java.util.*;

public class RocketAction implements SchedulableAction
{
	/** RocketaAction class
 	* This class implements the "t -5 signal"
	* i.e. this class will send a CAN Message
	* VIA the CAN bus.
	* The message will be arbitrary, informing
	* the listening device (the FC) that the
	* launch computer is ready for launch.
	*
	* PRE :
	*
	* POST :
	*
	*/
 
	protected TCPCanSocket sock;

	public RocketAction(String hostname) throws IOException
	{
	/**
	*    ** RocketAction Method **
	* This method is the constructor for the
	* RocketAction class
	*
	* PARAMETERS : The 
	*
	* PRE : The hostname is a valid string
	*
	* POST : we have parsed the string into
	* a new CAN message
	*/
	sock = new TCPCanSocket(new Socket(hostname, TCPCanSocket.DEFAULT_SOCKET_PORT));
	}
	public void dispatch(String cmd) throws Exception
	{
	/** dispatch method
	* This method will parse a string into a CAN message
	* and send that CAN message out VIA a TCPSocket sock
	*
	* Remember, a well formed CAN message will have a
	* ID           short
	* Timestamp    short
	* body         byte array
	* data length
 	*/

	    short id;
	    short timestamp = 12;
	    byte[] can_Body = new byte[8];
	    byte b;
	    int i = 0;
	    StringTokenizer tkn = new StringTokenizer(cmd);
	    id = Short.parseShort( tkn.nextToken(), 16 );
	    while (tkn.hasMoreTokens()) {
	    System.out.println( id );
	    b = Byte.parseByte( tkn.nextToken(), 16 );
	    can_Body[i] = b;
	    System.out.println( b );
	    i++;
	    }
	    CanMessage myMessage = new CanMessage(id, timestamp ,can_Body);
	    sock.write(myMessage);
	}
}// end RocketAction
