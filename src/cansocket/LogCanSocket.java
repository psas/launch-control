package cansocket;

import java.io.*;

public class LogCanSocket implements CanSocket
{
	CanSocket base;
	Writer log;

	public LogCanSocket(CanSocket base, String logfile) throws IOException
	{
		this.base = base;
		log = new FileWriter(logfile, /* append */ true);
	}

	public NetMessage read() throws IOException
	{
		return log(base.read());
	}

	public void write(NetMessage msg) throws IOException
	{
		base.write(log(msg));
	}

	protected NetMessage log(NetMessage msg) throws IOException
	{
		StringBuffer buf = new StringBuffer();
/*
		if (msg instanceof CanMessage)
		{
			CanMessage cm = (CanMessage) msg;
			//!!! why not use cm.toString?
			byte body[] = cm.getBody();
			buf.append(cm.getId())
				.append(' ').append(cm.getTimestamp())
				.append(' ').append(body.length);
			for(int i = 0; i < body.length; ++i)
				buf.append(' ').append(body[i]);
		}
		buf.append('\n');
*/
		log.write(msg.toString());
		log.write("\n");
		log.flush();

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
