/* Copyright 2005 Jamey Sharp, Keith Packard
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
import java.text.*;
import java.util.*;

public class FCStateLabel extends StateLabel implements CanObserver
{
	protected static final String[] stateStrings;

	static {
		TreeMap map = new TreeMap();
		Field fields[] = CanBusIDs.class.getFields();
		for(int i = 0; i < fields.length; i++)
			try {
				String name = fields[i].getName();
				if(!name.endsWith("State"))
					continue;
				map.put(new Integer(fields[i].getInt(null)),
						name.substring(0, name.length() - "State".length()));
			} catch(IllegalAccessException e) {
				System.err.println("CanBusIDs field " + i + ": " + e);
			}
		stateStrings = (String[]) map.values().toArray(new String[map.size()]);
	}

	protected static final DateFormat df = new SimpleDateFormat("HH:mm:ss.SSS: ");
	protected static final long delay = 3600; /* link timeout delay (millisecs) */

	protected final Timer timer = new Timer(true /*daemon*/);

	protected LinkStateChecker task;

	protected class LinkStateChecker extends TimerTask
	{
		public void run()
		{
			// If the task is activated, then it has been too long since
			// we recieved a message
			setKnown(false);
		}
	}

	public FCStateLabel(CanDispatch dispatch)
	{
		super("-");
		dispatch.add(this);
	}

	public void message(CanMessage msg)
	{
		if(msg.getId() != CanBusIDs.FC_REPORT_STATE)
			return;

		int state = (int) msg.getData8(0) & 0xff;
		String date = df.format(new Date());
		if(state >= stateStrings.length)
			setText(date + "unknown (" + state + ")");
		else
			setText(date + stateStrings[state]);
		setState(true);
		setKnown(true);

		// set a task to run after delay milliseconds, which will happen
		// unless we've recieved a message (and thus entered this function)
		// before that time
		if (task != null)
			task.cancel();
		task = new LinkStateChecker();
		timer.schedule(task, delay, 1000);
	}
}
