package rocketview;

import cansocket.*;

import java.util.*;
import javax.swing.*;

/*----------------------------------------------------------------
 * Handles message id GPSTime
 * Updates the time box with rocket time
 *
 * expects 6 bytes
 *   0 year
 *   1 month
 *   2 day
 *   3 hour
 *   4 minute
 *   5 second
 *
 *   display: rocket time: 20yy/mm/dd hh:mm:ss
 */
class TimeObserver extends JLabel implements Observer
{

    public TimeObserver() {
	this.setText( "-- no time from rocket yet --" );
    }

	public void update(Observable o, Object arg)
	{
		if (!(arg instanceof CanMessage))
			return;
			
		CanMessage msg = (CanMessage) arg;
		switch(msg.getId11())
		{
                    case CanBusIDs.GPSTime:
			// System.out.println( "gpstime" );
                        byte year = msg.getData8(0);
                        byte month = msg.getData8(1);
                        byte day = msg.getData8(2);
                        byte hour = msg.getData8(3);
                        byte minute = msg.getData8(4);
                        byte second = msg.getData8(5);

                        StringBuffer buf = new StringBuffer("rocket time: " );
                        int bigYear =  year + 2000;
                        buf.append( bigYear ).append( "/" );
                        buf.append( month ).append( "/" );
                        buf.append( day ).append( " " );
                        buf.append( hour ).append( ":" );
                        buf.append( minute ).append( ":" );
                        buf.append( second );
                        
                        setText( buf.toString() );
                    
                default:
			/* message not for me; do nothing */
		}
	}
}
