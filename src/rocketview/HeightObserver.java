package rocketview;

import cansocket.*;
import stripchart.*;

import java.util.*;
import javax.swing.*;

class HeightObserver extends JLabel implements Observer
{
	protected Float gpsHeight;
	protected Float imuHeight;

	public void update(Observable o, Object arg)
	{
		if (!(arg instanceof CanMessage))
			return;
			
		CanMessage msg = (CanMessage) arg;
		switch(msg.getId11())
		{
		case CanBusIDs.FC_GPS_HEIGHT >> 5:
			gpsHeight = new Float(msg.getData32(0) / (float)100.0);
			break;
		case CanBusIDs.FC_IMU_HEIGHT >> 5:
			imuHeight = new Float(msg.getData32(0) / (float)100.0);
			break;
		default:
			return;
		}
		StringBuffer buf = new StringBuffer();
		if(gpsHeight != null)
			buf.append("gps: ").append(gpsHeight).append("m  ");
		if(imuHeight != null)
			buf.append("press: ").append(imuHeight).append("m");
		setText(buf.toString());
	}
}
