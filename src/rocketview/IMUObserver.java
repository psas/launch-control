/* Copyright 2005 Ian Osgood, Jamey Sharp, Travis Spencer, Tim Welch
 * 
 * This program is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by the Free
 * Software Foundation; either version 2 of the License, or (at your option)
 * any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for
 * more details.
 *
 * Portland State Aerospace Society (PSAS) is a student branch chapter of the
 * Institute of Electrical and Electronics Engineers Aerospace and Electronics
 * Systems Society. You can reach PSAS at info@psas.pdx.edu.  See also
 * http://psas.pdx.edu/
 */
package rocketview;

import cansocket.*;
import widgets.*;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

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
	protected JButton calibrateButton = new JButton("Calibrate Bias");

	
	// first subscript in arrays is one of these
	protected final int IMU_ACCEL = 0;
	protected final int IMU_GYRO = 1;

	protected final StripChart data[][];
	protected final String title[][] = {
		{ "X", "Y", "Z", },
		{ "Pitch", "Yaw", "Roll", }
	};
	protected final double bias[][] = { { 2400.45, 2462.06, 1918.72 }, { 0, 0, 0 } };
	protected final double gain[][] = {
		{ 392.80, 386.90, 77.00 },		// cf. fcfifo/imu.c
		{ 22.75, 22.75, 22.75 }
	};
	protected final String unit[] = { "g", "deg/s" };
	protected final int id[] = {
		CanBusIDs.IMU_ACCEL_DATA,
		CanBusIDs.IMU_GYRO_DATA
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
		
		GridBoxLayout mainLayout = new GridBoxLayout();
		setLayout(mainLayout);

		add(calibrateButton);
		
		JPanel subSys = new JPanel();
		subSys.setLayout(new GridLayout(0, 1));
		data = new StripChart[title.length][];
		for(int i = 0; i < data.length; ++i)
		{
			data[i] = new StripChart[title[i].length];
			for(int j = 0; j < data[i].length; ++j)
				subSys.add(createChart(dispatch, i, j));
		}

		GridBagConstraints gbc = (GridBagConstraints)mainLayout.getConstraints(calibrateButton).clone();
		gbc.fill = GridBagConstraints.BOTH;
		gbc.weighty = 1.0;
		gbc.gridheight = GridBagConstraints.REMAINDER;
		add(subSys, gbc);
	}

	protected JComponent createChart(CanDispatch dispatch, int type, int num)
	{
		StripChart chart = new StripChart();
		data[type][num] = chart;

		// data.setYRange(vLow[type][num], vHigh[type][num]);
		chart.setYRange(0, 4095);

		IMUBorder border = new IMUBorder(dispatch, title[type][num], unit[type], id[type], num);
		border.setBias(bias[type][num]);
		border.setGain(gain[type][num]);
		chart.setBorder(border);
		if(type == IMU_GYRO)
			calibrateButton.addActionListener(border);
		return chart;
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
