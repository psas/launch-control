package launchcontrol;

import cansocket.*;
import widgets.*;

import java.io.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class RocketPanel extends JPanel //implements ActionListener
{
	protected final RocketState statusLabel = new RocketState();
	protected CanSocket sock;
	protected LaunchControl lc;

	public RocketPanel(CanSocket socket, LaunchControl parent)
	{
		sock = socket;
		lc = parent;

		setLayout(new FlowLayout());
		// add status for rocket ready
		add(statusLabel);
		
		new ReaderThread().start();
	}

	private class ReaderThread extends Thread
	{
		public void run()
		{
			try {
				CanMessage m;
				while ((m = sock.read()) != null)
				{
					statusLabel.update(m);
					//lc.update(m);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
