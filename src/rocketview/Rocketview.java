package rocketview;

import cansocket.*;
import widgets.*;

import java.awt.*;
import java.util.*;
import java.net.*;
import java.text.DateFormat;

import javax.swing.*;
import javax.swing.border.*;

/**
 * Main window for PSAS telemetry viewer.
 */
public class Rocketview extends JFrame
{
	protected final CanListener dispatch;

	public static void main(String[] args) throws Exception
	{
		System.out.println( "Rocketview UDP" );
		System.out.flush();

		//int port = 4446;
		int port = UDPCanSocket.PORT_RECV;
		if (args.length > 0)
			port = Integer.parseInt(args[0]);

		Rocketview f = new Rocketview(InetAddress.getLocalHost().toString(), port);
		f.setDefaultCloseOperation(EXIT_ON_CLOSE);
		f.setVisible(true);

		f.dispatch.run();

		System.out.println( "Rocketview exits main()" );
		System.exit(0);
	} // end main()

	// construct a Rocketview
	// set up all panels, layout managers, and titles
	public Rocketview(String host, int port) throws Exception
	{
		super("Rocketview: " + host + ": " + port);

		dispatch = new CanListener(new LogCanSocket(new UDPCanSocket(port), "RocketView.log"));

		// status boxes
		JPanel fc = new JPanel();
		fc.setLayout(new GridBoxLayout());

		// flight computer state
		addObserver(fc, new RocketState());

		// message box for scrolled text, later add to split pane
		TextObserver messArea = new TextObserver();
		dispatch.addObserver( messArea );

		JScrollPane messScroll = new JScrollPane( messArea );
		messScroll.setBorder( new TitledBorder( "CanId  len  data" ));


		// subSys panel holds a labelled display for each subsystem
		//   vertical box layout
		JPanel subSys = new JPanel();
		subSys.setLayout(new GridLayout(1, 0));

		addObserver(subSys, "GPS", new GPSObserver());
		addObserver(subSys, "APS", new APSObserver());

		// rvPane is the outermost content pane
		Container rvPane = getContentPane();
		rvPane.setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();

		gbc.weighty = 1.0;
		gbc.fill = gbc.VERTICAL;
		rvPane.add(fc, gbc);

		gbc.weightx = 2.0;
		gbc.fill = gbc.BOTH;
		rvPane.add(messScroll, gbc);

		gbc.weighty = 0.0;
		gbc.gridy = 1;
		gbc.fill = gbc.HORIZONTAL;
		gbc.gridwidth = gbc.RELATIVE;
		rvPane.add(subSys, gbc);

		IMUObserver imu = new IMUObserver();
		dispatch.addObserver(imu);
		gbc.fill = gbc.BOTH;
		gbc.gridy = 0;
		gbc.gridwidth = gbc.REMAINDER;
		gbc.gridheight = gbc.REMAINDER;
		rvPane.add(imu, gbc);

		pack();
	}

	public void outputSizes(Component c, String name) {
		System.out.println(name + ":");
		System.out.println("\tMAX: " + c.getMaximumSize());
		System.out.println("\tMIN: " + c.getMinimumSize());
		System.out.println("\tPREF: " + c.getPreferredSize());
	}

	// add title to JComponent (or Container if possible)
	// set left-align flow layout on Container with no vertical spacing
	// set preferred size as small as possible
	// add them as a Dispatch observer
	protected void addObserver(Container c, String title, JComponent o)
	{
		o.setBorder(new TitledBorder(title));
		addObserver(c, o);
	}

	// add Component to Container
	// add Component as an Observer of Dispatch
	protected void addObserver(Container c, Component o)
	{
		c.add(o);
		dispatch.addObserver((Observer) o);
	}

} // end class Rocketview
