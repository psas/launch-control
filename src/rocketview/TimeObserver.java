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
                case CanBusIDs.GPSTime: {
                    Byte year = new Byte(msg.getData8(0));
                    Byte month = new Byte(msg.getData8(1));
                    Byte day = new Byte(msg.getData8(2));
                    Byte hour = new Byte(msg.getData8(3));
                    Byte minute = new Byte(msg.getData8(4));
                    Byte second = new Byte(msg.getData8(5));
                    
                    StringBuffer buf = new StringBuffer();
                    Integer bigYear = new Integer( year.intValue() + 2000 );
                    buf.append( bigYear.toString() );
                    buf.append( "/" );
                    buf.append( month.toString() );
                    buf.append( "/" );
                    buf.append( day.toString() );
                    
                    buf.append( " " );
                    buf.append( hour.toString() );
                    buf.append( ":" );
                    buf.append( minute.toString() );
                    buf.append( ":" );
                    buf.append( second.toString() );
                    
                    setText( buf.toString() );
                    
               }
                default:
			/* message not for me; do nothing */
		}
	}
}
