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
	protected final CanDispatch dispatch;

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

		dispatch = new CanDispatch(new LogCanSocket(new UDPCanSocket(port), "RocketView.log"));

		// status boxes
		JPanel fc = new JPanel();
		fc.setLayout(new GridBoxLayout());

		// flight computer state
		RocketState statepanel = new RocketState();
		dispatch.add(statepanel);
		addObserver(fc, statepanel);

		// message box for scrolled text
		JScrollPane messScroll = new JScrollPane(new TextObserver(dispatch));
		messScroll.setBorder( new TitledBorder( "CanId  len  data" ));


		// subSys panel holds a labelled display for each subsystem
		//   vertical box layout
		JPanel subSys = new JPanel();
		subSys.setLayout(new GridLayout(1, 0));

		addObserver(subSys, "GPS", new GPSObserver(dispatch));
		addObserver(subSys, "APS", new APSObserver(dispatch));

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

		IMUObserver imu = new IMUObserver(dispatch);
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

	protected void addObserver(Container c, Component o)
	{
		c.add(o);
	}
} // end class Rocketview
