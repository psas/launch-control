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
	protected final JButton armButton = new JButton("Arm Rocket");
	protected final JButton powerDownButton = new JButton("Power Down Rocket");

	protected CanSocket sock;
	protected LaunchControl lc;

	public RocketPanel(CanSocket socket, LaunchControl parent)
	{
		sock = socket;
		lc = parent;

		setLayout(new FlowLayout());
		// add status for rocket ready
		add(statusLabel);
		// add buttons for manual commands
		add(preFlightCheckButton);
		preFlightCheckButton.setActionCommand("preflight");
		preFlightCheckButton.addActionListener(this);

		add(armButton);
		armButton.setActionCommand("arm");
		armButton.addActionListener(this);
		armButton.addActionListener(parent);

		add(powerDownButton);
		powerDownButton.setActionCommand("powerdown");
		powerDownButton.addActionListener(this);
		
		new ReaderThread().start();
	}

	public void actionPerformed(ActionEvent event)
	{
		try {
			if(event.getActionCommand().equals("preflight"))
			{
				byte[] data = { CanBusIDs.PreflightCheckState };
				sock.write(new CanMessage(CanBusIDs.FC_REQUEST_STATE, 0, data));
			}
			else if (event.getActionCommand().equals("arm"))
			{
				byte[] data = { CanBusIDs.ArmedState };
				sock.write(new CanMessage(CanBusIDs.FC_REQUEST_STATE, 0, data));
			}
			else if (event.getActionCommand().equals("powerdown"))
			{
				byte[] data = { CanBusIDs.PowerDownState };
				sock.write(new CanMessage(CanBusIDs.FC_REQUEST_STATE, 0, data));
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
