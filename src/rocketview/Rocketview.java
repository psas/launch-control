package rocketview;

import cansocket.*;
import launchcontrol.*;
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
	public static void main(String[] args) throws Exception
	{
		CanDispatch dispatch = new CanDispatch();
		Rocketview f = new Rocketview(dispatch, args.length > 0);
		f.setDefaultCloseOperation(EXIT_ON_CLOSE);
		f.pack();
		f.setVisible(true);
		dispatch.run();
	} // end main()

	// construct a Rocketview
	// set up all panels, layout managers, and titles
	public Rocketview(CanDispatch dispatch, boolean showLaunchControl) throws Exception
	{
		super("Rocketview");

		// status boxes
		JPanel fc = new JPanel();
		fc.setLayout(new GridBoxLayout());

		// flight computer state
		FCStateLabel stateLabel = new FCStateLabel(dispatch);
		fc.add(stateLabel);
		fc.add(new StateGrid(dispatch));

		// message box for scrolled text
		TextObserver messScroll = new TextObserver(dispatch);
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

		gbc.gridy = 2;
		if(showLaunchControl)
		{
			LaunchControl control = new LaunchControl(dispatch);
			rvPane.add(control, gbc);
			stateLabel.addLinkStateListener(control);
		}
		else
			dispatch.setSocket(new LogCanSocket(new UDPCanSocket(), "RocketView.log"));

		IMUObserver imu = new IMUObserver(dispatch);
		gbc.fill = gbc.BOTH;
		gbc.gridy = 0;
		gbc.gridwidth = gbc.REMAINDER;
		gbc.gridheight = gbc.REMAINDER;
		rvPane.add(imu, gbc);
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
