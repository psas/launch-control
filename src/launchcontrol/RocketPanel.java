package launchcontrol;

import cansocket.*;

import java.io.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class RocketPanel extends JPanel
{
	int rocketState;
	protected CanSocket sock;
	protected JLabel statusLabel = new JLabel("ROCKET SAFE");

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
		rocketState = 0;
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
				{
					if ( !(m instanceof CanMessage) )
						continue;

					CanMessage c = (CanMessage) m;
					if ( c.getId11() != StatusID )
						continue;

					rocketState = c.getData8(0);
					if (rocketState >= 0 && rocketState < stateStrings.length)
						statusLabel.setText(stateStrings[rocketState]);
					else
						statusLabel.setText("Unknown state(" + Integer.toString(rocketState) + ")");
						
					if (rocketState == 3)  // IDLE
					{
						byte[] preflight = new byte[1];
						preflight[0] = 4;  // PREFLIGHT
						CanMessage preflightMsg = new CanMessage(0, StatReqID, 0, 1, preflight );
						sock.write(preflightMsg);
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}