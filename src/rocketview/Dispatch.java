package rocketview;

import cansocket.*;

import java.util.*;

class Dispatch extends Observable
{
	public void run(CanSocket sock)
	{
		CanMessage m;
		try
		{
			while((m = sock.read()) != null)
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
