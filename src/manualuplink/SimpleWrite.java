/*
 */
import java.io.*;
import java.util.*;
import gnu.io.*;
import java.lang.*;

/**
 * Class declaration
 *
 *
 * @author
 * @version 1.10, 08/04/00
 */
class SimpleWrite {
    static Enumeration	      portList;
    static CommPortIdentifier portId;
    static SerialPort	      serialPort;
    static OutputStream       outputStream;
    static boolean	      outputBufferEmptyFlag = false;

    static class inputDelay{
	public String input;
	public int delay;
}
    /**
     * Method declaration
     *
     *
     * @param args
     *
     * @see
     */
    public void open( String defaultPort ) {
	boolean portFound = false;
	portList = CommPortIdentifier.getPortIdentifiers();

	while (portList.hasMoreElements()) {
	    portId = (CommPortIdentifier) portList.nextElement();

	    if (portId.getPortType() == CommPortIdentifier.PORT_SERIAL) {

		if (portId.getName().equals(defaultPort)) {
		    System.out.println("Found port " + defaultPort);

		    portFound = true;

		    try {
			serialPort = 
			    (SerialPort) portId.open("SimpleWrite", 2000);
		    } catch (PortInUseException e) {
			System.out.println("Port in use.");

			continue;
		    } 

		    try {
			outputStream = serialPort.getOutputStream();
		    } catch (IOException e) {}

		    try {
			serialPort.setSerialPortParams(9600, 
						       serialPort.DATABITS_8, 
						       SerialPort.STOPBITS_1, 
						       SerialPort.PARITY_NONE);
		    } catch (UnsupportedCommOperationException e) {}
	

		    try {
		    	serialPort.notifyOnOutputEmpty(true);
		    } catch (Exception e) {
			System.out.println("Error setting event notification");
			System.out.println(e.toString());
			System.exit(-1);
		    }
		    System.out.println(
		    	"Writing to "
			+serialPort.getName());
		} 
	    } 
	} 

	if (!portFound) {
	    System.out.println("port " + defaultPort + " not found.");
	} 
    } 

public void write( char c ){
/**
* we are trying to send this string...
* ho#o*oXol
* where X is variable...
*/		
String message = "ho#o*o" + c + "ol";
	for(int i=0;i<message.length();i++) {	
		    try {
		    	outputStream.write( message.charAt(i) );
			} catch (IOException e) {}
			try{	
				Thread.sleep(750);
				} catch(Exception e){}
		}
}//write

public void close(){
		    serialPort.close();
}

}
