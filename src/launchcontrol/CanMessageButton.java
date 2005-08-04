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

	public CanMessageButton(String name, CanSocket socket, int id, byte[] data)
	{
		super(name);
		this.socket = socket;
		this.id = id;
		this.data = data;
		addActionListener(this);
	}

	public void actionPerformed(ActionEvent evt)
	{
		try {
			socket.write(new CanMessage(id, 0, data));
		} catch(IOException ioe) {
			System.err.println("\"" + getText() + "\" failed: " + ioe);
		}
	}
}
