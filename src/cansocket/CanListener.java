package cansocket;

import java.io.*;
import java.util.*;

public class CanListener extends Observable
{
    protected final CanSocket sock;

    public CanListener(CanSocket sock)
    {
	this.sock = sock;
    }

    public void run() throws IOException
    {
        CanMessage m;
        while ((m = sock.read()) != null)
	{
	    setChanged();
	    notifyObservers(m);
	}
    }
}
