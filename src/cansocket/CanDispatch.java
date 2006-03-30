/* Copyright 2005 Jamey Sharp
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
