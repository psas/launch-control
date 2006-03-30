/* Copyright 2005 David Cassard, Ian Osgood, Jamey Sharp, Peter Welte,
 *                Tim Welch
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
package rocketview;

import cansocket.*;

import java.awt.Font;
import java.lang.reflect.*;
import javax.swing.*;

/*----------------------------------------------------------------
 * Prints all message to the message text box
 */
class TextObserver extends JScrollPane implements CanObserver
{
	protected static final String msgSyms[] = new String[0x1000];

	static {
		// initialize the map of CAN msg symbols
		Field fields[] = CanBusIDs.class.getFields();
		for(int i = 0; i < fields.length; i++)
			try {
				int msg = fields[i].getInt(null) >>> 4;
				msgSyms[msg] = fields[i].getName();
			} catch(IllegalAccessException e) {
				System.err.println("CanBusIDs field " + i + ": " + e);
			}
	}

	protected final JTextArea text = new JTextArea(15, 40); // rows, columns
    
	public TextObserver(CanDispatch dispatch) throws IllegalAccessException
    {
	this.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
	dispatch.add(this);

	text.setLineWrap(true);
	text.setFont(new Font("Monospaced", Font.PLAIN, 10));
	setViewportView(text);
    }

    public void message(CanMessage msg)
    {
	int nid = msg.getId() & (0x1f << 11);
	switch(nid)
	{
		case CanBusIDs.FC_NID:
		case CanBusIDs.FC_IMU_NID:
		case CanBusIDs.FC_GPS_NID:
			return;
	}

	int verb = msg.getId() & ((0x3 << 8) | CanBusIDs.CID_REQUEST);
	switch(verb)
	{
		/* XXX: would like to filter CID_REPORT, but it's the
		 * same verb as CID_ERROR, so we can't. For the same
		 * reason, don't block CID_INFO or related. */
		case CanBusIDs.CID_ACTION_BC:
		case CanBusIDs.CID_TEST:
		case CanBusIDs.CID_SET:
		case CanBusIDs.CID_ACK:
		case CanBusIDs.CID_GET:
		case CanBusIDs.CID_DATA:
			return;
	}

	// filter out all id's that are handled elsewhere
	switch(msg.getId())
	{
		case CanBusIDs.REC_REPORT_MODE:
		case CanBusIDs.REC_REPORT_PYRO:
		case CanBusIDs.REC_REPORT_TIMER:
			return;
	}

	StringBuffer buf = new StringBuffer();
	String name = msgSyms[msg.getId() >>> 4];
	if(name != null)
		buf.append(name).append(": ");
	buf.append(msg).append("\n");
	text.append(buf.toString());

	//Try to keep the scrollpane looking at the tail of the log
	final JScrollBar vertBar = getVerticalScrollBar();
	SwingUtilities.invokeLater(new Runnable() {
	  public void run() {
	    if (! vertBar.getValueIsAdjusting()) 
	      vertBar.setValue(vertBar.getMaximum());
	  }
	});
    }
}
