package rocketview;

import cansocket.*;
import stripchart.*;

import java.awt.*;
import java.util.*;
import javax.swing.*;

class IMUObserver extends JPanel implements Observer
{
	protected final Dimension preferredSize = new Dimension(400, 150);

	protected final int IMU_ACCEL = 0;
	protected final int IMU_GYRO = 1;

	protected final StreamXYDataset data[][];
	protected final String title[][] = {
		{
			"X",
			"Y",
			"Z",
		},
		{
			"Pitch",
			"Yaw",
			"Roll",
		},
	};
	protected final int freq[] = { 250, 100, };
	protected final int HIST_LEN = 30;
	protected int msgCount[] = { 0, 0, };

	public IMUObserver()
	{
		JStripChart chart;
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		data = new StreamXYDataset[title.length][];
		for(int i = 0; i < data.length; ++i)
		{
			data[i] = new StreamXYDataset[title[i].length];
			for(int j = 0; i < data[i].length; ++i)
			{
				data[i][j] = new StreamXYDataset(freq[i] * HIST_LEN);
				chart = new JStripChart(title[i][j], data[i][j], null);
				chart.setPreferredSize(preferredSize);
				add(chart);
			}
		}
	}

	public void update(Observable o, Object arg)
	{
		CanMessage msg = (CanMessage) arg;
		switch(msg.getId())
		{
		case CanBusIDs.IMUAccel:
			addValues(IMU_ACCEL, msg);
			break;
		case CanBusIDs.IMUGyro:
			addValues(IMU_GYRO, msg);
			break;
		}
	}

	protected void addValues(int type, CanMessage msg)
	{
		Integer x = new Integer(msgCount[type]++);
		for(int i = 0; i < data[type].length; ++i)
		{
			Short y = new Short(msg.getData16(i));
			data[type][i].addXYValue(x, y);
		}
	}
}
