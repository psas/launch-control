package rocketview;

import cansocket.*;
import widgets.NameDetailLabel;

import javax.swing.*;

class TimeObserver extends NameDetailLabel implements CanObserver
{
	public TimeObserver() {
		super("Time", "-");
	}

	public void message(CanMessage msg)
	{
		if(msg.getId11() != CanBusIDs.FC_GPS_TIME >> 5)
				return;

		byte day = msg.getData8(0);
		byte month = msg.getData8(1);
		short year = msg.getData16(1);
		byte hour = msg.getData8(4);
		byte minute = msg.getData8(5);
		byte second = msg.getData8(6);

		StringBuffer buf = new StringBuffer();
		buf.append( year ).append( "/" );
		buf.append( month ).append( "/" );
		buf.append( day ).append( " " );
		buf.append( hour ).append( ":" );
		buf.append( minute ).append( ":" );
		buf.append( second );

		setDetail( buf.toString() );
	}
}
