package launchcontrol;

import cansocket.*;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;
import java.text.*;
import java.util.*;
import javax.swing.*;

public class LaunchControl extends JFrame
	implements ScheduleListener, ActionListener, ItemListener
{
	protected final static String startMsg = "Start Countdown";
	protected final static String stopMsg = "ABORT";
	protected final static String stoppedMsg = "Countdown stopped";
	protected final DecimalFormat fmt = new DecimalFormat("T+0.0;T-0.0");

	protected final JButton preFlightCheckButton = new JButton("Preflight Check");
	protected final JButton armButton = new JButton("Arm Rocket");
	protected JButton countdownButton = new JButton();
	protected JButton abortButton = new JButton(stopMsg);
	protected JButton fcPowerOnButton = new JButton("FC On");
	protected JButton powerDownButton = new JButton("Powerdown FC");
	protected JButton fcPowerOffButton = new JButton("FC Off");
	protected java.util.Timer powerSequence = null; // power on or off fc
	protected JCheckBox shorePowerState = new JCheckBox("Shore");
	// possible TODO: shorePowerState checkbutton can have image icon
	//  displaying a power plug if we're using shore power, 
	//  or a battery if we're using rocket battery (quite like a laptop
	//  power display).
	protected JLabel clock = new JLabel();
	protected JLabel statusLabel = new JLabel();
	protected String startSound = Config.getString("startSound");
	protected String abortSound = Config.getString("abortSound");
	protected CanSocket rocketSocket; // rocket communication socket
	protected TCPCanSocket towerSocket;
	protected final Dimension windowSize = new Dimension((int)(300 * 1.61803399), 300);

	protected class ShorePowerTask extends TimerTask {
		private boolean power;
		private CanMessage powerOn;
		private CanMessage powerOff;

		public ShorePowerTask (boolean power_state) {
			power = power_state;
			short id = CanBusIDs.LTR_SET_SPOWER;
			int timestamp = 0;
			byte onBody[] = new byte[8];
			byte offBody[] = new byte[8];
			onBody[0] = 1;
			offBody[0] = 0;
			powerOn = new CanMessage(id, timestamp, onBody);
			powerOff = new CanMessage(id, timestamp, offBody);
		}

		public void run() {
			CanMessage powerMessage;
			if (power)  // set shore power on
				powerMessage = powerOn;
			else  // set shore power off
				powerMessage = powerOff;
				
			if (towerSocket == null) {
				System.out.println(
					"can't send power command (towerSocket null); connected?");
			} else {
				try {
					if (power) {
						System.out.println("ShorePowerTask->powerOn");
						//BROKEN/BUG: setSelected doesn't fire actionevent,
						//but it does appear to fire itemchanged event.
						//this causes power message to be sent twice
						//shorePowerState.setSelected(true);
					} else {
						System.out.println("ShorePowerTask->powerOff");
						//shorePowerState.setSelected(false);
					}

					towerSocket.write(powerMessage);
					towerSocket.flush();
				} catch(Exception e) {
					e.printStackTrace();
				}
			}
		}
	}


	protected final Scheduler sched = new Scheduler();

	private LaunchControl() throws IOException
	{
		super("LaunchControl");
		//possible TODO: use gridlayout (or gridbaglayout) instead
		//of using boxes of boxes. The advantage of a gridlayout is
		// all components could be same size, and spaces can be inserted
		// easily and uniformly.
		
		rocketSocket = new UDPCanSocket(Config.getString("rocket.host"), Config.getInt("rocket.port", UDPCanSocket.PORT_SEND));

		Box layout_box;
		Dimension x_spacer_dim = new Dimension(5,0);
		Dimension y_spacer_dim = new Dimension(0,10);
		Component x_spacer = Box.createRigidArea(x_spacer_dim);
		Component y_spacer = Box.createRigidArea(y_spacer_dim);
		Container content = getContentPane();
		content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));

		// State monitoring box
		  // also pass the rocket socket to a thread for listening
		content.add(new RocketPanel(rocketSocket, this));
		//layout_box.add(new RocketPanel(rocketSocket, this));
		
		layout_box = new Box(BoxLayout.X_AXIS);
		layout_box.add(Box.createRigidArea(x_spacer_dim));
		layout_box.add(clock);
		layout_box.add(Box.createRigidArea(x_spacer_dim));
		Container statusBar = new JPanel();
		statusBar.setLayout(new BorderLayout());
		statusLabel.setBorder(BorderFactory.createLoweredBevelBorder());
		statusBar.add(statusLabel, BorderLayout.CENTER);
		layout_box.add(statusBar);
		layout_box.add(Box.createRigidArea(x_spacer_dim));
		content.add(layout_box);
		content.add(Box.createRigidArea(y_spacer_dim));



				
		// Sequence button box
		/*
		JPanel layout_pane = new JPanel();
		layout_pane.setLayout(new GridLayout(1,0));
		*/
		layout_box = new Box(BoxLayout.X_AXIS);
		//Box countdown_box = new Box(BoxLayout.Y_AXIS);
		Container countdown_pane = new JPanel();
		countdown_pane.setLayout(new GridLayout(0,1));
		countdown_pane.add(preFlightCheckButton);
		preFlightCheckButton.setActionCommand("preflight");
		preFlightCheckButton.addActionListener(this);

		countdown_pane.add(armButton);
		armButton.setActionCommand("arm");
		armButton.addActionListener(this);

		countdown_pane.add(countdownButton);
		countdownButton.addActionListener(this);

		/*
		int height = preFlightCheckButton.getPreferredSize().height +
			armButton.getPreferredSize().height + 
			countdownButton.getPreferredSize().height;
			*/
		/*
		int pfcb_width = preFlightCheckButton.getPreferredSize().width;
		int ab_width = armButton.getPreferredSize().width;
		int cb_width = countdownButton.getPreferredSize().width;
		int width = pfcb_width >= ab_width ? pfcb_width : ab_width;
		width = width >= cb_width ? width : cb_width;

		preFlightCheckButton.setPreferredSize( new Dimension(width, 
				preFlightCheckButton.getPreferredSize().height));

		armButton.setPreferredSize( new Dimension(width, 
				armButton.getPreferredSize().height));

		countdownButton.setPreferredSize(new Dimension(width, 
				countdownButton.getPreferredSize().height));
		*/
		abortButton.setPreferredSize(countdown_pane.getPreferredSize());
		abortButton.setBackground(Color.red);
		//abortButton.setSize(countdown_pane.getPreferredSize());
		abortButton.setMaximumSize(countdown_pane.getPreferredSize());
		Dimension countdown_size = countdown_pane.getPreferredSize();
		/*
		System.out.println("cntdown box pref size: " + 
				countdown_pane.getPreferredSize());
		System.out.println("abort btn pref size: " + 
				abortButton.getPreferredSize());
		System.out.println("abort btn max size: " + 
				abortButton.getMaximumSize());
				*/
		
		layout_box.add(Box.createRigidArea(x_spacer_dim));
		layout_box.add(countdown_pane);
		layout_box.add(Box.createRigidArea(x_spacer_dim));
		layout_box.add(abortButton);
		layout_box.add(Box.createRigidArea(x_spacer_dim));
		abortButton.setActionCommand("abort");
		abortButton.addActionListener(this);

		content.add(layout_box); 
		content.add(Box.createRigidArea(y_spacer_dim));

	

		// Power button box
		layout_box = new Box(BoxLayout.X_AXIS);
		layout_box.add(Box.createRigidArea(x_spacer_dim));
		Container fcpower_pane = new JPanel();
		fcpower_pane.setLayout(new GridLayout(0, 1));

		fcpower_pane.add(fcPowerOnButton);
		fcPowerOnButton.setActionCommand("fc_on");
		fcPowerOnButton.addActionListener(this);

		fcpower_pane.add(powerDownButton);
		powerDownButton.setActionCommand("powerdown");
		powerDownButton.addActionListener(this);

		fcpower_pane.add(fcPowerOffButton);
		fcPowerOffButton.setActionCommand("fc_off");
		fcPowerOffButton.addActionListener(this);
		
		//fcpower_pane.setSize(countdown_pane.getPreferredSize());
		layout_box.add(fcpower_pane);
		layout_box.add(Box.createHorizontalGlue());
		layout_box.add(Box.createRigidArea(new Dimension(30, 0)));
		layout_box.add(shorePowerState);
		layout_box.add(Box.createRigidArea(new Dimension(30, 0)));
		//shorePowerState.setAlignmentX(Component.CENTER_ALIGNMENT);
		shorePowerState.addItemListener(this);

		content.add(layout_box);
		content.add(Box.createRigidArea(y_spacer_dim));




		try {
			towerSocket = new TCPCanSocket(Config.getString("tower.host"), Config.getInt("tower.port", TCPCanSocket.DEFAULT_PORT));
			Scheduler.addSchedulableAction("tower", new SocketAction(towerSocket, "tower"));
		} catch(ConnectException e) {
			e.printStackTrace();
		}

		Scheduler.addSchedulableAction("rocket", new SocketAction(rocketSocket, "rocket"));

		ended(); // reset the button and label
		sched.addScheduleListener(this, 100);

		setDefaultCloseOperation(EXIT_ON_CLOSE);
		pack();

		//System.out.println("setting window size to: " + windowSize);
		setSize(windowSize);

		show();
	}


	public void itemStateChanged(ItemEvent event)
	{
		Object source = event.getItemSelectable();
		if (source == shorePowerState)
		{
			short id = CanBusIDs.LTR_SET_SPOWER;
			int timestamp = 0;
			byte body[] = new byte[8];
			if (event.getStateChange() == ItemEvent.DESELECTED)
				body[0] = 0;
			else
				body[0] = 1;
			try {
				CanMessage myMessage = new CanMessage(id, timestamp, body);
				towerSocket.write(myMessage);
				towerSocket.flush();
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
	}

	public void actionPerformed(ActionEvent event)
	{
		try {
			if (event.getActionCommand().equals("preflight"))
			{
				//XXX: TODO: get base / origin gps and pressure values
				//from main.conf using Config.java
				double pressure = 101.5, temp = 20, altitude = 60;
				rocketSocket.write(new PressureBaseMessage(pressure,
							temp, altitude));

				double lat = 45.4704, lon = -122.6247, height = 60;
				rocketSocket.write(new GpsOriginMessage(
							lat, lon, height));
				
				byte[] data = { CanBusIDs.PreflightCheckState };
				rocketSocket.write(new CanMessage(CanBusIDs.FC_REQUEST_STATE, 0, data));
			}
			else if (event.getActionCommand().equals("arm"))
			{
				byte[] data = { CanBusIDs.ArmingState };
				rocketSocket.write(new CanMessage(CanBusIDs.FC_REQUEST_STATE, 0, data));
			}
			else if (event.getActionCommand().equals("powerdown"))
			{
				if(JOptionPane.showConfirmDialog(this,
					"Are you sure you want to shutdown the flight computer?", 
					"Proceed?",
					JOptionPane.YES_NO_OPTION) != JOptionPane.YES_OPTION) 
					return; // user wasn't sure.
				//byte[] data = { CanBusIDs.PowerDownState };
				byte[] data = new byte[8];
				data[0] = CanBusIDs.PowerDownState;
				rocketSocket.write(new CanMessage(CanBusIDs.FC_REQUEST_STATE, 0, data));
			} 
			else if(event.getActionCommand().equals("fc_on"))
			{
				int delay = 2000; /* delay between msgs, in milliseconds  */

				// cancel any prev. sequence
				if (powerSequence != null) {
					powerSequence.cancel();
				}

				// sched 5 things for new sequence
				int i = 1;
				powerSequence = new java.util.Timer(true /* daemon */);
				for (i = 1; i <= 5; ++i)
				{
					boolean power;
					if (i % 2 != 0) // i is odd
						power = true; // power ON at 1, 3, 5 secs
					 else  // i is even
						power = false;
					 powerSequence.schedule(
							 new ShorePowerTask(power), i * delay);
				}
			} else if (event.getActionCommand().equals("fc_off"))
			{
				int delay = 2000; // delay btween msgs, in milliseconds 
				
				if(JOptionPane.showConfirmDialog(this,
					"Are you sure you want to cut power to flight computer?", 
					"Proceed?",
					JOptionPane.YES_NO_OPTION) != JOptionPane.YES_OPTION) 
					return; // user wasn't sure.

				// cancel any prev. sequence
				if (powerSequence != null) {
					powerSequence.cancel();
				}

				// sched 5 things for new sequence
				powerSequence = new java.util.Timer(true /* daemon */);
				int i;
				for (i = 1; i <= 5; ++i)
				{
					boolean power;
					if (i % 2 != 0) // i is odd
						power = false; // power OFF at 1, 3, 5 secs
					 else  // i is even
						power = true;
					 powerSequence.schedule(
							 new ShorePowerTask(power), i * delay);
				}
			} else if(event.getActionCommand().equals("start"))
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
		SwingUtilities.invokeLater(new Runnable() {
			public void run()
			{
				countdownButton.setEnabled(false);
			}
		});
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
				countdownButton.setEnabled(true);
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
		new LaunchControl();
	}
}
