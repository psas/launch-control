package rocketview;

import cansocket.*;
import stripchart.*;

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
		if (arg instanceof CanMessage) {
			CanMessage msg = (CanMessage) arg;
			if (msg.getId11() == (CanBusIDs.FC_GPS_HEIGHT >> 5)) {
				gpsHeight = new Float(msg.getData32(0) / (float)100.0);
			} else {
				return;
			}
		} else if (arg instanceof PressureDataMessage) {
			PressureDataMessage pressureData = (PressureDataMessage) arg;
			altitude = new Float(pressureData.altitude);
		}
			
		StringBuffer buf = new StringBuffer();

		if(gpsHeight != null)
			buf.append("gps altitude: ").append(gpsHeight).append("m  ");
		if(altitude != null)
			buf.append("pressure altitude: ").append(altitude).append("m");
		setText(buf.toString());
	}
}
