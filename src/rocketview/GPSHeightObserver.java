package rocketview;

import cansocket.*;

import java.util.*;
import javax.swing.*;

/*----------------------------------------------------------------
 * Handles message id GPSHeight
 * Updates the GPS box with height
 *
 * expects one 4-byte integer interpreted as
 * display = value / 100 meters
 *
 * display: GPS height: xxxm
 */
class GPSHeightObserver extends JLabel implements Observer
{
    protected float gpsHeight = Float.MAX_VALUE;

    public GPSHeightObserver() {
	this.setText( "GPS: -- no gps height --" );
    }

    public void update(Observable o, Object arg)
    {
	// filter on id
	CanMessage msg = (CanMessage) arg;
	if( msg.getId11() != CanBusIDs.GPSHeight )
	    return;

	gpsHeight = msg.getData32(0) / (float)100.0;
	// System.out.println( "gpsheight: " + gpsHeight );

	StringBuffer buf = new StringBuffer( "GPS height: " );
	if( gpsHeight < Float.MAX_VALUE )
	    buf.append( gpsHeight ).append( "m" );
	else
	    buf.append( "*bad value*" );

	setText( buf.toString() );
    }
}
