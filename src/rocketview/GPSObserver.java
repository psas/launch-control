package rocketview;

import cansocket.*;

import java.util.*;
import javax.swing.*;

class GPSObserver extends JLabel implements Observer
{
	String locked = "not ";
	int visible = 0;
	int used = 0;

	public void update(Observable o, Object arg)
	{
		if (!(arg instanceof CanMessage))
			return;
			
		// filter out non-GPS messages
		CanMessage msg = (CanMessage) arg;
		switch (msg.getId11()) {
			case CanBusIDs.FC_GPS_NAVSOL >> 5:
				// testing bits 0 2 3 4 16 17 19
	// indicating: Altitude used, Not enough sattillites, Exceeded max EHorPE
 //  Exceeded EVelPE, Propogated SOL, Altitude Used, PM
				if((msg.getData32(0) & 0xb001d) != 0)
					locked = "not ";
				else
					locked = "";
				break;
			case CanBusIDs.FC_GPS_SATS_VIS >> 5:
				visible = msg.getData8(0);
				break;
			case CanBusIDs.FC_GPS_SATS_USED >> 5:
				used = msg.getData8(0);
				break;
			default:
				return;
		}

		StringBuffer buf = new StringBuffer( " sats: " );
		buf.append(used).append("/").append(visible);
		buf.append(", GPS ").append(locked).append("locked: 0x");
		buf.append(Integer.toHexString(msg.getData32(0)));
		
		setText(buf.toString());
	}
}
