/** commPortUplink.java
 * the program to manyally fire the recovery pyros
 * for LV2.
 *
 *    Outline
 * Open || Port
 * When a command is selected...
 *   Turn on a || port pin (ex 0xFF)
 *   Wait 500 ms
 *   Play the dtmf tones on the soundcard
 *  For Arm we play #*7
 *  For Sep we play #*1
 *  For Lin we play #*2
 *  For Off we play #*3
 *
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
package manualuplink;

import java.io.*;
import java.util.*;
import java.lang.String.*;
import gnu.io.*;
import java.lang.Thread.*;
//import SimpleWrite;

class commPortUplink{
/**
* this must be a struct of
* Char, int, char, int, char.....
* that is passed into the simple writer class...
*/

    OutputStream outputStream;
    Menu M;
    public static final char arm ='7';
    public static final char sep = '1';
    public static final char lin = '2';
    public static final char off = '3';

    public static void main( String[] args )throws Exception{
	SimpleWrite S;
	char choice;
	long waitFor = 250;
	System.out.println("The PSAS uplink tool, C 2002");
	CommControl C = new CommControl();
	String defaultPort = "/dev/ttyS0";
	S = new SimpleWrite();
	Menu M = new Menu();
	S.open( defaultPort );
	while ( (choice = M.readMenu() ) != '9' ) {
	    switch ( choice ){
	    case '1':
		S.write( arm );
		break;
	    case '2':
		S.write( sep );
		break;
	    case '3':
		S.write( lin );
		break;
	    case '4':
		S.write( off );
		break;
	    default : System.out.println("Please enter a valid code");
		System.out.println();
		break;
	    }// the switch
	}// The while not nine
	System.out.println("ending the program");
	S.close();
        //close port

    }//The Main
}// The class

class CommControl{
    protected static final String Appname = "commPortUplink";

    public CommControl() throws IOException, NoSuchPortException, PortInUseException {
	/**
	 * Oh, Yeah...Make this a Constructor of a CommControl...
	 */

    }//constructor

    public void openPort() throws IOException, NoSuchPortException, PortInUseException {

	System.out.println("writing");
	//     id.open( "commPortUplink", 500 );
    	System.out.println("done");
    }//openPort

    public void closePort() throws IOException, NoSuchPortException, PortInUseException {
	//STRING CMD[] = { "Parport","off" };
	//Runtime.getRuntime().exec(cmd);
	//port.close();
	}

}//CommCotrol

class Menu{

    protected void menuDraw(){
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
	char ch;
        String tmp = read.readLine();
        if (tmp.length() < 1)
	  return 0;
	ch = tmp.charAt(0);
	System.out.println( ch );
	return ch;
    }

    public void callDTMF( String str ) throws IOException{
	/**
	 * ***NOT Calling DTMFDial to send the tones
	 * We are now using the Comm port to send commands
	 * to the 2M control board.  This board will control
	 * the Yeasu 2M radio, and make IT sent the DTMF
         * tones.
	 */
	String cmd[] = { "dial", str };
	Runtime.getRuntime().exec(cmd);
    }//end calldtmf
    
}// end menu
