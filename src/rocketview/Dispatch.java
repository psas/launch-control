package rocketview;

import cansocket.*;
import java.util.*;

class Dispatch extends Observable
{
	public void run(CanSocket sock) throws Exception
	{
		CanMessage m;
		while ((m = sock.read()) != null)
		{
		    setChanged();
		    notifyObservers(m);
		}
	}
}
