package rocketview;

import cansocket.*;
import stripchart.*;

import java.util.*;
import javax.swing.*;

class TimeObserver extends JLabel implements Observer
{
	public void update(Observable o, Object arg)
	{
		CanMessage msg = (CanMessage) arg;
		switch(msg.getId())
		{
                    case CanBusIDs.GPSTime:
                        byte year = msg.getData8(0);
                        byte month = msg.getData8(1);
                        byte day = msg.getData8(2);
                        byte hour = msg.getData8(3);
                        byte minute = msg.getData8(4);
                        byte second = msg.getData8(5);
                        
                        StringBuffer buf = new StringBuffer();
                        int bigYear =  year + 2000;
                        buf.append( bigYear );
                        buf.append( "/" );
                        buf.append( month );
                        buf.append( "/" );
                        buf.append( day );
                        
                        buf.append( " " );
                        buf.append( hour );
                        buf.append( ":" );
                        buf.append( minute );
                        buf.append( ":" );
                        buf.append( second );
                        
                        setText( buf.toString() );
                    
                default:
			/* message not for me; do nothing */
		}
	}
}
