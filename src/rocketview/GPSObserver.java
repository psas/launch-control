package rocketview;

import cansocket.*;

import java.util.*;
import javax.swing.*;

class GPSObserver extends JLabel implements Observer
{
	public void update(Observable o, Object arg)
	{
		if (!(arg instanceof CanMessage))
			return;
			
		// filter out non-GPS messages
		CanMessage msg = (CanMessage) arg;
		if(msg.getId11() != CanBusIDs.FC_GPS_NAVSOL >> 5)
			return;

		StringBuffer buf = new StringBuffer( "GPS: " );
		if(msg.getData16(0) != 0 || msg.getData16(1) != 0)
			buf.append("not ");
		buf.append("locked: 0x");
		buf.append(Integer.toHexString(msg.getData16(0)));
		buf.append(" 0x");
		buf.append(Integer.toHexString(msg.getData16(1)));
		setText(buf.toString());
	}
}
