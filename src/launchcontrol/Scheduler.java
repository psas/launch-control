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

	private URL events;
	private Timer timer;

	private ScheduleListener listener = null;
	private int millidelta;

	private int startTime, endTime; // in milliseconds
	private Date started;

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

	/**
	 * Requests that the given ScheduleListener be called once every delta
	 * milliseconds during a countdown. Only takes effect on the next call
	 * to startCountdown() or abortCountdown(). Only the most recently
	 * added listener will recieve schedule events.
	 */
	public void addScheduleListener(ScheduleListener l, int delta)
	{
		if(l == null)
			return; // no action when given a null listener
		listener = l;
		millidelta = delta;
	}

	/**
	 * Reads the events file and begin executing it. Events are executed in
	 * a separate thread. No exception is thrown nor any action taken if the
	 * countdown has already started.
	 */
	public void startCountdown() throws IOException
	{
		if(timer != null)
			return; // can't re-start a running countdown.

		if(listener != null)
			listener.started();

		// read events file for general settings
		Properties conf = new Properties();
		conf.load(events.openStream());
		startTime = endTime = 0;

		try {
			String start = conf.getProperty("startTime");
			if(start != null)
				startTime = (int)(Float.parseFloat(start) * 1000);
		} catch(NumberFormatException e) {
			// ignore
		}
		try {
			String end = conf.getProperty("endTime");
			if(end != null)
				endTime = (int)(Float.parseFloat(end) * 1000);
		} catch(NumberFormatException e) {
			// ignore
		}

		setTimer(/*aborting*/ false);
	}

	/**
	 * Moves the countdown to time 0 if that time hasn't yet been reached.
	 * No exeception is thrown nor any action taken if the countdown has
	 * passed time 0, nor if the countdown is currently stopped.
	 */
	public void abortCountdown() throws IOException
	{
		if(timer == null)
			return; // can't cancel a countdown that isn't running.
		if((new Date().getTime() - started.getTime() + startTime) > 0)
			return; // it's too late, you can't abort now. muhuhahaha.

		if(listener != null)
			listener.aborted();

		timer.cancel();
		timer = null;

		setTimer(/*aborting*/ true);
	}

	/**
	 * Initializes the Timer instance from the events file. Used by both
	 * startCountdown() and abortCountdown() with the aborting parameter
	 * set to false and true, respectively. When aborting, events set to
	 * execute before time 0 are ignored, and startTime is forced to 0.
	 */
	private void setTimer(boolean aborting) throws IOException
	{
		// don't start until everything has probably been set up
		// tweak the number in the following line for an appropriate delay
		started = new Date(new Date().getTime() + 250);

		if(aborting)
			startTime = 0;

		timer = new Timer();
		// read events file for timing
		BufferedReader cs = new BufferedReader(
			new InputStreamReader(events.openStream())
		);
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

			if(aborting && time <= 0)
				continue; // when aborting, run only positive-scheduled events

			// sanity check start and end times
			if(time < startTime) // assertion: if aborting, this is always false.
				startTime = time;
			if((time + 1) > endTime)
				endTime = time + 1;

			String value = line.substring(colon + 1).trim();
			String action, cmd;
			// split on first comma
			int comma = value.indexOf(',');
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

			// schedule this event
			timer.schedule(new ScheduledTask(action, cmd),
				new Date(started.getTime() + time - startTime));
		}

		// schedule the repeated clock updates as requested for the listener
		if(listener != null && millidelta > 0)
			timer.scheduleAtFixedRate(new TimerTask() {
				public void run()
				{
					listener.time(new Date().getTime() - started.getTime() + startTime);
				}
			}, started, millidelta);

		// schedule cleanup at endTime
		timer.schedule(new TimerTask() {
			public void run()
			{
				if(listener != null)
					listener.ended();

				timer.cancel();
				timer = null;
			}
		}, new Date(started.getTime() + endTime - startTime));
	}

	private class ScheduledTask extends TimerTask
	{
		final SchedulableAction action;
		final String cmd;

		public ScheduledTask(String actstr, String c)
		{
			action = (SchedulableAction)types.get(actstr);
			if(action == null)
				throw new IllegalArgumentException("unknown action " + actstr);
			cmd = c;
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
