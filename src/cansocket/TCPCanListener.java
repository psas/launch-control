// TCPCanListener.java

// started by Karl Hallowell June 26, 2003.

/** TCPCanListener listens to a socket for UDP-based Can messages. Use
 * underlying CanListener class to actually run the listening thread.
 */

package cansocket;

import java.io.*;
import java.net.*;

public class TCPCanListener extends CanListener {
    private TCPCanSocket sock = null;

    /*
     * the creation methods are more complicated because I have to pass through
     * the parameters for creating a TCPCanSocket. Maybe this mess of methods
     * can be simplified? Creation is simple. Initialize sock and then invoke
     * the init() method.
     */

    public TCPCanListener() throws IOException {
	sock = new TCPCanSocket ();
	init();
    }


    /*
     * Copied over from UDPCanListener, but these aren't supported.
     * UDPCanSocket and TCPCanSocket need to have similar creation
     * methods IMHO because one might want to swap UDP and
     * TCP connections.

    public TCPCanListener(int localport) throws IOException {
        sock = new TCPCanSocket (localport);
	init();
    }

    
    public TCPCanListener(int localport, InetAddress remaddr, int remport )
    throws IOException {
	sock = new TCPCanSocket(localport, remaddr, remport);
	init();
    }
    */

    // The TCPCanSocket methods that are supported.
    /*
    public TCPCanListener(String host) throws IOException {
	sock = new TCPCanSocket (host);
	init();
    }

    public TCPCanListener(String host, int port) throws IOException {
	sock = new TCPCanSocket (host, port);
	init();
    }
    */

    public TCPCanListener(int port) throws IOException {
	sock = new TCPCanSocket(port, 100);
	init();
    }


    public TCPCanListener(int port, int backlog) throws IOException {
	sock = new TCPCanSocket(port, backlog);
	init();
    }

    /**
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
	// because TCPCanListener inherits from CanListener which in turn
	// implements Runnable.

	Thread myThread = new Thread ((Runnable) this);

	// start the thread.

	myThread.start();

    }

    /*
     * Have to overload this because CanListener's run() method isn't meant to
     * be run.
     */

    public void run() {
	try {
	    super.run((CanSocket) sock);
	} catch (IOException e) {
	    // TODO: log this exception...
	    e.printStackTrace();
	}
    }
}
