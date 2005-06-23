package rocketview;

import cansocket.*;

import java.util.*;
import javax.swing.border.*;

class IMUBorder extends TitledBorder implements CanObserver
{
	protected final String name, unit;
	protected final int id, pos;
	
	protected int sum, high, low, n;
	protected int time;
	
	public IMUBorder(CanDispatch dispatch, String name, String unit, int id, int pos)
	{
		super(name + ": -");

		this.name = name + ": ";
		this.unit = " " + unit;
		this.id = id;
		this.pos = pos;
		
		sum = high = n = 0;
		low = 0xffff;
		time = 0;

		dispatch.add(this);
	}
	
	public void message(CanMessage msg)
	{
		if (msg.getId() == id)
		{
			int data = msg.getData16(pos);
			if (low  > data) low  = data;
			if (high < data) high = data;
			sum += data;
			n++;
			if (msg.getTimestamp() > time)
			{
				float avg = sum/n;
				setTitle(name + avg + unit + " (" + n + ")");

				sum = high = n = 0;
				low = 0xffff;
				time = msg.getTimestamp() + 50;
			}
		}
	}
}
