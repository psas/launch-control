/* Copyright 2005 Jamey Sharp
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

import java.io.*;
import java.awt.event.*;
import javax.swing.*;

import cansocket.*;

public class CanMessageButton extends JButton implements ActionListener
{
	protected final CanSocket socket;
	protected final int id;
	protected final byte[] data;
	protected final boolean confirm;

	public CanMessageButton(String name, CanSocket socket, int id, byte[] data, boolean confirm)
	{
		super(name);
		this.socket = socket;
		this.id = id;
		this.data = data;
		this.confirm = confirm;
		addActionListener(this);
	}

	public CanMessageButton(String name, CanSocket socket, int id, byte[] data)
	{
		this(name, socket, id, data, false);
	}

	public void actionPerformed(ActionEvent evt)
	{
		try {
			if(!confirm
			   || (JOptionPane.showConfirmDialog(this,
                                   "Run emergency action \""+getText()+"\"?", "Proceed?",
                                   JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION)) {
				socket.write(new CanMessage(id, 0, data));
			}
		} catch(IOException ioe) {
			System.err.println("\"" + getText() + "\" failed: " + ioe);
		}
	}
}
