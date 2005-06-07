package rocketview;

import cansocket.*;
import widgets.*;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.*;

/*----------------------------------------------------------------
 * Handles message ids IMUAccel & IMUGyro
 * Updates the 6 stripcharts
 *
 * for IMUAccel expect 3 short ints (6 bytes total): x, y, z
 * for IMUGyro expect 3 short ints (6 bytes total): pitch, yaw, roll
 *
 * chart vertical axis expects values 0-4096 opaque units
 */
class IMUObserver extends JPanel implements CanObserver
{
	// first subscript in arrays is one of these
	protected final int IMU_ACCEL = 0;
	protected final int IMU_GYRO = 1;

	protected final StripChart data[][];
	protected final String title[][] = {
		{ "X", "Y", "Z", },
		{ "Pitch", "Yaw", "Roll", }
	};

/*
	// low limit, graph vertical axis
	protected final double vLow[][] = {
		{ -2, -2, -16 },
		{ -90, -90, -90 }
	};

	// high limit, graph vertical axis
	protected final double vHigh[][] = {
		{ 2, 2, 16 },
		{ 90, 90, 90 }
	};
*/


	public IMUObserver(CanDispatch dispatch)
	{
		dispatch.add(this);
		setLayout(new GridLayout(0, 1));
		data = new StripChart[title.length][];
		for(int i = 0; i < data.length; ++i)
		{
			data[i] = new StripChart[title[i].length];
			for(int j = 0; j < data[i].length; ++j)
				createChart(i, j);
		}
	}

	protected void createChart(int type, int num)
	{
		StripChart chart = new StripChart();
		data[type][num] = chart;

		// data.setYRange(vLow[type][num], vHigh[type][num]);
		chart.setYRange(0, 4095);

		JPanel panel = new JPanel();
		panel.setBorder(new TitledBorder(title[type][num]));
		panel.add(chart);
		add(panel);
	}

	public void message(CanMessage msg)
	{
		int type;
		switch (msg.getId())
		{
			case CanBusIDs.IMU_ACCEL_DATA:
				type = IMU_ACCEL;
				break;
			case CanBusIDs.IMU_GYRO_DATA:
				type = IMU_GYRO;
				break;
			default:
				return;
		}

		for(int i = 0; i < data[type].length; ++i)
			data[type][i].addPoint(msg.getTimestamp() / 100f, msg.getData16(i));
	}
}
