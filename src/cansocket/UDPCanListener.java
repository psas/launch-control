// UDPCanListener.java

// started by Karl Hallowell June 26, 2003.

// UDPCanListener listens to a socket for UDP-based Can messages. Use
// underlying CanListener class to actually run the listening thread.

package cansocket;

import java.io.*;
import java.net.*;

public class UDPCanListener extends CanListener {
    private UDPCanSocket sock = null;

    /*
     * the creation methods are more complicated because I have to pass through
     * the parameters for creating a UDPCanSocket. Maybe this mess of methods
     * can be simplified? Creation is simple. Initialize sock and then invoke
     * the init() method.
     */

    public UDPCanListener() throws IOException {
	sock = new UDPCanSocket ();
	init();
    }
    public UDPCanListener(int localport) throws IOException {
	sock = new UDPCanSocket (localport);
	init();
    }

    public UDPCanListener(int localport, String remaddr, int remport )
    throws IOException {
	sock = new UDPCanSocket(localport, 
				InetAddress.getByName(remaddr), remport);
	init();
    }


    
    public UDPCanListener(int localport, InetAddress remaddr, int remport )
    throws IOException {
	sock = new UDPCanSocket(localport, remaddr, remport);
	init();
    }

    /*
     * init() starts the listener thread which is contained in the
     * CanListener.run() method.
     */

    private void init()
    throws IOException {
	// assert that sock != null. Exit if not the case.
	// TODO: Throw exception? What kind?
	if (sock == null) {
	    return;
	}

	// ok, pass "this" as the Runnable object to a new thread. This works
	// because UDPCanListener inherits from CanListener which in turn
	// implements Runnable.

	Thread myThread = new Thread ((Runnable) this);

	// start the thread.

	myThread.start();

    }

    /*
     * Have to overload this because CanListener's run() method isn't meant to
     * be run.
     */

    public void run()
    {
	try {
	    super.run((CanSocket) sock);
	} catch (IOException e) {
	    // TODO: Log this exception
	    e.printStackTrace();
	}
    }
}
