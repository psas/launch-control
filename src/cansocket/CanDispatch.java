package cansocket;

import java.io.*;
import java.util.*;

public class CanDispatch extends ArrayList implements Runnable
{
	protected CanSocket sock;

	public CanDispatch(CanSocket sock)
	{
		setSocket(sock);
	}

	public CanDispatch()
	{
	}

	public void setSocket(CanSocket sock)
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

	public void update(CanMessage m)
	{
		for(int i = size() - 1; i >= 0; --i)
			((CanObserver) get(i)).message(m);
	}

	public void run() 
	{
		CanMessage m;
		try 
		{
			while ((m = sock.read()) != null)
				update(m);
		} 
		catch (IOException e) 
		{
			System.err.println("CanListener encountered error " + 
					"reading socket: " + e);
		}
	}
}
