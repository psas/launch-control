package launchcontrol;

/** Class SocketAction
 *
 * By Ian Osgood
 * for the Portland State Aerospace Society
 * 17 Jul 04
 *
 * Last Modified 17 Jul 04
 *
 * This class will send a CAN message to the launch tower via the
 * TCPCanSocket class.  This message will be a valid CAN
 * message with an ID ( always 11 bits), and body ( 0 to 8 bytes )
 *
 *
 */  

/** ************* Changelog ****************
 *
 * 17 July :
 * - copied from RocketAction
 * - Wrote the code in Dispatch method
 */

//***********************************
//		Imports
//***********************************
import cansocket.*;

import java.io.*;
import java.net.*;
import java.util.*;


/** SocketAction class
 	* This class implements communication to the launch tower
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
public class SocketAction implements SchedulableAction
{
	protected CanSocket sock;
	protected String type;
	
	/**
	*     SocketAction Method 
	* This method is the constructor for the
	* SocketAction class
	*
	* PARAMETERS : The socket bound to the launch tower
	*
	* PRE :
	*
	* POST :
	*/
	public SocketAction(CanSocket sock, String type) throws IOException
	{
		this.sock = sock;
		this.type = type;
	}
	

	/** dispatch method
	* This method will parse a string into a CAN message
	* and send that CAN message out VIA a CanSocket sock
	*
	* it maps the logical relay names and on/off parameter to the
	* appropriate LTC node commands and parameters
	*
	* Remember, a well formed CAN message will have a
	* ID           short
	* Timestamp    short
	* body         byte array
	* data length
	*
	* Also remember that the timestamp is currently hardcoded
	* PRE : The ID is a valid Java Short
	*	The Body is a valid java Byte array (-128d to +127d)
	*
	* POST : ??
 	*/
	public void dispatch(String cmd) throws Exception
	{
		short id;
		short timestamp = 0;
		byte[] can_Body = new byte[8];
		byte b;
		int len = 0;
		String command;
		StringTokenizer tkn = new StringTokenizer(cmd);

		// Set command
		command = tkn.nextToken();
		
		// convert to CAN id
		id = Config.getInt(type + "." + command).shortValue();

		// Set Body
		while (tkn.hasMoreTokens() )
		{
			b = (byte) Short.parseShort( tkn.nextToken(), 16 );
			can_Body[len] = b;
			len++;
		}
		// why bother with this? why not just use can_Body?
		byte[] body_Buffer = new byte[len];
		int i = len;
		while ( --i >= 0 )
		{
			body_Buffer[i] = can_Body[i];
		}
		// this supports smaller IDs with no RTR
		CanMessage myMessage = new CanMessage(timestamp, id, 0, len, body_Buffer);
		sock.write(myMessage);
		sock.flush();
	}
}// end RocketAction
