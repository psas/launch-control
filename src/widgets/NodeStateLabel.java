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

public class NodeStateLabel extends StateLabel implements CanObserver
{
	protected final int index;
	protected final int mask;

	public NodeStateLabel(String name, int id)
	{
		super(name);
		index = id / 8;
		mask = 1 << (id % 8);
	}

	public void message(CanMessage msg)
	{
		switch(msg.getId())
		{
			case CanBusIDs.FC_REPORT_NODE_STATUS:
				setState((msg.getData8(index) & mask) != 0);
				break;
			case CanBusIDs.FC_REPORT_IMPORTANCE_MASK:
				setKnown((msg.getData8(index) & mask) != 0);
				break;
			default:
				return;
		}
	}
}
