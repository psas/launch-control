package rocketview;

import cansocket.*;

import java.util.*;

class Dispatch extends Observable implements Runnable
{
	CanSocket s;

	public Dispatch(CanSocket s)
	{
		this.s = s;
	}

	public void run()
	{
		CanMessage m;
		try
		{
			while((m = s.read()) != null)
			{
				setChanged();
				notifyObservers(m);
			}
		}
		catch(Exception e)
		{
			System.err.println(e);
		}
	}
}
