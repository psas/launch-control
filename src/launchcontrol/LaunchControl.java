/* Copyright 2005 Ian Osgood, Jamey Sharp, Karl Hallowell, Peter Welte,
 *                Tim Welch
 * 
 * This program is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by the Free
 * Software Foundation; either version 2 of the License, or (at your option)
 * any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for
 * more details.
 *
 * Portland State Aerospace Society (PSAS) is a student branch chapter of the
 * Institute of Electrical and Electronics Engineers Aerospace and Electronics
 * Systems Society. You can reach PSAS at info@psas.pdx.edu.  See also
 * http://psas.pdx.edu/
 */
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
	implements ScheduleListener, ActionListener
{
	// Member variables, presumably used by multiple methods
	protected final DecimalFormat fmt = new DecimalFormat("T+0.0;T-0.0");

	protected TCPCanSocket towerSocket;
	protected Thread reader;

	protected final Scheduler sched = new Scheduler();

	protected JLabel statusLabel;
	protected JLabel clock;
	protected JButton countdownButton;
	protected JButton abortButton;

	protected java.util.List<RelayCheckBox> relays = new ArrayList<RelayCheckBox>();

	/** Create LaunchControl GUI, open connections, and start scheduler */
	public LaunchControl() throws IOException
	{
		//possible TODO: use gridlayout (or gridbaglayout) instead
		//of using boxes of boxes. The advantage of a gridlayout is
		// all components could be same size, and spaces can be inserted
		// easily and uniformly.

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
		
		// dispatch for tower status
		CanDispatch towerStatus = new CanDispatch(towerSocket);
		reader = new Thread(towerStatus);
		reader.start();

		// add top components
		topLCPanel.add(clock);
		topLCPanel.add(statusLabel);
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
		RelayCheckBox relay = new RelayCheckBox(towerSocket, towerStatus, "Shore", CanBusIDs.LTR_SPOWER);
		relays.add(relay);
		overridePanel.add(relay);
		relay = new RelayCheckBox(towerSocket, towerStatus, "Strobe", CanBusIDs.LTR_STROBE);
		relays.add(relay);
		overridePanel.add(relay);
		relay = new RelayCheckBox(towerSocket, towerStatus, "Siren", CanBusIDs.LTR_SIREN);
		relays.add(relay);
		overridePanel.add(relay);
		
		// There is no use showing the ignition relay for two reasons:
		// 1. the SET action requires an extra three bytes (see sched.conf)
		// 2. the firmware has a bug so that REPORT_IGNITION actually shows the shore power status!

		bottomLCPanel.add(overridePanel, gbc);

		// setup countdown components
		countdownButton = new JButton();
		countdownButton.setActionCommand("start");
		countdownPanel.add(countdownButton);

		// add countdown components
		bottomLCPanel.add(countdownPanel, gbc);

		// setup/add abort button
		abortButton = new JButton(new AbstractAction("ABORT") {
			public void actionPerformed(ActionEvent evt)
			{
				sched.abortCountdown();
			}
		});
		abortButton.setBackground(Color.red);
		bottomLCPanel.add(abortButton, gbc);
		
		ended(); // reset the button and label
		sched.addScheduleListener(this, 100);
	}
	
	private void pollStatus()
	{
		try { Thread.sleep(500); } catch (InterruptedException e) { }
		for (RelayCheckBox relay : relays)
			relay.requestState();
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
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	public void started()
	{
		SwingUtilities.invokeLater(new Runnable() {
			public void run()
			{
				countdownButton.setText(abortButton.getText());
				countdownButton.removeActionListener(LaunchControl.this);
				countdownButton.addActionListener(abortButton.getAction());
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
				countdownButton.removeActionListener(abortButton.getAction());
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

	// LaunchControl-only frame for post-LV2 era
	public static void main(String[] args) throws Exception
	{
		JFrame f = new JFrame("Launch Control");
		LaunchControl lc = new LaunchControl();
		f.add(lc);
                f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                f.pack();
                f.setVisible(true);
                
                // poll for status
                while (true)
	                lc.pollStatus();
	}
}
