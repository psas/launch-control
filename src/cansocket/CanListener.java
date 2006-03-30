/* Copyright 2005 Ian Osgood, Jamey Sharp, Karl Hallowell, Peter Welte,
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

public class CanListener extends Observable implements Runnable
{
    protected final CanSocket sock;

    public CanListener(CanSocket sock)
    {
	this.sock = sock;
    }

    public void run() 
    {
        CanMessage m;
		try 
		{
			while ((m = sock.read()) != null)
			{
			setChanged();
			notifyObservers(m);
			}
		} 
		catch (IOException e) 
		{
			System.err.println("CanListener encountered error " + 
					"reading socket: " + e);
		}
    }
}
