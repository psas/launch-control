package launchcontrol;

import cansocket.*;
import widgets.*;

import java.io.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class RocketPanel extends JPanel
{
	protected final RocketState statusLabel = new RocketState();
	protected CanSocket sock;

	public RocketPanel(CanSocket socket, LaunchControl parent)
	{
		sock = socket;
		statusLabel.addLinkStateListener(parent);

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
					statusLabel.update(m);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
