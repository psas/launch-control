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
	implements ScheduleListener, LinkStateListener, ActionListener
{
	// Class constants
	protected final static String stopMsg = "ABORT";
	protected final static String startMsg = "Start Countdown";
	protected final static String stoppedMsg = "Countdown stopped";

	// Member variables, presumably used by multiple methods
	protected final DecimalFormat fmt = new DecimalFormat("T+0.0;T-0.0");
	protected java.util.Timer powerSequence = null; // power on or off fc
	protected String startSound = Config.getString("startSound");
	protected String abortSound = Config.getString("abortSound");

	protected CanSocket rocketSocket; // rocket communication socket
	protected TCPCanSocket towerSocket;

	protected final Scheduler sched = new Scheduler();
	
	// Launch control panels and components, tabbed to show hierarchy
	protected JPanel topLCPanel;
		protected JLabel statusLabel;
		protected JLabel clock;
		protected ShorePower shorePowerState;
	protected JPanel bottomLCPanel;
		protected JPanel countdownPanel;
			protected JButton preFlightCheckButton;
			protected JButton armButton;
			protected JButton countdownButton;
		protected JPanel overridePanel;
			protected JButton boostButton;
			protected JButton deployDrogueButton;
			protected JButton deployMainButton;
			protected JButton abortButton;
		protected JPanel fcPowerPanel;
			protected JButton fcPowerOnButton;
			protected JButton fcPowerOffButton;

	/** Create LaunchControl GUI, open connections, and start scheduler */
	public LaunchControl(CanDispatch dispatch) throws IOException
	{
		//possible TODO: use gridlayout (or gridbaglayout) instead
		//of using boxes of boxes. The advantage of a gridlayout is
		// all components could be same size, and spaces can be inserted
		// easily and uniformly.
		
		rocketSocket = new UDPCanSocket(Config.getString("rocket.host"), Config.getInt("rocket.port", UDPCanSocket.PORT_SEND));
			dispatch.setSocket(rocketSocket);
		
		towerSocket = new TCPCanSocket(Config.getString("tower.host"), 
			Config.getInt("tower.port", TCPCanSocket.DEFAULT_PORT));
			Scheduler.addSchedulableAction("tower", new SocketAction(towerSocket));
	
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		
		// setup top panel
		topLCPanel = new JPanel();
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
		bottomLCPanel = new JPanel();
		bottomLCPanel.setLayout(new BoxLayout(bottomLCPanel, BoxLayout.X_AXIS));
		this.add(bottomLCPanel);
		
		// setup countdown panel
		countdownPanel = new JPanel();
		countdownPanel.setLayout(new GridLayout(0, 1));
		
		// setup override panel
		overridePanel = new JPanel();
		overridePanel.setLayout(new GridLayout(0, 1));

		// setup countdown components
		preFlightCheckButton = new JButton("Preflight Check");
		preFlightCheckButton.setActionCommand("preflight");
		preFlightCheckButton.addActionListener(this);
        armButton = new JButton("Arm Rocket");
        armButton.setActionCommand("arm");
        armButton.addActionListener(this);
        countdownButton = new JButton();
        countdownButton.addActionListener(this);

		// setup override components
		boostButton = new JButton("Boost!");
		boostButton.setActionCommand("boost");
		boostButton.addActionListener(this);
        deployDrogueButton = new JButton("Deploy Drogue!");
        deployDrogueButton.setActionCommand("drogue");
        deployDrogueButton.addActionListener(this);
        deployMainButton = new JButton("Deploy Main!");
        deployMainButton.setActionCommand("main");
        deployMainButton.addActionListener(this);
		
		// add countdown components
		countdownPanel.add(preFlightCheckButton);
		countdownPanel.add(armButton);
		countdownPanel.add(countdownButton);
		bottomLCPanel.add(countdownPanel);

		// add override components
		overridePanel.add(boostButton);
		overridePanel.add(deployDrogueButton);
		overridePanel.add(deployMainButton);
		bottomLCPanel.add(overridePanel);
		
		// setup/add abort button
		abortButton = new JButton("ABORT");
		abortButton.setActionCommand("abort");
		abortButton.addActionListener(this);
		abortButton.setPreferredSize(countdownPanel.getPreferredSize());
		abortButton.setBackground(Color.red);
		abortButton.setMaximumSize(countdownPanel.getPreferredSize());
		bottomLCPanel.add(abortButton);
		
		// setup power panel
		fcPowerPanel = new JPanel();
		fcPowerPanel.setLayout(new GridLayout(0, 1));
		
		// setup power components
		fcPowerOnButton = new JButton("FC On");
		fcPowerOnButton.setActionCommand("fc_on");
		fcPowerOnButton.addActionListener(this);
		fcPowerOffButton = new JButton("FC Off");
		fcPowerOffButton.setActionCommand("fc_off");
		fcPowerOffButton.addActionListener(this);
		
		// add power components
		fcPowerPanel.add(fcPowerOnButton);
		fcPowerPanel.add(fcPowerOffButton);
		bottomLCPanel.add(fcPowerPanel);
		
		// add rocketSocket to scheduler
		Scheduler.addSchedulableAction("rocket", new SocketAction(rocketSocket));
		ended(); // reset the button and label
		sched.addScheduleListener(this, 100);
		
	}



	public void actionPerformed(ActionEvent event)
	{
		try {
			if (event.getActionCommand().equals("preflight"))
			{
				byte[] data = { CanBusIDs.PreflightCheckState };
				rocketSocket.write(new CanMessage(CanBusIDs.FC_REQUEST_STATE, 0, data));
			}
			else if (event.getActionCommand().equals("arm"))
			{
				byte[] data = { CanBusIDs.ArmingState };
				rocketSocket.write(new CanMessage(CanBusIDs.FC_REQUEST_STATE, 0, data));
			}
			else if (event.getActionCommand().equals("boost"))
			{
				byte[] data = { CanBusIDs.BoostState };
				rocketSocket.write(new CanMessage(CanBusIDs.FC_REQUEST_STATE, 0, data));
			}
			else if (event.getActionCommand().equals("drogue"))
			{
				byte[] data = { CanBusIDs.DeployDrogueState };
				rocketSocket.write(new CanMessage(CanBusIDs.FC_REQUEST_STATE, 0, data));
			}
			else if (event.getActionCommand().equals("main"))
			{
				byte[] data = { CanBusIDs.DeployMainState };
				rocketSocket.write(new CanMessage(CanBusIDs.FC_REQUEST_STATE, 0, data));
			}
			else if(event.getActionCommand().equals("fc_on"))
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
			else if(event.getActionCommand().equals("abort"))
			{
				System.out.println("got abort request");
				// XXX: if countdown is near T-0, which s/b done first?
				sched.abortCountdown(); // tell countdown sequencer to abort 
				// tell rocket's sequencer to abort
				byte[] data = new byte[8];
				rocketSocket.write(
						new CanMessage(CanBusIDs.FC_ABORT_LAUNCH, 0, data));
			}

		} catch(Exception e) {
			e.printStackTrace();
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


	public void linkStateChanged(boolean state)
	{
		if(!state)
			try {
				sched.abortCountdown();
			} catch(Exception e) {
				e.printStackTrace();
			}
	}


	public void started()
	{
		SwingUtilities.invokeLater(new Runnable() {
			public void run()
			{
				countdownButton.setText(stopMsg);
				countdownButton.setActionCommand("abort");
			}
		});
		setStatus("Countdown started");
		try {
			SoundAction.playSound(startSound);
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
			SoundAction.playSound(abortSound);
		} catch(Exception e) {
			// ignore
		}
	}

	public void ended()
	{
		SwingUtilities.invokeLater(new Runnable() {
			public void run()
			{
				countdownButton.setText(startMsg);
				countdownButton.setActionCommand("start");
				clock.setText("");
			}
		});
		setStatus(stoppedMsg);
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

	public static void main(String args[]) throws IOException
	{
		JFrame frame = new JFrame("LaunchControl");
		Container content = frame.getContentPane();

		CanDispatch dispatch = new CanDispatch();

		RocketState state = new RocketState();
		LaunchControl control = new LaunchControl(dispatch);

		state.addLinkStateListener(control);
		dispatch.add(state);

		content.add(state, BorderLayout.NORTH);
		content.add(control, BorderLayout.CENTER);

		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.pack();
		frame.setVisible(true);

		dispatch.run();
	}
}
