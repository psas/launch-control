package rocketview;

import cansocket.*;
import stripchart.*;

import java.util.*;
import javax.swing.*;

class GPSObserver extends JLabel implements Observer
{
	public void update(Observable o, Object arg)
	{
		CanMessage msg = (CanMessage) arg;
		if(msg.getId() != CanBusIDs.GPSStatus)
			return;
		StringBuffer buf = new StringBuffer();
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
		for(int i = 8 - s.length(); i > 0; --i)
			buf.append('0');
		buf.append(s);
		return buf;
	}
}
