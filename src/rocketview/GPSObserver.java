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
		// testing bits 0 2 3 4 16 17 19
		// indicating: Altitude used, Not enough sattillites, Exceeded max EHorPE
		//  Exceeded EVelPE, Propogated SOL, Altitude Used, PM
		if((msg.getData32(0) & 0xb001d) != 0)
			buf.append("not ");
		buf.append("locked: 0x");
		buf.append(Integer.toHexString(msg.getData32(0)));
		setText(buf.toString());
	}
}
