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

	public RocketPanel(CanSocket socket)
	{
		sock = socket;

		// add buttons for manual commands
		// add status for rocket ready
		setLayout(new BorderLayout());
		add(statusLabel, BorderLayout.CENTER);

		new ReaderThread().start();
	}

	private class ReaderThread extends Thread
	{
		public void run()
		{
			try {
				NetMessage m;
				while ((m = sock.read()) != null)
					statusLabel.update(m);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
