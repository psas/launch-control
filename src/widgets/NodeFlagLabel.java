/* Copyright 2005 Jamey Sharp
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
package widgets;

import cansocket.*;

import java.lang.reflect.*;

public class NodeFlagLabel extends NodeStateLabel
{
	protected final int id;

	public NodeFlagLabel(String name, int bit)
		throws NoSuchFieldException, IllegalAccessException
	{
		super(name, bit);
		int idx = name.indexOf('_');
		String prefix = name.substring(0, idx);
		String suffix = name.substring(idx);
		id = CanBusIDs.class.getField(prefix + "_REPORT" + suffix).getInt(null);
		setDetail("-");
	}

	public void message(CanMessage msg)
	{
		super.message(msg);
		if(msg.getId() != id)
			return;
		if(msg.getData8(0) == 0)
			setDetail("Off");
		else
			setDetail("On");
	}
}
