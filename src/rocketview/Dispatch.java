package rocketview;

import cansocket.*;
import java.util.*;

class Dispatch extends Observable
{
    private boolean detect_stop;	// honor stop id

	public Dispatch( boolean ds )
	{
	    detect_stop = ds;
	}

	public void run(CanSocket sock)
	{
	    int cnt = 0;
		CanMessage m;
		try
		{
			// null is abnormal
			// the only expected terminator is stop_id
			while ((m = sock.recv()) != null)
			{
			    // m.print();	// debug print

			    // count messages
			    cnt ++;
			    if (cnt % 50 == 0)
				System.out.println( cnt + " messages received" );

			    // detect stop message
			    if( detect_stop &&
				m.getId11() == CanMessage.STOP_ID) {
				return;
			    }

			    // alert the media
			    setChanged();
			    notifyObservers(m);
			}

			System.out.println( "dispatch exits read loop" );
		}
		catch(Exception e)
		{
			System.err.println(e);
		}
	}
}
