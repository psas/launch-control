package cansocket;

import java.io.*;
import java.util.Date;
import java.text.SimpleDateFormat;

public class LogCanSocket implements CanSocket
{
	CanSocket base;
	Writer log;
	SimpleDateFormat myformat;

	public LogCanSocket(CanSocket base, String logfile) throws IOException
	{
		this.base = base;
		log = new FileWriter(logfile, /* append */ true);
		/* timestamp log messages as e.g. "Fri, 15 Oct 2004 13:59:59:025 PDT": */
		myformat = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss:SSS z ");
		// write a header with the complete date
		log.write( myformat.format(new Date()) + " *** NEW SESSION ***\n");
		// use a more compact date for telemetry
		myformat = new SimpleDateFormat("HH:mm:ss:SSS ");
	}

	public CanMessage read() throws IOException
	{
		return log(base.read());
	}

	public void write(CanMessage msg) throws IOException
	{
		log.write( "-> ");		// distinguish commands from telemetry
		base.write(log(msg));
	}

	protected CanMessage log(CanMessage msg) throws IOException
	{
		log.write( myformat.format(new Date()) );
		log.write( msg.toString() );
		log.write( "\n" );
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
