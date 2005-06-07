package widgets;

import cansocket.*;

import java.awt.*;
import javax.swing.*;

/** A grid of leds displaying info about different elements, 
 * which are nodes in our case, and each of which is labeled. */
public class StateGrid extends JPanel
{
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
		/* Shared tests */
		"ZERO_IMU_GYRO",
		"PRESSURE_VALID",
		"GPS_LOCKED",
		"SAFE_DESCENT_GPS",
		"DROGUE_DEPLOY_SAFE_GPS",
		"SAFE_DESCENT_PRESSURE",
		"DROGUE_DEPLOY_SAFE_PRESSURE",
		"HEIGHT_MATCH_GPS_PRESSURE",
	
		/* Preflight check tests. */
		"SANE_ANTENNAS",
		"SANE_IMU_ACCEL",
		"QUIET_PRESSURE_BASE",
		"GOT_GPS",
		"SANE_GPS",
	
		/* Arming tests. */
		"ARMING_IMU_FAST",
	
		/* Boost tests */
		"BOOST_GPS",
		"BOOST_IMU",
		"BOOST_UMB",
		"BOOST_PRESSURE",
	
		/* Coast tests */
		"APOGEE_PRESSURE",
	
		/* Deploy drogue tests */
		"DROGUE_PRESSURE",
		"DROGUE_GPS",
		"DROGUE_IMU",
		"DROGUE_WORKING",
	
		/* Descent drogue tests */
		"DESCEND_GPS",
		"DESCEND_PRESSURE",
		"DESCEND_MAIN_FUTURE",
	
		/* Descend main tests */
		"TOUCHDOWN_GPS",
		"TOUCHDOWN_PRESSURE",
	
		/* Recovery wait tests */
		"RECOVERY_VOLTS",
	};

	public StateGrid(CanDispatch dispatch)
	{
		setLayout(new GridLayout(0,2));

		for (int i = 0; i < names.length; ++i)
		{
			NodeStateLabel gridEntry = new NodeStateLabel(names[i], i);
			dispatch.add(gridEntry);
			add(gridEntry);
		}
	}
}
