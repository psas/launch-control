/** ManualUplink.java
 * the program to manyally fire the recovery pyros
 * for LV2.
 *
 *    Outline
 * Open || Port
 * When a command is selected...
 *   Turn on a || port pin (ex 0xFF)
 *   Wait 500 ms
 *   Play the dtmf tones on the soundcard
 *   Wait 500 ms
 *   Turn off the pin on the parallel port
 *      e.g. 0x00 out to port
 *   If exit, close port, close sound card
 *
 * This code requires the RXTX package
 * available at rxtx.org
 * 
 * ===== NOTICE =====
 * The RXTX package is licsenced under the LGPL
 * Please keep this in mind...
 * ===== end notice =====
 *
 * Docs for the parallel port stuff
 * is at http://java.sun.com/products/javacomm/javadocs/packages.html
 */
import java.io.*;
import java.util.*;
import java.lang.String.*;
import gnu.io.*;
import java.lang.Thread.*;

class ManualUplink{

    protected static final String Arm = "#*7";
    protected static final String Sep = "#*1";
    protected static final String Lin = "#*2";
    protected static final String Off = "#*3";

    static Menu M;

    public static void main( String[] args )throws Exception{

	char choice;
	long waitFor = 500;
	System.out.println("The PSAS uplink tool, C 2002");
	CommControl C = new CommControl();
	
	M = new Menu();
	while ( (choice = M.readMenu()) != '9' ) {
	    switch ( choice ){
	    case '1':
		//send a 0xFF to the || Port
		C.openPort();
		Thread.sleep( waitFor );
		M.callDTMF( Arm );
		// send a 0x00 to the || port
		C.closePort( );
		Thread.sleep( waitFor );
		break;
	    case '2':
		C.openPort();
		Thread.sleep( waitFor );
		M.callDTMF( Sep );
		Thread.sleep( waitFor );
		C.closePort( );
		break;
	    case '3':
		C.openPort();
		Thread.sleep( waitFor );
		M.callDTMF( Lin );
		Thread.sleep( waitFor );
		C.closePort( );
		break;
	    case '4':
		C.openPort();
		Thread.sleep( waitFor );
		M.callDTMF( Off );
		Thread.sleep( waitFor );
		C.closePort( ); 
		break;
	    default : System.out.println("Please enter a valid code");
		System.out.println();
		break;
	    }// the switch
	}// The while not nine
	System.out.println("ending the program");
        //close port

    }//The Main
}// The class

class CommControl{
    protected static final String Appname = "ManualUplink";
    CommPort port;


    public CommControl() throws IOException, NoSuchPortException, PortInUseException {
	/**
	 * Oh, Yeah...Make this a Constructor of a CommControl...
	 */
	Enumeration ports;
	String name;
	ports = CommPortIdentifier.getPortIdentifiers();
	while ( ports.hasMoreElements() ){
	    CommPortIdentifier id = (CommPortIdentifier) ports.nextElement();
	    System.out.println ( id.getName() );
	}
	System.out.println (" Enter the parallel port's ID");
	name = new BufferedReader(new InputStreamReader(System.in)).readLine();
	port = CommPortIdentifier.getPortIdentifier( name ).open( Appname, 500 ) ;
	//OutputStreamWriter osw = new OutputStreamWriter( System.out );
	//OutputStreamWriter osw = new OutputStreamWriter( port.getOutputStream() );
    }//constructor

    public void openPort() throws IOException, NoSuchPortException, PortInUseException {

	System.out.println("writing");
	String cmd[] = { "parport","on" };
	Runtime.getRuntime().exec(cmd);
	//try{
	    //   osw.write( 255 );
	    //  osw.flush();
	//}
	//catch (IOException e){
	//    System.err.println(e);
	//}
    System.out.println("done");
    }//openPort

    public void closePort() throws IOException, NoSuchPortException, PortInUseException {
	String cmd[] = { "parport","off" };
	Runtime.getRuntime().exec(cmd);
	//String name = port.getName ();
	//port.close();
	//System.out.println( "Closing da Port!" );	
	}

}//CommCotrol

class Menu{

    public void menuDraw(){
	System.out.println();
	System.out.println();
	System.out.println("The PSAS Manual Uplink Tool");
	System.out.println("Choose one option");
	System.out.println("1) Arm or Disarm");
	System.out.println("2) Seperation");
	System.out.println("3) Line Cutter");
	System.out.println("4) All Off");
	System.out.println("9) End Program");
	System.out.println();
	System.out.println();
	System.out.println("enter a number 1-5 :");
    }

    public char readMenu() throws IOException{
	menuDraw();
	InputStreamReader input = new InputStreamReader( System.in );
	BufferedReader read = new BufferedReader( input );
	int ch;
	ch = read.readLine().charAt(0);
	System.out.println( (char) ch );
	return ((char) ch );
    }


    public void callDTMF( String str ) throws IOException{
	//System.out.println( "Calling DTMFDial wtih string :" + str );
	/**Method CallDTMF
	 * This method will do three things
	 *
	 * 1. Key the 2M Transmitter mike
	 *    This is done by calling java.lang.io.gnu.io.CommPort
	 *    ParallelPort to open the parallel port and send
	 *    a bit ( a one)
	 *
	 * 2. Call DTMF Dial using Runtime.GetRuntime().exec(cmd)
	 *    The String CMD will be the command dial, plus the 
	 *    DTMF codes being sent
	 *
	 * 3. Open the 2M Mike
	 *    This will be done by using the ParralelPort to send
	 *    a zero.
	 *
	 * NOTE: There will be hardware attached to the parralel
	 *       port that will be listening for the bits, and
	 *       will use that to open and close the mike.
	 */
	/**
	 * ***Opening the Parralel Port***
	 */



	/**
	 * ***Calling DTMFDial to send the tones
	 */

	//We are going to use java.lang.blah to call DTMFDial
	//and we need to pass in "dial str" 
	//where str is the string of chars being dialed
	//String s = "dial 5551212";
	//String cmd[] = { s };
	// Uncomment the next line for production code
	String cmd[] = { "dial", str };

	// comment this line out.  This is for stub testing
	// without DTMFDial
	//       	String cmd[] = { "/home/leachl/psas/ManualShell.sh" };

	Runtime.getRuntime().exec(cmd);


	/**
	 *    ***Closing the Parallel Port
	 */




    }//end calldtmf
    
}// end menu
