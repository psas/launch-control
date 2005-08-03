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
	protected static final boolean showStripCharts = true;

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

		// flight computer state
		FCStateLabel stateLabel = new FCStateLabel(dispatch);
		StateGrid.setDispatcher(dispatch);
		
		// message box for scrolled text
		TextObserver messScroll = new TextObserver(dispatch);
		messScroll.setBorder( new TitledBorder( "CanId  len  data" ));

		// subSys panels hold a labeled display for each subsystem
		//   vertical box layout
		JPanel subSys1 = new JPanel();
		subSys1.setLayout(new GridBoxLayout());
		subSys1.add(new GPSObserver(dispatch));
		subSys1.add(new ATVObserver(dispatch));
		
		JPanel subSys2 = new JPanel();				
		subSys2.setLayout(new GridBoxLayout());
		subSys2.add(new APSObserver(dispatch));
		subSys2.add(new IMUStateObserver(dispatch));	
		
		JPanel subSys3 = new JPanel();
		subSys3.setLayout(new GridBoxLayout());
		subSys3.add(new RecObserver(dispatch));
		subSys3.add(new OtherObserver(dispatch, stateLabel));
		
		// rvPane is the outermost content pane
		Container rvPane = getContentPane();
		rvPane.setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();

		gbc.fill = gbc.BOTH;
		gbc.weighty = 1.0;
		gbc.weightx = 1.0;
		rvPane.add(messScroll, gbc);
		gbc.weightx = 0.0;
		rvPane.add(subSys1, gbc);
		rvPane.add(subSys2, gbc);
		rvPane.add(subSys3, gbc);
		
		gbc.gridy = 2;
		gbc.weighty = 0.0;
		gbc.gridwidth = 4;
		if(showLaunchControl)
		{
			LaunchControl control = new LaunchControl(dispatch);
			rvPane.add(control, gbc);
			stateLabel.addLinkStateListener(control);
		}
		else
			dispatch.setSocket(new LogCanSocket(new UDPCanSocket(), "RocketView.log"));

		if(showStripCharts)
		{
			gbc.gridx = 4;
			gbc.gridy = 0;
			gbc.weightx = 1.0;
			gbc.weighty = 1.0;
			gbc.gridwidth = gbc.REMAINDER;
			gbc.gridheight = gbc.REMAINDER;
			rvPane.add(new IMUObserver(dispatch), gbc);
		}
	}

	public void outputSizes(Component c, String name) {
		System.out.println(name + ":");
		System.out.println("\tMAX: " + c.getMaximumSize());
		System.out.println("\tMIN: " + c.getMinimumSize());
		System.out.println("\tPREF: " + c.getPreferredSize());
	}
} // end class Rocketview
