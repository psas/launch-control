package rocketview;

import cansocket.*;
import stripchart.*;

import java.util.*;
import javax.swing.*;

class PositionObserver extends JLabel implements Observer
{
	public void update(Observable o, Object arg)
	{
		CanMessage msg = (CanMessage) arg;
		if(msg.getId() != CanBusIDs.GPSLatLon)
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
		mag = -mag;
		float rad = mag / (float)100000000.0;
		b.append(rad * (180.0 / Math.PI)).append(sgn);
	}
}
