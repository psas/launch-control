package rocketview;

import java.util.*;
import javax.swing.*;

import cansocket.*;

/*----------------------------------------------------------------
 * Handles message id GPSLatLon
 * Updates the GPS position box with xx.xxxx N | S xx.xxx E | W
 *
 * interprets 8 bytes of can as 2 4-byte integers
 *
 * foreach value:
 * radians = |value| / 100,000,000
 * displayed = toDegrees(radians)
 *
 * positive = N or E
 * negative = S or W
 *
 * display: GPS position: xx.xxxx N | S xx.xxx E | W
 */
class GPSPositionObserver extends JLabel implements Observer
{
    public GPSPositionObserver() {
	this.setText( "GPS: -- no position --" );
    }

    public void update(Observable o, Object arg)
    {
	// filter on id
	CanMessage msg = (CanMessage) arg;
	if(msg.getId11() != CanBusIDs.GPSLatLon)
	    return;
	// System.out.println( "gpslatlon" );

	StringBuffer b = new StringBuffer( "GPS position: " );
	dir(b, msg.getData32(0), 'N', 'S');

	b.append(' ');
	dir(b, msg.getData32(1), 'E', 'W');

	setText(b.toString());
    }

    protected void dir(StringBuffer b, int mag, char pos, char neg)
    {
	char sgn = mag < 0 ? neg : pos;
	double rad = Math.abs (mag) / (double)100000000.0;
	double deg = Math.toDegrees( rad );

	b.append( deg ).append( sgn );
    }
}
