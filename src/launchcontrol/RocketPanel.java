package launchcontrol;

import cansocket.*;
import widgets.*;

import java.io.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class RocketPanel extends JPanel implements ActionListener
{
	protected final RocketState statusLabel = new RocketState();
	protected final JButton preFlightCheckButton = new JButton("Preflight Check");

	protected CanSocket sock;

	public RocketPanel(CanSocket socket)
	{
		sock = socket;

		setLayout(new FlowLayout());
		// add status for rocket ready
		add(statusLabel);
		// add buttons for manual commands
		add(preFlightCheckButton);
		preFlightCheckButton.setActionCommand("preflight");
		preFlightCheckButton.addActionListener(this);

		new ReaderThread().start();
	}

	public void actionPerformed(ActionEvent event)
	{
		try {
			if(event.getActionCommand().equals("preflight"))
			{
				byte[] preflight = { CanBusIDs.PreflightCheckState };
				sock.write(new CanMessage(CanBusIDs.FC_REQUEST_STATE, 0, preflight));
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
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
