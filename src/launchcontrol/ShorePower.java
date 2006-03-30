/* Copyright 2005 Jamey Sharp, Peter Welte
 * 
 * This program is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by the Free
 * Software Foundation; either version 2 of the License, or (at your option)
 * any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for
 * more details.
 *
 * Portland State Aerospace Society (PSAS) is a student branch chapter of the
 * Institute of Electrical and Electronics Engineers Aerospace and Electronics
 * Systems Society. You can reach PSAS at info@psas.pdx.edu.  See also
 * http://psas.pdx.edu/
 */
package launchcontrol;

import cansocket.*;
import widgets.*;

import java.io.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
	
// possible TODO: shorePowerState checkbutton can have image icon
//  displaying a power plug if we're using shore power, 
//  or a battery if we're using rocket battery (quite like a laptop
//  power display). Actually we might just put that in RocketState widget.

/** The ShorePower widget is used to set (and view) state of shore power.
 * When a user selects or deselects the checkbox widget, this object's
 * itemStateChanged method is invoked (this object is set as an ItemListener
 * in the constructor) which will then send the power on, or power off 
 * message (respectively) to the Launch tower specified in the constructor.
 *
 * In general, whenever this object or any other sends a LTR_SET_SPOWER
 * message to the tower, it should immediately send an LTR_GET_SPOWER message.
 * Doing so will cause the tower to send LTR_REPORT_SPOWER, which this 
 * object will be listening.  When that message is recieved, the
 * state of the checkbox is set to the value reported (on or off).
 *
 * Note that when the user (de)selects the widget, LTR_GET_SPOWER is sent
 * after LTR_SET_SPOWER which should allow the checkbox to report the 
 * actual power state according to the hardware on the tower, which will
 * allow the user to see if the shore power is not getting set to the 
 * correct state for any reason.
 *
 * For now it is just a check box, but it may eventually be either a
 * checkbox or an icon depending on if you want to control or just
 * view the state, respectively.
 */
public class ShorePower extends JCheckBox implements ItemListener, CanObserver
{
	
	protected CanSocket sock;
	protected boolean powerState; // true = pwr on, false = pwr off.
	protected boolean ignoreEvents; // true = disable ShorePower's event handler

	protected final Thread reader;

	/** Create a ShorePower widget.  
	 * @param socket the socket to read from to get information
	 * from the launch tower.
	 */
	public ShorePower(CanSocket socket)
	{
		this(socket, null);
	}

	/** Create a ShorePower widget.  
	 * On creation, the constructor asks the tower to report the
	 * state of shore power and creates a thread to listen for 
	 * the response, so when the user sees this widget
	 * it should be reporting the actual power state at that time.
	 * @param socket the socket to read from to get information
	 * from the launch tower.
	 * @param title the title to display next to this checkbox
	 */
	public ShorePower(CanSocket socket, String title)
	{
		sock = socket;
		addItemListener(this);
		if (title != null)
			setText(title);

		CanDispatch dispatch = new CanDispatch(socket);
		dispatch.add(this);
		reader = new Thread(dispatch);

		/* Request the power at startup. */
		requestState();
	}

	protected void requestState()
	{
		try {
			CanMessage requestMessage = new CanMessage(CanBusIDs.LTR_GET_SPOWER,
					0, new byte[8]);
			sock.write(requestMessage);
			sock.flush();
			if(!reader.isAlive())
				reader.start();
		} catch(IOException e) {
			System.err.println("write failed, will retry later: " + e);
		}
	}


	/** Tells tower to set shore power to current state of widget.
	 * This method is called automatically when the state of the 
	 * widget changes.  
	 * Note that the widget will change, and thus this method will 
	 * be called, either when the user 
	 * wishes the shore power to change (user (de)selects it) or when
	 * this object recieves the LTR_REPORT_SPOWER message from the
	 * tower socket (code (de)selects it).  In the later case,
	 * this widget should not tell the tower to set power to the 
	 * state it just reported power to be in, and so the code
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
		if (!ignoreEvents) {
			System.out.println("ShorePower widget acting on item event");
			byte body[] = new byte[8];
			if (event.getStateChange() == ItemEvent.DESELECTED) {
				body[0] = 0;
				System.out.println("\tshore power DESELECTED, telling tower");
			} else {
				body[0] = 1;
				System.out.println("\tshore power SELECTED, telling tower");
			}
			try {
				CanMessage myMessage = new CanMessage(CanBusIDs.LTR_SET_SPOWER, 0, body);
				sock.write(myMessage);
				requestState();
			} catch(IOException e) {
				System.err.println("write failed, will retry later: " + e);
			}
		}
	}

	/** possibly update the widget based on the message read from tower. */
	public void message(CanMessage msg) {
		// if msg is a LTR_REPORT_SPOWER (XXX: ensure its not actually LTR_SPOWER)
		// then set power to first byte.
		if (msg.getId() == CanBusIDs.LTR_REPORT_SPOWER) {
			if (msg.getData8(0) == 1) 
				powerState = true;
			else 
				powerState = false;

			// change the state of this widget, ensuring our event handler isn't
			// called, because this isn't a user generated change of state.
			//
			// XXX: possible BUG.  see my comment about when events are 
			// generated in the comment for itemStateChanged() above.
			ignoreEvents = true; 
			setSelected(powerState);
			ignoreEvents = false;
		}
	}
}
