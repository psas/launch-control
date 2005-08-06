package rocketview;

import java.text.*;
import java.math.*;
import javax.swing.*;
import javax.swing.border.*;

import cansocket.*;
import widgets.*;

public class IMUStateObserver extends JPanel implements CanObserver
{
	protected final DecimalFormat fmtP = new DecimalFormat("0.0kPa");
	protected final DecimalFormat fmtA = new DecimalFormat("0.0m");
	protected final DecimalFormat fmtT = new DecimalFormat("0.0C");
	protected final NameDetailLabel pressure = new NameDetailLabel("Press", "-kPa");
	protected final NameDetailLabel altitude = new NameDetailLabel("Alt", "-m");
	protected final NameDetailLabel temperature = new NameDetailLabel("Temp", "-C");
	
	protected double base_alt   = 0.0;
	protected double base_press = 1.0;
	protected double base_temp  = 15.0;

	protected static final double L = -0.0065;
	protected static final double R = 287.053;
	protected static final double g = 9.80655;
	protected static final double altExp = -L*R/g;
	
	protected double cookPressure(int rawPress)
	{
		return 0.0224468 * (470.734 + rawPress);
	}
	protected double cookTemperature(int rawTemp)
	{
		return 3487.972309658033 / Math.log(3.116381893600779E8 / rawTemp - 252811.23882451496) - 273.15;
	}
	protected double cookAltitude(double pressure)
	{
		return base_alt + (Math.pow(pressure/base_press, altExp)-1) * base_temp / L;
	}

	public IMUStateObserver(CanDispatch dispatch)
	{
		setLayout(new GridBoxLayout());

		dispatch.add(this);

		add(StateGrid.getLabel("IMU"));
		add(pressure);
		add(altitude);
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
			case CanBusIDs.FC_IMU_BASE:
				base_temp  = msg.getData16(0) / 100.0;
				base_press = cookPressure(msg.getData16(1));
				base_alt   = msg.getData32(1) / 100.0;
				return;
			case CanBusIDs.PRESS_REPORT_DATA:
				double p = cookPressure(msg.getData16(0));
				double a = cookAltitude(p);
				pressure.setDetail(fmtP.format(p));
				altitude.setDetail(fmtA.format(a));
				return;
			case CanBusIDs.TEMP_REPORT_DATA:
				double v = cookTemperature(msg.getData16(0));
				temperature.setDetail(fmtT.format(v));
				return;
		}
	}
}
