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
 */

//***********************************
//		Imports
//***********************************
import java.io.*;
import java.net.*;

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
	* PRE : 
	*
	* POST :
	*/
	// constructor code goes here

	//sock = new TCPCanSocket(new Socket(hostname, TCPCanSocket.DEFAULT_SOCKET_PORT));

	}

	public void dispatch(String cmd) throws Exception
	{
	/** dispatch method
	* This method will parse a string into a CAN message
	* and send that CAN message out VIA a TCPSocket sock
	*
 	*/
       // code goes here pls :)
      
	// based on TowerAction (as a templatey thingey...jamey doesn't like the idea tho'
	// parse a string into (format TBD...ID, Body)
	//Write that data into a new can message
	//the can message will be sent out on the bus via sock 
	    System.out.println(cmd);
	}
}// end RocketAction
