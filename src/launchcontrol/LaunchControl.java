package launchcontrol;

import cansocket.*;
import widgets.*;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;
import java.text.*;
import java.util.*;
import javax.swing.*;

public class LaunchControl extends JPanel
	implements ScheduleListener, CanObserver, ActionListener
{
	// Member variables, presumably used by multiple methods
	protected final DecimalFormat fmt = new DecimalFormat("T+0.0;T-0.0");
	protected java.util.Timer powerSequence = null; // power on or off fc

	protected CanSocket rocketSocket; // rocket communication socket
	protected TCPCanSocket towerSocket;

	protected final Scheduler sched = new Scheduler();

	protected JLabel statusLabel;
	protected JLabel clock;
	protected JButton countdownButton;
	protected AbortButton abortButton;

	protected static final long delay = 250; /* link timeout delay (millisecs) */
	protected final java.util.Timer linkTimer = new java.util.Timer(true /* daemon */);
	protected LinkTimeout task;

	protected class LinkTimeout extends TimerTask
	{
		public void run()
		{
			try {
				sched.abortCountdown();
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
	}

	/** Create LaunchControl GUI, open connections, and start scheduler */
	public LaunchControl(CanDispatch dispatch) throws IOException
	{
		//possible TODO: use gridlayout (or gridbaglayout) instead
		//of using boxes of boxes. The advantage of a gridlayout is
		// all components could be same size, and spaces can be inserted
		// easily and uniformly.
		
		rocketSocket = new UDPCanSocket(Config.getString("rocket.host"), Config.getInt("rocket.port", UDPCanSocket.PORT_SEND));
		dispatch.setSocket(rocketSocket);
		dispatch.add(this);
		
		towerSocket = new TCPCanSocket(Config.getString("tower.host"), 
			Config.getInt("tower.port", TCPCanSocket.DEFAULT_PORT));
			Scheduler.addSchedulableAction("tower", new SocketAction(towerSocket));
	
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		
		// setup top panel
		JPanel topLCPanel = new JPanel();
		topLCPanel.setLayout(new BoxLayout(topLCPanel, BoxLayout.X_AXIS));
		
		// setup top components
		clock = new JLabel();
		statusLabel = new JLabel();
		statusLabel.setBorder(BorderFactory.createLoweredBevelBorder());
		
		// add top components
		topLCPanel.add(clock);
		topLCPanel.add(statusLabel);
		topLCPanel.add(new ShorePower(towerSocket, "Shore"));
		this.add(topLCPanel);
		
		// setup bottom panel
		JPanel bottomLCPanel = new JPanel();
		bottomLCPanel.setLayout(new GridBagLayout());
		this.add(bottomLCPanel);

		GridBagConstraints gbc = new GridBagConstraints();
		gbc.fill = gbc.BOTH;
		gbc.weightx = 1.0;
		gbc.weighty = 1.0;
		
		// setup countdown panel
		JPanel countdownPanel = new JPanel();
		countdownPanel.setLayout(new GridLayout(0, 1));
		
		// setup override panel
		JPanel overridePanel = new JPanel();
		overridePanel.setLayout(new GridLayout(0, 1));

		// setup countdown components
		countdownPanel.add(new CanMessageButton("Preflight Check",
					rocketSocket, CanBusIDs.FC_REQUEST_STATE,
					new byte[] { CanBusIDs.PreflightCheckState }));
		countdownPanel.add(new CanMessageButton("Arm Rocket",
					rocketSocket, CanBusIDs.FC_REQUEST_STATE,
					new byte[] { CanBusIDs.ArmingState }));
		countdownButton = new JButton();
		countdownButton.setActionCommand("start");
		countdownPanel.add(countdownButton);

		// add countdown components
		bottomLCPanel.add(countdownPanel, gbc);

		// setup override components
		overridePanel.add(new CanMessageButton("Boost!",
					rocketSocket, CanBusIDs.FC_REQUEST_STATE,
					new byte[] { CanBusIDs.BoostState },
					true));
		overridePanel.add(new CanMessageButton("Deploy Drogue!",
					rocketSocket, CanBusIDs.FC_REQUEST_STATE,
					new byte[] { CanBusIDs.DeployDrogueState },
					true));
		overridePanel.add(new CanMessageButton("Deploy Main!",
					rocketSocket, CanBusIDs.FC_REQUEST_STATE,
					new byte[] { CanBusIDs.DeployMainState },
					true));

		// add override components
		bottomLCPanel.add(overridePanel, gbc);
		
		// setup/add abort button
		abortButton = new AbortButton();
		bottomLCPanel.add(abortButton, gbc);
		
		// setup power panel
		JPanel fcPowerPanel = new JPanel();
		fcPowerPanel.setLayout(new GridLayout(0, 1));
		
		// setup power components
		JButton fcPowerOnButton = new JButton("FC On");
		fcPowerOnButton.setActionCommand("fc_on");
		fcPowerOnButton.addActionListener(this);
		fcPowerPanel.add(fcPowerOnButton);
		JButton fcPowerOffButton = new JButton("FC Off");
		fcPowerOffButton.setActionCommand("fc_off");
		fcPowerOffButton.addActionListener(this);
		fcPowerPanel.add(fcPowerOffButton);
		
		// add power components
		bottomLCPanel.add(fcPowerPanel, gbc);
		
		// add rocketSocket to scheduler
		Scheduler.addSchedulableAction("rocket", new SocketAction(rocketSocket));
		ended(); // reset the button and label
		sched.addScheduleListener(this, 100);
	}



	public void actionPerformed(ActionEvent event)
	{
		try {
			if(event.getActionCommand().equals("fc_on"))
			{
				fcPower(true);
			} 
			else if (event.getActionCommand().equals("fc_off"))
			{
				if(JOptionPane.showConfirmDialog(this,
					"Are you sure you want to cut power to flight computer?", 
					"Proceed?",
					JOptionPane.YES_NO_OPTION) != JOptionPane.YES_OPTION) 

					return; // user wasn't sure.

				fcPower(false);
			} 
			else if(event.getActionCommand().equals("start"))
			{
				if(JOptionPane.showConfirmDialog(this,
					"Are you sure you want to start the countdown?", "Proceed?",
					JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION)

					sched.startCountdown();
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	protected class AbortButton extends CanMessageButton
	{
		public AbortButton()
		{
			super("ABORT", rocketSocket, CanBusIDs.FC_ABORT_LAUNCH, new byte[0]);
			setBackground(Color.red);
		}

		public void actionPerformed(ActionEvent evt)
		{
			sched.abortCountdown();
			super.actionPerformed(evt);
		}
	}

	/** Power on or off the FC by toggling shore power. 
	 * a new sequence of on/off toggles of the shore power is started. 
	 * The scheduled sequence should be over in ten seconds.
	 * @param fcPower the new desired power state of the 
	 * FC (true=on, false=off) 
	 */
	protected void fcPower(boolean fcPower) 
	{
		/* A two second delay (2000 millisecs) between shore on/off msgs 
		 * which are sent in sequence while trying to turn on or off the FC. */
		int delay = 2000;

		// cancel any prev. sequence
		if (powerSequence != null) 
		{
			powerSequence.cancel();
		}
		powerSequence = new java.util.Timer(true /* daemon */);

		//first set shore power to correct initial state for a short period
		//if fcPower = on, init state is off.  if fcPower = off, init state is on.
		int init_period = 4000; // four seconds until anything else may run.
		powerSequence.schedule(
				new ShorePowerTask(!fcPower, towerSocket), 0 /* run now */);

		// sched 5 things for new sequence
		for (int i = 1; i <= 5; ++i)
		{
			boolean power;
			if (i % 2 != 0) // i is odd
				power= fcPower ; // power == fcPower at 1, 3, 5 secs
			else  // i is even
				power= !fcPower; //on even secs, shore is set to !fcPower
			powerSequence.schedule(
					new ShorePowerTask(power, towerSocket), 
					init_period + i * delay);
		}
	}

	public void message(CanMessage msg)
	{
		// Some messages should be ignored for the purpose of
		// deciding whether we can hear the rocket.
		switch(msg.getId())
		{
			case CanBusIDs.FC_REPORT_LINK_QUALITY:
				return;
		}

		if(task != null)
			task.cancel();
		task = new LinkTimeout();
		linkTimer.schedule(task, delay, 1000);
	}


	public void started()
	{
		SwingUtilities.invokeLater(new Runnable() {
			public void run()
			{
				countdownButton.setText(abortButton.getText());
				countdownButton.removeActionListener(LaunchControl.this);
				countdownButton.addActionListener(abortButton);
			}
		});
		setStatus("Countdown started");
		try {
			SoundAction.playSound(Config.getString("startSound"));
		} catch(Exception e) {
			// ignore
		}
	}

	public void disableAbort()
	{
		// no longer useful
	}

	public void aborted()
	{
		disableAbort();
		setStatus("Countdown aborted: cleaning up");
		try {
			SoundAction.playSound(Config.getString("abortSound"));
		} catch(Exception e) {
			// ignore
		}
	}

	public void ended()
	{
		SwingUtilities.invokeLater(new Runnable() {
			public void run()
			{
				countdownButton.setText("Start Countdown");
				countdownButton.removeActionListener(abortButton);
				countdownButton.addActionListener(LaunchControl.this);
				clock.setText("");
			}
		});
		setStatus("Countdown stopped");
	}

	public void time(final long millis)
	{
		SwingUtilities.invokeLater(new Runnable() {
			public void run()
			{
				clock.setText(fmt.format((float)millis / 1000.0));
			}
		});
	}

	protected void setStatus(final String msg)
	{
		SwingUtilities.invokeLater(new Runnable() {
			public void run()
			{
				statusLabel.setText(msg);
			}
		});
	}
}
