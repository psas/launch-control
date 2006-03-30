/* Copyright 2005 Ian Osgood, Jamey Sharp
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
import widgets.*;

public class BooleanStateLabel extends NameDetailLabel implements CanObserver
{
	protected final int id;

	public BooleanStateLabel(String name, int id)
	{		
		super(name);
		setDetail("-");
		this.id = id;
	}

	public void message(CanMessage msg)
	{
		if(msg.getId() != id)
			return;

		if (msg.getData8(0) == 0)
			setDetail("Off");
		else
			setDetail("On");
	}
}
