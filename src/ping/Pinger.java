package ping;

import cansocket.*;

import java.io.IOException;
import java.util.Observable;
import java.util.Observer;
import java.util.Vector;


/** A thread to ping a target repeatedly.
 * (This object creates and manages its own thread).
 *
 * This sends a FC_PING_MESSAGE
 * to the socket at a specified rate.  
 *
 * It also should be registered as a listener with 
 * the socket dispatch (CanListener). That can be done
 * manually by the code creating this widget, or that code can
 * just pass in a reference to the dispatch to the constructor.
 */
public class Pinger implements Runnable, Observer
{
	
	protected CanSocket socket; //socket to send messages to
	protected long delay; // in milliseconds
	protected final static boolean DEBUG = true;
	protected Thread myThread;
	protected Vector expected; //holds pings untill they're seen
	protected Vector lost; //pings suspected to be lost
	protected Vector received; //pings we've received

	// ping statistics
	protected int sent, num_received, duplicates;
	
	
	// main exists for CLI usage
	public static void main (String args[]) throws Exception {
		// TODO: we should get rocket address through 
		// config file (like RocketView). 
		//  and/or allow user to specify on command line.
		// also allow user to specify rate
		
		// open socket, create dispatch.
		CanSocket socket = new UDPCanSocket("10.0.0.1" /* rocket */,
				UDPCanSocket.PORT_SEND);

		CanListener dispatch = new CanListener(
				new LogCanSocket(socket, "ping.log"));
		
		// create pinger. 
		Pinger pinger = new Pinger(socket);

		// register pinger with dispatch
		dispatch.addObserver(pinger);

		// start listening on another thread
		new Thread(dispatch).start();

		// start sending
		pinger.start(); 
	}

	/** Create a Pinger object with the specified socket and default delay.
	 * The default delay of 1 second between pings will be used. 
	 * @param socket the CanSocket to send pings over.
	 */
	public Pinger(CanSocket socket)
	{
		this(socket, 1000);
	}

	/** Create a Pinger object with the specified socket and delay.
	 * Note that this creates a thread for this object, but
	 * start() must be called to start it.
	 * @param milliseconds the delay between pings
	 * @param socket the CanSocket to send pings over.
	 */
	public Pinger(CanSocket socket, long milliseconds) 
	{
		this.socket = socket;
		expected = new Vector();
		lost = new Vector();
		received = new Vector();

		setDelay(milliseconds);
		myThread = new Thread(this);
	}


	/** start sending messages.
	 * this start()s this object's thread 
	 */
	public void start()
	{
		myThread.start();
	}

	public void setDelay(long milliseconds) 
	{
		this.delay = milliseconds;
	}


	/** get the next ping (body) value */
	protected long next(long current)
	{
		// TODO: should we use a random #, or a gray code, or
		// a checksum or anything more advanced than "#+1"?
		return current + 1;
	}



	public void run() 
	{
		// get initial value
		// TODO: inital value s/b based on current time,
		// so that we can tell when the first ping was sent.
		// or, just store the initial date and first in sequence
		// (how about '1'), and store the rate. 
		// From that, and with the incoming sequence #, we'll 
		// know when it was sent.  
		// Time Sent = initalTime + (sequence_diff * pauseTime)
		long value = 1; 
		//byte[] data;

		while (true) 
		{
			
			Ping ping = new Ping(value);
			// message will be expected once it is sent.
			expect(ping);

			// send ping message
			if (DEBUG) {
				System.out.println("Sending ping: " + ping);
			}

			try 
			{
			socket.write(
				new CanMessage(CanBusIDs.FC_PING_MESSAGE, 0, 
					ping.getDataBytes()));
			} catch (IOException e)
			{
				System.err.println("IOExeption while sending ping: " + e);
			}

			//DEBUG:
			//handlePing(ping);



			// calculate next value
			value = next(value);
			
			// sleep for a bit.
			try {
				myThread.sleep(delay);
			} catch (InterruptedException e) {
				System.err.println("wait from pinger caught: " + e);
			}
		}
	}
	


	/** Before we can send a ping, we need to expect() it.
	 * This just adds the ping to the expected list,
	 * and increases # of sent pings by 1.  
	 * For every ping sent, this should be called exactly once.
	 * @param p the Ping to expect. 
	 */
	public void expect(Ping p)
	{
		// Store msg to list. 
		++sent;
		expected.add(p);
	}



	public String statisticsToString()
	{
		return "Sent: " + sent + ", Recieved: " + num_received + 
			", Duplicates: NOT IMPLEMENTED\n";
	}



	
	public void update(Observable o, Object arg)
	{
		if (!(arg instanceof CanMessage))
			return;

		update((CanMessage) arg);
	}



	public void update(CanMessage msg)
	{
		if (msg.getId() == CanBusIDs.FC_PING_MESSAGE)
		{
			handlePing(msg.getBody());		
		}
	}



	protected void handlePing(byte[] body)
	{
		handlePing(new Ping(body));
	}

	protected void handlePing(Ping ping) 
	{
		++num_received;

		// get sequence # and look for it in list of expected
		if (expected.contains(ping) == false) 
		{
			if (lost.contains(ping) == false) 
			{
				// if not expected, (and not in possibly_lost list)
				// print error re: magically appearing / corrupted
				// ping returned. 
				System.out.println("Unexpected ping received: " + ping);
			} 
			else 
			{
				// found received ping in lost list.  
				// It isn't really lost then.
				System.out.println("Found previously thought-lost ping: " 
						+ ping);
				lost.remove(ping);
			}
		} 
		else // ping was found in expected list.
		{	
			expected.remove(ping);
			// print ping and time elapsed(TODO)
			System.out.println("Received ping: " + ping); 
			
			//TODO
			// if msg is found but not first on FIFO 
			// print warning re: poss. lost/skipped
			//  ping. put skipped ping(s) on lost list.
			//
			// To do that, before removing ping from expected,
			// look up ping's index.  Then consider all pings
			// btween 0 and index to be lost.  
			// Of course, this requires that the expected list
			// acts more like a LLL than an array (when things are 
			// removed, they shouldn't leave gaps, since a fifo
			// on an array will otherwise shift itself down the
			// array (into higher indices) which would waste
			// memory/time).
		}
		received.add(ping);
		System.out.print(statisticsToString());
	}
}

