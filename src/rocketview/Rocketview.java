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
	protected static final Dimension preferredSize
	    = new Dimension( 1024, 750 );
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


		// top panel for status boxes
		JPanel top = new JPanel();
		top.setLayout(new BoxLayout(top, BoxLayout.Y_AXIS));
		leftCol.add( top );


		// time title is TC date/time at startup
		JPanel time = new JPanel();
		String startTitle = "rv start: " + startTime;
		addObserver( time, startTitle, new TimeObserver() );
		top.add( time );
	

		// flight computer state
		JPanel fcState = new JPanel();
		addObserver( fcState, "FC State", new RocketState() );
		top.add( fcState );


		// bottom panel for state info, messages, and later charts
		JPanel bottom = new JPanel();
		bottom.setLayout(new BoxLayout(bottom, BoxLayout.X_AXIS));
		leftCol.add(bottom);

		
		
		// message box for scrolled text, later add to split pane
		TextObserver messArea = new TextObserver();
		dispatch.addObserver( messArea );

		JScrollPane messScroll = new JScrollPane( messArea );
		messScroll.setBorder( new TitledBorder( "CanId  len  data" ));


		// subSys panel holds a labelled display for each subsystem
		//   vertical box layout
		JPanel subSys = new JPanel();
		subSys.setLayout(new BoxLayout(subSys, BoxLayout.Y_AXIS ));
		

		// inertial nav: not implemented
		/*
		JPanel ins = new JPanel();
		ins.setBorder(BorderFactory.createLineBorder( Color.gray ));
		ins.setLayout(new FlowLayout( FlowLayout.LEFT ));
		ins.add( new JLabel( "INS: -- no information --" ));
		subSys.add( ins );
		*/
		
		
		// height data from pressure and/or gps
		JPanel height = new JPanel();
		addUntitledObserver( height, new HeightObserver());
		subSys.add( height );

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

		
		//Split pane which holds subsystems info, message area, 
		//and (XXX:later, using nested splitpanes) stripcharts.
		JSplitPane splitPane = new JSplitPane(
				JSplitPane.HORIZONTAL_SPLIT,
				subSys, messScroll);
		splitPane.setDividerLocation(.25);
		splitPane.setAlignmentX(Component.LEFT_ALIGNMENT);
		splitPane.setBackground(Color.blue);
		bottom.add(splitPane);
		bottom.add(Box.createGlue());


		//look at min, max and preferred size for components.
		/*
		outputSizes(this, "RocketView");
		outputSizes(top, "top container");
		outputSizes(bottom, "bottom jsplitpane");
		outputSizes(subSys, "subsystems jpanel");
		outputSizes(messScroll, "message area scrollpanel");
		*/

		pack();
	}

	public void outputSizes(Component c, String name) {
		System.out.println(name + ":");
		System.out.println("\tMAX: " + c.getMaximumSize());
		System.out.println("\tMIN: " + c.getMinimumSize());
		System.out.println("\tPREF: " + c.getPreferredSize());
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

	// add title to JComponent (or Container if possible)
	// set left-align flow layout on Container with no vertical spacing
	// set preferred size as small as possible
	// add them as a Dispatch observer
	protected void addObserver(Container c, String title, JComponent o)
	{
		if (c instanceof JComponent) {
			JComponent jc = (JComponent) c;
			// setting the border around the container (e.g. jpanel) if possible  
			// seems to give the desired layout effect: border is drawn around entire
			// widget area, not just the space used (as was the case with jlabel)
			jc.setBorder(new TitledBorder(title));
			// try setting preferred size to as small as possible. 
			//int containerWidth = (int)jc.getPreferredSize().getWidth();
			//int compHeight = (int)o.getPreferredSize().getHeight();
			//jc.setPreferredSize( new Dimension(containerWidth, compHeight) );
		} else {
			o.setBorder(new TitledBorder(title));
		}
		c.setLayout(new FlowLayout( FlowLayout.LEFT, 5, 0 /* no vert spacing */));
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
