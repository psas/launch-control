package rocketview;

import cansocket.*;

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
	protected static final Dimension preferredSize
	    = new Dimension( 1024, 750 );
	// preferredSize = new Dimension(750, 550);
	// preferredSize = new Dimension(1024, 768);
	protected final Dispatch dispatch;

	public static void main(String[] args) throws Exception
	{
		System.out.println( "Rocketview UDP" );
		System.out.flush();

		int port = UDPCanSocket.PORT_RECV;
		if (args.length > 0)
			port = Integer.parseInt(args[0]);

		Rocketview f = new Rocketview( InetAddress.getLocalHost() + ":" + port);
		f.setDefaultCloseOperation(EXIT_ON_CLOSE);
		f.setVisible(true);

		// f.dispatch.run(new LogCanSocket(new TCPCanSocket(host),host));
		// f.dispatch.run(new LogCanSocket(new TCPCanSocket(), host));
		f.dispatch.run(new UDPCanSocket(port));

		System.out.println( "Rocketview exits main()" );
		System.exit(0);
	} // end main()

	// construct a Rocketview
	// set up all panels, layout managers, and titles
	public Rocketview(String host) throws Exception
	{
		super("Rocketview: " + host);

		dispatch = new Dispatch();

		// format a start-time string
		DateFormat df
		    = DateFormat.getDateTimeInstance(
		    DateFormat.SHORT, DateFormat.SHORT );
		String startTime = df.format (new Date ());

		// rvPane is the outermost content pane
		Container rvPane = getContentPane();
		rvPane.setLayout(new GridLayout(1, 0)); // just 1 row

		// left side for state, message, subSys to share
		JPanel leftCol = new JPanel();
		leftCol.setLayout( new BoxLayout( leftCol, BoxLayout.Y_AXIS ));
		rvPane.add( leftCol );

		// right side for stripcharts
		addObserver( rvPane, new IMUObserver());


		// top panel for status boxes
		JPanel top = new JPanel();
		leftCol.add( top );

		// time title is TC date/time at startup
		JPanel time = new JPanel();
		String startTitle = "rv start: " + startTime;
		addObserver( time, startTitle, new TimeObserver() );
		top.add( time );

		// flight computer state
		JPanel fcState = new JPanel();
		addObserver( fcState, "FC State", new FcStateObserver() );
		top.add( fcState );


		// message box for scrolled text
		TextObserver messArea = new TextObserver();
		dispatch.addObserver( messArea );

		JScrollPane messScroll = new JScrollPane( messArea );
		messScroll.setBorder( new TitledBorder( "CanId  len  data" ));
		leftCol.add( messScroll );


		// subSys panel holds a labelled display for each subsystem
		//   vertical box layout
		JPanel subSys = new JPanel();
		subSys.setLayout(new BoxLayout(subSys, BoxLayout.Y_AXIS ));
		leftCol.add( subSys );

		// inertial nav: not implemented
		JPanel ins = new JPanel();
		ins.setBorder(BorderFactory.createLineBorder( Color.gray ));
		ins.setLayout(new FlowLayout( FlowLayout.LEFT ));
		ins.add( new JLabel( "INS: -- no information --" ));
		subSys.add( ins );

		// 2 GPS observers go in 1 panel
		JPanel gps = new JPanel();
		addUntitledObserver( gps, new GPSPositionObserver() );
		addObserver( gps, new GPSObserver() );
		subSys.add( gps );

		// APS panel
		JPanel aps = new JPanel();
		addUntitledObserver( aps, new APSObserver() );
		// addObserver( aps, "APS", new APSObserver() );
		subSys.add( aps );

		pack();
	}

	public Dimension getPreferredSize()
	{
		return preferredSize;
	}

	// add border to first JComponent
	// set left-align flow layout on first JComponent
	// add them as a Dispatch observer
	protected void addUntitledObserver(JComponent c, JComponent o)
	{
		c.setBorder(BorderFactory.createLineBorder( Color.gray ));
		c.setLayout(new FlowLayout( FlowLayout.LEFT ));

		addObserver(c, o);
	}

	// add title to JComponent
	// set left-align flow layout on Container
	// add them as a Dispatch observer
	protected void addObserver(Container c, String title, JComponent o)
	{
		o.setBorder(new TitledBorder(title));
		c.setLayout(new FlowLayout( FlowLayout.LEFT ));
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
