package rocketview;

import cansocket.*;

import java.awt.*;
import java.util.*;
import javax.swing.*;
import javax.swing.border.*;

/**
 * Main window for PSAS telemetry viewer.
 */
public class Rocketview extends JFrame
{
	protected static final Dimension preferredSize = new Dimension(750, 550);
	protected final Dispatch dispatch;

	public static void main(String[] args) throws Exception
	{
		String host = args.length > 0 ? args[0] : "localhost";
		Rocketview f = new Rocketview(host);
		f.setDefaultCloseOperation(EXIT_ON_CLOSE);
		f.setVisible(true);
		f.dispatch.run(new LogCanSocket(new TCPCanSocket(host), host));
		System.exit(0);
	}

	public Rocketview(String host) throws Exception
	{
		super("Rocketview: " + host);

		dispatch = new Dispatch();
		getContentPane().setLayout(new BorderLayout());

		addObserver(getContentPane(), new IMUObserver());

		JPanel bottom = new JPanel();
		bottom.setLayout(new BorderLayout());
		getContentPane().add(bottom, BorderLayout.SOUTH);

		JPanel labels;
		
		labels = new JPanel();
		labels.setLayout(new GridLayout(1, 0));
		bottom.add(labels, BorderLayout.NORTH);

		addObserver(labels, "time", new TimeObserver());
		addObserver(labels, "position", new PositionObserver());

		labels = new JPanel();
		labels.setLayout(new GridLayout(1, 0));
		bottom.add(labels, BorderLayout.SOUTH);

		addObserver(labels, "height", new HeightObserver());
		addObserver(labels, "gps", new GPSObserver());

		pack();
	}

	public Dimension getPreferredSize()
	{
		return preferredSize;
	}

	protected void addObserver(Container c, String title, JComponent o)
	{
		o.setBorder(new TitledBorder(title));
		addObserver(c, o);
	}

	protected void addObserver(Container c, Component o)
	{
		c.add(o);
		dispatch.addObserver((Observer) o);
	}
}
