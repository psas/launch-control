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
