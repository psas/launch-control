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
import java.util.*;

public class NodeModeLabel extends NodeStateLabel
{
	protected final int id;

	/** Map from mode numbers to names. */
	protected final Map modes = new HashMap();

	public NodeModeLabel(String name, int bit)
		throws NoSuchFieldException, IllegalAccessException
	{
		super(name, bit);
		setDetail("-");

		id = CanBusIDs.class.getField(name + "_REPORT_MODE").getInt(null);

		/* Find all the legal modes for the current node. */
		Field[] fields = CanBusIDs.class.getFields();
		final String prefix = name + "_MODE_";
		for(int i = 0; i < fields.length; ++i)
		{
			String fname = fields[i].getName();
			if(!fname.startsWith(prefix))
				continue;
			byte code = (byte) fields[i].getInt(null);
			modes.put(new Byte(code), fname.substring(prefix.length()));
		}
	}

	public void message(CanMessage msg)
	{
		super.message(msg);
		if(msg.getId() != id)
			return;
		byte value = msg.getData8(0);
		String mode = (String) modes.get(new Byte(value));
		if(mode == null)
			mode = "0x" + Integer.toHexString((int) value & 0xff);
		setDetail(mode);
	}
}
