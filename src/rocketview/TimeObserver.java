/* Copyright 2005 David Cassard, Ian Osgood, Jamey Sharp, Max Ehrenfreund
 *                Peter Welte, Tim Welch
 * 
 * This program is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by the Free
 * Software Foundation; either version 2 of the License, or (at your option)
 * any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for
 * more details.
 *
 * Portland State Aerospace Society (PSAS) is a student branch chapter of the
 * Institute of Electrical and Electronics Engineers Aerospace and Electronics
 * Systems Society. You can reach PSAS at info@psas.pdx.edu.  See also
 * http://psas.pdx.edu/
 */
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
		if (hour < 10) buf.append("0");
		buf.append( hour ).append( ":" );
		if (minute < 10) buf.append("0");
		buf.append( minute ).append( ":" );
		if (second < 10) buf.append("0");
		buf.append( second );

		setDetail( buf.toString() );
	}
}
