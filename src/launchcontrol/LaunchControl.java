package launchcontrol;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;
import java.util.*;
import javax.swing.*;

public class LaunchControl extends JFrame
{
	private static JLabel statusLabel = new JLabel("nothing to see here");

	private LaunchControl() throws IOException
	{
		super("LaunchControl");
		Container content = getContentPane();
		content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));

		Properties conf = new Properties();
		conf.load(new FileInputStream("main.conf"));
		Scheduler s = new Scheduler(new File(conf.getProperty("schedule")).toURL());
		content.add(s.getControls(
			conf.getProperty("startSound"),
			conf.getProperty("abortSound")
		));

		TowerAction tower = new TowerAction(
			conf.getProperty("towerHost"),
			Integer.parseInt(conf.getProperty("towerPort"))
		);
		content.add(tower.getControls());
		Scheduler.addSchedulableAction("tower", tower);

		RocketAction rocket = new RocketAction(
		        conf.getProperty("rocketHost")
		);
		Scheduler.addSchedulableAction("rocket", rocket);

		Container statusBar = new JPanel();
		statusBar.setLayout(new BorderLayout());
		statusLabel.setBorder(BorderFactory.createLoweredBevelBorder());
		statusBar.add(statusLabel, BorderLayout.CENTER);
		content.add(statusBar);

		setDefaultCloseOperation(EXIT_ON_CLOSE);

		pack();
	}

	public static void setStatus(final String msg)
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
		new LaunchControl().setVisible(true);
	}
}
