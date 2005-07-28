package rocketview;

import javax.swing.*;
import javax.swing.border.*;

import widgets.StateGrid;
import cansocket.*;

public class IMUStateObserver extends JPanel implements CanObserver{

	public IMUStateObserver(CanDispatch dispatch)
	{
		setBorder(new TitledBorder("IMU"));
		setLayout(new GridBoxLayout());

		dispatch.add(this);
				
		add(StateGrid.getLabel("IMU"));
		add(StateGrid.getLabel("IMU_ACCEL_DATA"));
		add(StateGrid.getLabel("SANE_IMU_ACCEL"));
		add(StateGrid.getLabel("BOOST_IMU"));
		add(StateGrid.getLabel("IMU_GYRO_DATA"));
		add(StateGrid.getLabel("DROGUE_IMU"));
		add(StateGrid.getLabel("ARMING_IMU_FAST"));
		add(StateGrid.getLabel("PRESS_REPORT_DATA"));
		add(StateGrid.getLabel("TEMP_REPORT_DATA"));
		add(StateGrid.getLabel("PRESSURE_VALID"));
		add(StateGrid.getLabel("QUIET_PRESSURE_BASE"));
	}
	
	
	public void message(CanMessage msg)
   {
		
   }
}
