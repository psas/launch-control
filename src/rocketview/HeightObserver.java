package rocketview;

import cansocket.*;

import java.util.*;
import javax.swing.*;

class HeightObserver extends JLabel implements Observer
{
	protected Float gpsHeight;
	protected Float altitude;

	public HeightObserver() 
	{
		setText("altitude: unknown");
	}

	public void update(Observable o, Object arg)
	{
		if (!(arg instanceof CanMessage))
			return;

		CanMessage msg = (CanMessage) arg;
		if (msg.getId() == CanBusIDs.FC_GPS_HEIGHT) {
			gpsHeight = new Float(msg.getData32(0) / (float)100.0);
		} else {
			return;
		}

		StringBuffer buf = new StringBuffer();

		if(gpsHeight != null)
			buf.append("gps altitude: ").append(gpsHeight).append("m  ");
		if(altitude != null)
			buf.append("pressure altitude: ").append(altitude).append("m");
		setText(buf.toString());
	}
}
