package launchcontrol;

import cansocket.*;
import widgets.*;

import java.io.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/**
 * Checkbox used to modify and display state of a Launch Tower relay.
 */

public class RelayCheckBox extends JCheckBox implements ItemListener, CanObserver
{
	protected CanSocket sock;
	protected String title;
	protected int getMsg;
	protected int setMsg;
	protected int reportMsg;
	protected boolean ignoreEvents; // true = disable ShorePower's event handler

	protected final Thread reader;

	/** Create a RelayCheckBox widget.  
	 * @param socket the socket to read from to get information
	 * from the launch tower.
	 */
	public RelayCheckBox(CanSocket socket)
	{
		this(socket, null, CanBusIDs.LTR_SPOWER);
	}

	/* XXX these message-part constants should actually be in CanBusIDs */
	public static int GET    = 0x1310;
	public static int SET    = 0x1001;
	public static int REPORT = 0x1301;

	/** Create a RelayCheckBox widget.  
	 * @param socket the socket to read from to get information
	 * from the launch tower.
	 * @param relay  the base message from which get/set/report messages are constructed
	 */
	public RelayCheckBox(CanSocket socket, String title, int relay)
	{
		this(socket, title, relay | GET, relay | SET, relay | REPORT);
	}

	/** Create a RelayCheckBox widget.
	 * On creation, the constructor asks the tower to report the
	 * state of relay and creates a thread to listen for 
	 * the response, so when the user sees this widget
	 * it should be reporting the actual relay state at that time.
	 * @param socket the socket to read from to get information
	 * from the launch tower.
	 * @param title the title to display next to this checkbox
	 */
	public RelayCheckBox(CanSocket socket, String title, int get, int set, int report)
	{
		sock = socket;
		this.title = title;
		if (title != null)
			setText(title);
		getMsg = get;
		setMsg = set;
		reportMsg = report;

		addItemListener(this);

		CanDispatch dispatch = new CanDispatch(socket);
		dispatch.add(this);
		reader = new Thread(dispatch);

		/* Request the state at startup. */
		requestState();
	}

	protected void requestState()
	{
		try {
			sock.write(new CanMessage(getMsg, 0, new byte[8]));
			sock.flush();
			if(!reader.isAlive())
				reader.start();
		} catch(IOException e) {
			System.err.println("write failed, will retry later: " + e);
		}
	}


	/** Tells tower to set relay to current state of widget.
	 * This method is called automatically when the state of the 
	 * widget changes.  
	 * Note that the widget will change, and thus this method will 
	 * be called, either when the user 
	 * wishes the shore power to change (user (de)selects it) or when
	 * this object recieves the REPORT message from the
	 * tower socket (code (de)selects it).  In the later case,
	 * this widget should not tell the tower to set the relay to the 
	 * state it just reported the relay to be in, and so the code
	 * changing the state (using setSelected(boolean state)) should
	 * set ignoreEvents to true and this method will do nothing.
	 *
	 * Hell, english is hard.  I suggest you simply read the code.
	 *
	 * Note also that the event is only supposed to be sent, 
	 * and this method invoked, when the user does something
	 * to the widget.  Unfortunately, however, that seems not 
	 * to be the case and those events are sent whenever the state
	 * of the widget changes.  That seems buggy to me - feel free
	 * to prove me wrong and fix.
	 *
	 * @param event the high level event sent by this object
	 * when the user(!) selects or deselects the widget.
	 */
	public void itemStateChanged(ItemEvent event) {
		if (ignoreEvents)
			return;

		System.out.println(title + " widget acting on item event");
		byte body[] = new byte[8];
		if (event.getStateChange() == ItemEvent.DESELECTED) {
			body[0] = 0;
			System.out.println("\t" + title + " DESELECTED, telling tower");
		} else {
			body[0] = 1;
			System.out.println("\t" + title + " SELECTED, telling tower");
		}
		try {
			sock.write(new CanMessage(setMsg, 0, body));
			requestState();
		} catch(IOException e) {
			System.err.println("write failed, will retry later: " + e);
		}
	}

	/** possibly update the widget based on the message read from tower. */
	public void message(CanMessage msg) {
		// if msg is a LTR_REPORT_SPOWER (XXX: ensure its not actually LTR_SPOWER)
		// then set power to first byte.
		if (msg.getId() == reportMsg) {
			// change the state of this widget, ensuring our event handler isn't
			// called, because this isn't a user generated change of state.
			//
			// XXX: possible BUG.  see my comment about when events are 
			// generated in the comment for itemStateChanged() above.
			ignoreEvents = true; 
			setSelected(msg.getData8(0) == 1);
			ignoreEvents = false;
		}
	}
	
}
