import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;
import java.util.*;
import javax.swing.*;

public class LaunchControl extends JFrame
{
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
		Scheduler.addSchedulableAction("tower", (SchedulableAction)tower);

		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e)
			{
				System.exit(0);
			}
		});

		pack();
	}

	public static void main(String args[]) throws IOException
	{
		new LaunchControl().setVisible(true);
	}
}
