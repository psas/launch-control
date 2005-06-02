package cansocket;

import java.io.*;
import java.util.*;

public class CanDispatch extends ArrayList implements Runnable
{
	protected final CanSocket sock;

	public CanDispatch(CanSocket sock)
	{
		this.sock = sock;
	}

	/** Force a cast to CanObserver to check the common add path. */
	public boolean add(Object o)
	{
		return add((CanObserver) o);
	}

	/** Fully statically-type-safe list add. */
	public boolean add(CanObserver o)
	{
		return super.add(o);
	}

	public void run() 
	{
		CanMessage m;
		try 
		{
			while ((m = sock.read()) != null)
			{
				Iterator it = iterator();
				while(it.hasNext())
					((CanObserver) it.next()).message(m);
			}
		} 
		catch (IOException e) 
		{
			System.err.println("CanListener encountered error " + 
					"reading socket: " + e);
		}
	}
}
