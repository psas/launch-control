package widgets;

import java.awt.GridLayout;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.ImageIcon;

/** A grid of leds displaying info about different elements, 
 * which are nodes in our case, and each of which is labeled. */
public class StateGrid extends JPanel
{
	// node states stored as bits
	protected byte[] states;
	protected byte[] mask;

	// names of node states
	protected static final String[] names = {
		"APS mode",
		"IMU mode",
		"GPS mode",
		"ATV mode",
		"REC mode",

		"APS_SWITCH_2", /* CAN bus */
		"APS_SWITCH_3", /* ATV power amp (and DC/DC converter) */
		"APS_SWITCH_4", /* WIFI power amp */
		"UMB_ROCKETREADY",
		"UMB_CONNECTOR",

		"GPS_POWER",

		"ATV_POWER_CAMERA",
		"ATV_POWER_OVERLAY",
		"ATV_POWER_TX",
		"ATV_POWER_PA",

		"APS_VOLTS",
		"APS_AMPS",
		"APS_CHARGE",

		"IMU_ACCEL",
		"IMU_GYRO",
		"IMU_PRESSURE",
		"IMU_TEMPERATURE",

		"GPS_UART_TRANSMIT",

		/* enum sequencer_test */
		"SANE_ANTENNAS",
		"SANE_IMU_ACCEL",
		"QUIET_PRESSURE_BASE",
		"GOT_GPS",
		"GPS_LOCKED",
		"SANE_HEIGHT",
		"SANE_GPS",
		"BOOST_GPS",
		"BOOST_IMU",
		"BOOST_UMB",
		"BOOST_PRESSURE",
		"PRESSURE_CONSTANT",
		"APOGEE_Z",
		"APOGEE_PRESSURE",
		"TOUCHDOWN_GPS",
		"TOUCHDOWN_PRESSURE",
		"ZERO_IMU_GYRO",
	};

	protected ImageIcon greenled = new ImageIcon(ClassLoader.getSystemResource("widgets/greenled.png"));
	protected ImageIcon redled = new ImageIcon(ClassLoader.getSystemResource("widgets/redled.png"));
	protected ImageIcon grayled = new ImageIcon(ClassLoader.getSystemResource("widgets/grayled.png"));

	public StateGrid() {
		setLayout(new GridLayout(0,4)); // four rows

		// initialize state array to 0 
		states = new byte[8]; 
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
		for (int i = 0; i < names.length; ++i) {
			JLabel gridEntry = new JLabel(names[i]);
			gridEntry.setIcon(grayled);
			add(gridEntry);
			//setElementIcon(gridEntry, 0);
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
		for (int i = 0; i < names.length; ++i) {
			boolean newBit = getBit(newStates, i);
			boolean newInteresting = getBit(newMask, i);
			if (getBit(oldStates, i) != newBit
			    || getBit(oldMask, i) != newInteresting)
				setElementIcon(i, newBit == true,
                                               newInteresting == true);
		}
	}
}
