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
	protected final static String stopMsg = "Abort Countdown";
	protected final static String stoppedMsg = "Countdown stopped";
	protected final DecimalFormat fmt = new DecimalFormat("T+0.0;T-0.0");

	protected JButton countdownButton = new JButton();
	protected JCheckBox shorePowerState = new JCheckBox("Shore");
	protected JLabel clock = new JLabel();
	protected JLabel statusLabel = new JLabel();
	protected String startSound = Config.getString("startSound");
	protected String abortSound = Config.getString("abortSound");
	protected TCPCanSocket towerSocket;

	protected final Scheduler sched = new Scheduler();

	private LaunchControl() throws IOException
	{
		super("LaunchControl");
		Container content = getContentPane();
		content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));

		JPanel time = new JPanel();
		time.setLayout(new BorderLayout());
		time.add(countdownButton, BorderLayout.WEST);
		time.add(shorePowerState, BorderLayout.EAST);
		time.add(clock, BorderLayout.CENTER);
		content.add(time);

		Container statusBar = new JPanel();
		statusBar.setLayout(new BorderLayout());
		statusLabel.setBorder(BorderFactory.createLoweredBevelBorder());
		statusBar.add(statusLabel, BorderLayout.CENTER);
		content.add(statusBar);

		UDPCanSocket rocketSocket = new UDPCanSocket(Config.getString("rocket.host"), Config.getInt("rocket.port", UDPCanSocket.PORT_SEND));

		// also pass the rocket socket to a thread for listening
		content.add(new RocketPanel(rocketSocket, this));
		
		try {
			towerSocket = new TCPCanSocket(Config.getString("tower.host"), Config.getInt("tower.port", TCPCanSocket.DEFAULT_PORT));
			Scheduler.addSchedulableAction("tower", new SocketAction(towerSocket, "tower"));
		} catch(ConnectException e) {
			e.printStackTrace();
		}
		Scheduler.addSchedulableAction("rocket", new SocketAction(rocketSocket, "rocket"));

		ended(); // reset the button and label
		sched.addScheduleListener(this, 100);
		countdownButton.addActionListener(this);
		//shorePowerState.setActionCommand("shore");
		//shorePowerState.addActionListener(this);
		shorePowerState.addItemListener(this);

		setDefaultCloseOperation(EXIT_ON_CLOSE);

		pack();
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
			if(event.getActionCommand().equals("start"))
			{
				if(JOptionPane.showConfirmDialog(this,
					"Are you sure you want to start the countdown?", "Proceed?",
					JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION)
					sched.startCountdown();
			}
			else if(event.getActionCommand().equals("abort"))
			{
				sched.abortCountdown();
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
