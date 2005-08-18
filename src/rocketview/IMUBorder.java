package rocketview;

import cansocket.*;

import java.util.*;
import java.text.*;
import java.awt.event.*;
import javax.swing.border.*;

class IMUBorder extends TitledBorder implements CanObserver, ActionListener
{
	protected final DecimalFormat fmt = new DecimalFormat("0.00");
	protected final String name, unit;
	protected final int id, pos;

	protected double sum, bias, gain;
	protected int n, time;

	public IMUBorder(CanDispatch dispatch, String name, String unit, int id, int pos)
	{
		super(name + ": -");

		this.name = name + ": ";
		this.unit = " " + unit;
		this.id = id;
		this.pos = pos;
		
		sum = bias = 0.0;
		time = n = 0;
		gain = 1.0;

		dispatch.add(this);
	}

	public void setBias(double bias)
	{
		this.bias = bias;
	}
	
	public void setGain(double gain)
	{
		this.gain = gain;
	}
	
	public void message(CanMessage msg)
	{
		if (msg.getId() == id)
		{
			double data = (msg.getData16(pos) - bias) / gain;
			sum += data;
			n++;
			if (msg.getTimestamp() > time)
			{
				double avg = sum/n;
				setTitle(name + fmt.format(avg) + unit);

				sum = 0.0;
				n = 0;
				time = msg.getTimestamp() + 100;
			}
		}
	}
	
	public void actionPerformed(ActionEvent event)
	{
		// from "Calibrate" button
		if (n>0) bias += gain*sum/n;
		setTitle(name + "0.0" + unit);
	}
}
