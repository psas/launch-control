import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;
import java.text.*;
import java.util.*;
import javax.swing.*;

public class LaunchControl extends JFrame
	implements ScheduleListener, ActionListener
{
	private final static String startMsg = "Start Countdown";
	private final static String stopMsg = "Abort Countdown";
	private final static String stoppedMsg = "Countdown Stopped";
	private final static DecimalFormat fmt = new DecimalFormat("T+0.0;T-0.0");

	private JButton button = new JButton();
	private JLabel clock = new JLabel();
	private Properties conf = new Properties();
	private Scheduler s;

	private LaunchControl() throws IOException
	{
		super("LaunchControl");
		conf.load(new FileInputStream("main.conf"));
		s = new Scheduler(new File(conf.getProperty("schedule")).toURL());
		s.addScheduleListener(this, 100);

		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e)
			{
				System.exit(0);
			}
		});

		ended(); // reset the button and label
		button.addActionListener(this);
		Container pane = getContentPane();
		pane.add(button, BorderLayout.WEST);
		pane.add(clock, BorderLayout.CENTER);
		pack();
	}

	public void actionPerformed(ActionEvent event)
	{
		try {
			if(event.getActionCommand().equals("start"))
				s.startCountdown();
			else if(event.getActionCommand().equals("abort"))
				s.abortCountdown();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	public void started()
	{
		SwingUtilities.invokeLater(new Runnable() {
			public void run()
			{
				button.setText(stopMsg);
				button.setActionCommand("abort");
			}
		});
		try {
			SoundAction.playSound(conf.getProperty("startSound"));
		} catch(Exception e) {
			// ignore
		}
	}

	public void aborted()
	{
		SwingUtilities.invokeLater(new Runnable() {
			public void run()
			{
				button.setEnabled(false);
			}
		});
		try {
			SoundAction.playSound(conf.getProperty("abortSound"));
		} catch(Exception e) {
			// ignore
		}
	}

	public void ended()
	{
		SwingUtilities.invokeLater(new Runnable() {
			public void run()
			{
				button.setText(startMsg);
				button.setActionCommand("start");
				button.setEnabled(true);
				clock.setText(stoppedMsg);
			}
		});
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

	public static void main(String args[]) throws IOException
	{
		new LaunchControl().setVisible(true);
	}
}
