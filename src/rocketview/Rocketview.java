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
		// dispatch.run(new TCPCanSocket(host));
		// System.exit(0);
	}

	public Rocketview(String host) throws Exception
	{
		super("Rocketview: " + host);

		dispatch = new Dispatch();
		getContentPane().setLayout(new BorderLayout());

		Box b;

		b = Box.createHorizontalBox();
		getContentPane().add(b, BorderLayout.CENTER);

		addObserver(b, new MessageObserver());
		addObserver(b, new IMUObserver());

		Box bottom = Box.createVerticalBox();
		getContentPane().add(bottom, BorderLayout.SOUTH);

		b = Box.createHorizontalBox();
		bottom.add(b);

		addObserver(b, "time", new TimeObserver());
		addObserver(b, "status", new StatusObserver());
		addObserver(b, "position", new PositionObserver());

		b = Box.createHorizontalBox();
		bottom.add(b);

		addObserver(b, "height", new HeightObserver());
		addObserver(b, "gps", new GPSObserver());

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
