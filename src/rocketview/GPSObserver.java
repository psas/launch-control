package rocketview;

import cansocket.*;

import java.util.*;
import javax.swing.*;

/*----------------------------------------------------------------
 * Handles message id GPSID
 * Updates the GPS box with status display
 *
 * expects 3 bytes
 * if byte1=0 and byte2=0
 *   locked
 * else
 *   not locked
 *
 *   display: byte0 sats byte1 in binary byte2 in bunary
 */
class GPSObserver extends JLabel implements Observer
{
	public GPSObserver() {
	    this.setText( "GPS: -- no gps status --" );
	}

	public void update(Observable o, Object arg)
	{
		// filter out non-GPS messages
		CanMessage msg = (CanMessage) arg;
		if(msg.getId11() != CanBusIDs.GPSID)
			return;

		// System.out.println( "gpsid" );

		// StringBuffer buf = new StringBuffer();
		StringBuffer buf = new StringBuffer( "GPS: " );
		if(msg.getData8(1) != 0 || msg.getData8(2) != 0)
			buf.append("not ");
		buf.append("locked, ");
		buf.append(msg.getData8(0)).append(" sats, ");
		appendBinary(buf, msg.getData8(1)).append(' ');
		appendBinary(buf, msg.getData8(2));
		setText(buf.toString());
	}

	protected StringBuffer appendBinary(StringBuffer buf, byte v)
	{
		String s = Integer.toBinaryString(v);
		int i = s.length();
		if(i > 8)
			s = s.substring(i - 8);
		else
			for(i = 8 - i; i > 0; --i)
				buf.append('0');
		buf.append(s);
		return buf;
	}
}
