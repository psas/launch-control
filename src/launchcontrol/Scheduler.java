import java.io.*;
import java.net.*;
import java.util.*;

public class Scheduler
{
	private static Hashtable types = new Hashtable();
	static {
		types.put("sound", new SoundAction());
		types.put("msg", new MessageAction());
	}

	private Timer timer = null;
	private URL events = null;
	private Properties conf = null;
	private ScheduleListener listener = null;
	private int startTime, endTime; // in milliseconds
	private Date started = null;

	/**
	 * Create a new Scheduler from an event list. The event list is not
	 * read immediately, but will be re-read every time startCountdown()
	 * is called.
	 *
	 * @param events URL to a configuration file listing events
	 */
	public Scheduler(URL events)
	{
		this.events = events;
	}

	public synchronized void startCountdown(ScheduleListener l, int millidelta)
		throws Exception
	{
		if(timer != null)
			throw new IllegalStateException("timer already running");
		listener = l;
		if(listener != null)
			listener.started();

		started = new Date(new Date().getTime() + 1000);

		conf = new Properties();
		conf.load(events.openStream()); // read once for general settings
		startTime = (int)(Float.parseFloat(conf.getProperty("startTime", "0")) * 1000);
		endTime = (int)(Float.parseFloat(conf.getProperty("endTime", "0")) * 1000);

		timer = new Timer();
		if(listener != null && millidelta > 0)
			timer.scheduleAtFixedRate(new TimerTask() {
				public void run()
				{
					listener.time(new Date().getTime() - started.getTime() + startTime);
				}
			}, started, millidelta);

		BufferedReader cs = new BufferedReader(new InputStreamReader(events.openStream())); // read again for timing
		String line;
		while((line = cs.readLine()) != null)
		{
			int colon = line.indexOf(':');
			if(colon == -1)
				continue; // no colon present; try the next line

			int time;
			try {
				time = (int)(Float.parseFloat(line.substring(0, colon).trim()) * 1000);
			} catch(NumberFormatException e) {
				continue; // wasn't a number; try the next line
			}

			String value = line.substring(colon + 1).trim();
			int comma = value.indexOf(',');
			String action;
			String cmd;
			if(comma != -1)
			{
				action = value.substring(0, comma).trim();
				cmd = value.substring(comma + 1).trim();
			}
			else
			{
				action = value;
				cmd = "";
			}
			timer.schedule(new ScheduledTask(action, cmd),
				new Date(started.getTime() + time - startTime));

			// sanity check start and end times
			if(time < startTime)
				startTime = time;
			if(time > endTime)
				endTime = time;
		}

		timer.schedule(new TimerTask() {
			public void run()
			{
				timer.cancel();
				timer = null;
				conf = null;
				if(listener != null)
					listener.ended();
				listener = null;
			}
		}, new Date(started.getTime() + endTime - startTime));
	}

	public synchronized void abortCountdown() throws Exception
	{
		if(timer == null)
			throw new IllegalStateException("timer not running");
		timer.cancel();
		timer = null;
		conf = null;
		if(listener != null)
			listener.aborted();
		listener = null;
	}

	private class ScheduledTask extends TimerTask
	{
		private SchedulableAction action = null;
		private String cmd = null;

		public ScheduledTask(String action, String cmd)
		{
			this.action = (SchedulableAction)types.get(action);
			this.cmd = cmd;
		}

		public void run()
		{
			try {
				action.dispatch(cmd);
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
	}
}
