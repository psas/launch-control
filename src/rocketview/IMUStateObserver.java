package rocketview;

import java.text.*;
import javax.swing.*;
import javax.swing.border.*;

import cansocket.*;
import widgets.*;

public class IMUStateObserver extends JPanel implements CanObserver
{
	protected final DecimalFormat fmt = new DecimalFormat("0.0 C");
	protected final NameDetailLabel temperature = new NameDetailLabel("Temp", "-");

	public IMUStateObserver(CanDispatch dispatch)
	{
		setLayout(new GridBoxLayout());

		dispatch.add(this);

		add(StateGrid.getLabel("IMU"));
		add(temperature);
		add(StateGrid.getLabel("IMU_ACCEL_DATA"));
		add(StateGrid.getLabel("IMU_GYRO_DATA"));
		add(StateGrid.getLabel("PRESS_REPORT_DATA"));
		add(StateGrid.getLabel("TEMP_REPORT_DATA"));
		add(StateGrid.getLabel("SANE_IMU_ACCEL"));
		add(StateGrid.getLabel("PRESSURE_VALID"));
		add(StateGrid.getLabel("QUIET_PRESSURE_BASE"));
		add(StateGrid.getLabel("ARMING_IMU_FAST"));
	}

	public void message(CanMessage msg)
	{
		switch (msg.getId())
		{
			case CanBusIDs.TEMP_REPORT_DATA:
				double v = 3487.972309658033 / Math.log(3.116381893600779E8 / msg.getData16(0) - 252811.23882451496) - 273.15;
				temperature.setDetail(fmt.format(v));
				return;
		}
	}
}
