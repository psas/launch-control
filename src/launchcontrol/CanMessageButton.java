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
