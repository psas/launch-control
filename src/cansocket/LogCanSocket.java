/* Copyright 2004 David Cassard, Ian Osgood, Jamey Sharp, Peter Welte
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
package cansocket;

import java.io.*;
import java.util.Date;
import java.text.SimpleDateFormat;

/* dump all CAN message network traffic to a log file */
/* the raw log file can translated to ASCII Cantalope format with the fcfifo 'dumplog' tool */

public class LogCanSocket implements CanSocket
{
	CanSocket base;
	DataOutputStream log;

	public LogCanSocket(CanSocket base, String logfile) throws IOException
	{
		this.base = base;
		log = new DataOutputStream( new FileOutputStream(logfile, /* append */ true) );
		/* TODO: initial message to capture local date and time
			to calibrate the timestamps and signal the start of a session */
		/* OR: allocate a unique file name per session */
	}

	public CanMessage read() throws IOException
	{
		return log(base.read());
	}

	public void write(CanMessage msg) throws IOException
	{
		base.write(log(msg));
	}

	protected CanMessage log(CanMessage msg) throws IOException
	{
		msg.putMessage( log );
		// log.flush();
		return msg;
	}

	public void close() throws IOException
	{
		base.close();
		log.close();
	}

	public void flush() throws IOException
	{
		base.flush();
	}
}
