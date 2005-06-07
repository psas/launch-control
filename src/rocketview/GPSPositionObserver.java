package rocketview;

import cansocket.*;

import java.lang.*;
import java.util.*;
import java.text.*;
import javax.swing.*;

class GPSPositionObserver extends JLabel implements Observer
{
	protected static DecimalFormat minFmt = new DecimalFormat("00.000");
	public GPSPositionObserver()
	{
		setText("gps: unknown");
	}

	public void update(Observable o, Object arg)
	{
		if (!(arg instanceof CanMessage))
			return;
			
		CanMessage msg = (CanMessage) arg;
		if(msg.getId() != CanBusIDs.FC_GPS_LATLON)
			return;

		StringBuffer b = new StringBuffer();
		dir(b, msg.getData32(0), 'N', 'S');
		b.append(' ');
		dir(b, msg.getData32(1), 'E', 'W');
		setText(b.toString());
	}

	protected static void dir(StringBuffer b, int mag, char pos, char neg)
	{
		char sgn = mag < 0 ? neg : pos;
		float rad = Math.abs(mag) / (float)100000000.0;
		double deg = Math.toDegrees(rad);
		double degOnly = Math.floor(deg);
		double minutes = (deg - degOnly) * 60.0;
		b.append(Math.round(degOnly)).append("° ").append(minFmt.format(minutes)).append(sgn);
	}
}
