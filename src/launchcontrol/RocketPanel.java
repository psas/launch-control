package launchcontrol;

import cansocket.*;

import java.io.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class RocketPanel extends JPanel
{
	private JLabel statusLabel = new JLabel();

	protected int state;
	protected CanSocket sock;

	protected static final int StatusID = Config.getInt("rocket.status", 8);
	protected static final int RocketReadyState = Config.getInt("rocket.stReady", 7);
	
	protected static final int StatReqID = Config.getInt("rocket.request", 0);
	
	protected static final String[] stateStrings = {
		"TURNED ON",
		"POWER UP",
		"INITIALIZE",
		"IDLE",
		"PREFLIGHT CHECK",
		"READY",
		"ARMED",
		"ROCKET READY",
		"LAUNCH ABORT",
		"BOOST",
		"COAST",
		"DEPLOY DROGUE",
		"DESCEND DROGUE",
		"DEPLOY MAIN",
		"DESCEND MAIN",
		"RECOVERY WAIT",
		"RECOVERY SLEEP",
		"POWER DOWN"
	};

	public RocketPanel(CanSocket socket)
	{
		sock = socket;

		// add buttons for manual commands
		// add status for rocket ready
		setLayout(new BorderLayout());
		add(statusLabel, BorderLayout.CENTER);

		setState(-1);
		new ReaderThread().start();
	}

	protected void setState(int state)
	{
		this.state = state;
		if(state < 0)
			statusLabel.setText("Unknown state");
		else if(state >= stateStrings.length)
			statusLabel.setText("Unknown state (" + Integer.toString(state) + ")");
		else
			statusLabel.setText(stateStrings[state]);
	}

	protected void changeState() throws IOException
	{
		if(state == CanBusIDs.IdleState)
		{
			byte[] preflight = { CanBusIDs.PreflightCheckState };
			sock.write(new CanMessage(0, CanBusIDs.FC_REQUEST_STATE >> 5, 0, 1, preflight));
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
					if ( !(m instanceof CanMessage) )
						continue;

					CanMessage c = (CanMessage) m;
					if(c.getId11() != CanBusIDs.FC_REPORT_STATE >> 5)
						continue;

					setState(c.getData8(0));
					changeState();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
