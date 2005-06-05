package widgets;

import java.awt.GridLayout;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.ImageIcon;
import javax.swing.SwingConstants;

/** A grid of leds displaying info about different elements, 
 * which are nodes in our case, and each of which is labeled. */
public class StateGrid extends JPanel
{
	// node states stored as bits
	protected byte[] states;
	protected byte[] mask;
	
	protected static String[] modes = {
			"APS mode",
			"IMU mode",
			"GPS mode",
			"ATV mode",
			"REC mode"
	};
	protected static String[] aps = {
			"APS_SWITCH_2", /* CAN bus */
			"APS_SWITCH_3", /* ATV power amp (and DC/DC converter) */
			"APS_SWITCH_4", /* WIFI power amp */
			"APS_VOLTS",
			"APS_AMPS",
			"APS_CHARGE"
	};
	protected static String[] umb = {
			"UMB_ROCKETREADY",
			"UMB_CONNECTOR",
	};
	protected static String[] gps = {
			"GPS_POWER",
			"GPS_UART_TRANSMIT"
	};
	protected static String[] atv = {
			"ATV_POWER_CAMERA",
			"ATV_POWER_OVERLAY",
			"ATV_POWER_TX",
			"ATV_POWER_PA"
	};
	protected static String[] imu = {
			"IMU_ACCEL",
			"IMU_GYRO",
			"IMU_PRESSURE",
			"IMU_TEMPERATURE"
	};
	protected static String[] shared = {
			"ZERO_IMU_GYRO",
			"PRESSURE_VALID",
			"GPS_LOCKED",
			"SAFE_DESCENT_GPS",
			"DROGUE_DEPLOY_SAFE_GPS",
			"SAFE_DESCENT_PRESSURE",
			"DROGUE_DEPLOY_SAFE_PRESSURE",
			"HEIGHT_MATCH_GPS_PRESSURE"
	};
	protected static String[] preflight = {
			"SANE_ANTENNAS",
			"SANE_IMU_ACCEL",
			"QUIET_PRESSURE_BASE",
			"GOT_GPS",
			"SANE_GPS"
	};
	/* Arming tests. */
	protected static String[] arming = {
		"ARMING_IMU_FAST"
	};
	protected static String[] boost = {
	/* Boost tests */
		"BOOST_GPS",
		"BOOST_IMU",
		"BOOST_UMB",
		"BOOST_PRESSURE"
	};
	/* Coast tests */
	protected static String[] coast = {
		"APOGEE_PRESSURE"
	};
	/* Deploy drogue tests */
	protected static String[] deploy = {
		"DROGUE_PRESSURE",
		"DROGUE_GPS",
		"DROGUE_IMU",
		"DROGUE_WORKING"
	};
	/* Descent drogue tests */
	protected static String[] descent = {
		"DESCEND_GPS",
		"DESCEND_PRESSURE",
		"DESCEND_MAIN_FUTURE"
	};
	/* Descend main tests */
	protected static String[] touchdown = {
		"TOUCHDOWN_GPS",
		"TOUCHDOWN_PRESSURE"
	};
	/* Recovery wait tests */
	protected static String[] recovery = {
		"RECOVERY_VOLTS"
	};
	
	protected static String [][] groups = {
		modes, aps, umb, gps, atv, imu, shared, preflight, arming, boost,
		coast, deploy, descent, touchdown, recovery
	};		
	
	protected static final int numGroups = groups.length;
	protected static final int numStates =
		modes.length + aps.length + umb.length + gps.length + atv.length +
		imu.length + shared.length + preflight.length + arming.length +
		boost.length + coast.length + deploy.length + descent.length +
		touchdown.length;
	

	// names of node states
//	protected static final String[] names = {
//		"APS mode",
//		"IMU mode",
//		"GPS mode",
//		"ATV mode",
//		"REC mode",
//
//		"APS_SWITCH_2", /* CAN bus */
//		"APS_SWITCH_3", /* ATV power amp (and DC/DC converter) */
//		"APS_SWITCH_4", /* WIFI power amp */
//		"UMB_ROCKETREADY",
//		"UMB_CONNECTOR",
//
//		"GPS_POWER",
//
//		"ATV_POWER_CAMERA",
//		"ATV_POWER_OVERLAY",
//		"ATV_POWER_TX",
//		"ATV_POWER_PA",
//
//		"APS_VOLTS",
//		"APS_AMPS",
//		"APS_CHARGE",
//
//		"IMU_ACCEL",
//		"IMU_GYRO",
//		"IMU_PRESSURE",
//		"IMU_TEMPERATURE",
//
//		"GPS_UART_TRANSMIT",
//
//		/* enum sequencer_test */
//		/* Shared tests */
//		"ZERO_IMU_GYRO",
//		"PRESSURE_VALID",
//		"GPS_LOCKED",
//		"SAFE_DESCENT_GPS",
//		"DROGUE_DEPLOY_SAFE_GPS",
//		"SAFE_DESCENT_PRESSURE",
//		"DROGUE_DEPLOY_SAFE_PRESSURE",
//		"HEIGHT_MATCH_GPS_PRESSURE",
//	
//		/* Preflight check tests. */
//		"SANE_ANTENNAS",
//		"SANE_IMU_ACCEL",
//		"QUIET_PRESSURE_BASE",
//		"GOT_GPS",
//		"SANE_GPS",
//	
//		/* Arming tests. */
//		"ARMING_IMU_FAST",
//	
//		/* Boost tests */
//		"BOOST_GPS",
//		"BOOST_IMU",
//		"BOOST_UMB",
//		"BOOST_PRESSURE",
//	
//		/* Coast tests */
//		"APOGEE_PRESSURE",
//	
//		/* Deploy drogue tests */
//		"DROGUE_PRESSURE",
//		"DROGUE_GPS",
//		"DROGUE_IMU",
//		"DROGUE_WORKING",
//	
//		/* Descent drogue tests */
//		"DESCEND_GPS",
//		"DESCEND_PRESSURE",
//		"DESCEND_MAIN_FUTURE",
//	
//		/* Descend main tests */
//		"TOUCHDOWN_GPS",
//		"TOUCHDOWN_PRESSURE",
//	
//		/* Recovery wait tests */
//		"RECOVERY_VOLTS",
//	};

	protected ImageIcon greenled = new ImageIcon(ClassLoader.getSystemResource("widgets/greenled.png"));
	protected ImageIcon redled = new ImageIcon(ClassLoader.getSystemResource("widgets/redled.png"));
	protected ImageIcon grayled = new ImageIcon(ClassLoader.getSystemResource("widgets/grayled.png"));

	public StateGrid() {
		setLayout(new GridLayout(0,4)); // four rows

		// initialize state array to 0 
		states = new byte[8]; 
		mask = new byte[8]; 
		/*
		for (int i = 0; i < 8; ++i) {
			states[i] = 0;
			// IDEA: we want to have things be greyed by default,
			// well we have 8 bytes and we only use like 4 so
			// maybe we could store 32 bits for whether things
			// are interesting or not.
		} */

		// create GUI
		draw();
	}



	/** Set state byte array and update LEDs.
	 * @param newStates 8 byte data from FC_REPORT_NODE_STATUS
	 * messages. */
	public void setStates(byte[] newStates)
	{
		redraw(states, newStates, mask, mask);
		states = newStates;
	}

	/**
	 * Set importance mask byte array and update LEDs.
	 * @param newMask 8 byte importance mask from FC_REPORT_IMPORTANCE_MASK
	 */
	public void setMask(byte[] newMask)
	{
		redraw(states, states, mask, newMask);
		mask = newMask;
	}

	/** Return specified bit of data. */
	protected boolean getBit(byte[] data, int bit) 
	{
		return (data[bit / 8] & (1 << (bit % 8))) != 0;
	}



	/** Set the icon for the given element.
	 *
	 * Uninteresting        -> gray LED
	 * Interesting and bad  -> red LED
	 * Interesting and good -> green LED
	 * 
	 * @param element: the element number n for the component,
	 * note the the component must have been the n'th component
	 * added to this StateGrid. 
	 * @param isGood: if the element is in a good state
	 * @param isInteresting: if the element is interesting (being checked
	 * by the flight computer).
	 *
	 */
	protected void setElementIcon(int element, boolean isGood,
                                      boolean isInteresting)
	{
		JLabel gridElement = (JLabel) getComponents()[element];
		setElementIcon(gridElement, isGood, isInteresting);
	}

	protected void setElementIcon(JLabel element, boolean isGood,
                                      boolean isInteresting)
	{
		if (!isInteresting)
			element.setIcon(grayled);
		else if (isGood) 
			element.setIcon(greenled);
		else 
			element.setIcon(redled);
	}

	
	
	
	// how to update?  Options:
	// 1) clear grid of state leds/names,
	// and recreate all of them.
	protected void draw() 
	{
		removeAll();
		int i,j = 0;
		for ( i = 0; i < groups.length; ++i) {
			for ( j = 0; j < groups[i].length; ++j) {
				JLabel gridEntry = new JLabel(groups[i][j]);
	                        gridEntry.setVerticalTextPosition(
	                            SwingConstants.CENTER);
	                        gridEntry.setHorizontalAlignment(SwingConstants.LEFT);
				gridEntry.setIcon(grayled);
				add(gridEntry);
				//setElementIcon(gridEntry, 0);
			}
		}
	}
	


	// 2) assuming we can access element X of the
	// grid, we only change an element X of the grid 
	// if bit X of the state has changed.
	protected void redraw(byte[] oldStates, byte[] newStates,
	                      byte[] oldMask, byte[] newMask)
	{
		//compare newStates to oldStates and newMask to oldMask
		//and only change elements which have changed
		int i,j = 0;
		for ( i = 0; i < groups.length; ++i) {
			for ( j = 0; j < groups[i].length; ++j) {
				boolean newBit = getBit(newStates, i);
				boolean newInteresting = getBit(newMask, i);
				if (getBit(oldStates, i) != newBit
				    || getBit(oldMask, i) != newInteresting)
					setElementIcon(i, newBit == true,
	                                               newInteresting == true);
			}
		}
	}
}
