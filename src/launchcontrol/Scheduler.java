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

	public synchronized void startCountdown() throws java.io.IOException
	{
		if(timer != null)
			throw new IllegalStateException("timer already running");

		float startTime, endTime; // in seconds
		conf = new Properties();
		conf.load(events.openStream());
		startTime = Float.parseFloat(conf.getProperty("startTime"));
		endTime = Float.parseFloat(conf.getProperty("endTime"));

		new ScheduledTask("sound", conf.getProperty("startSound")).run();

		timer = new Timer();
		Enumeration keys = conf.keys();
		while(keys.hasMoreElements())
		{
			String key = (String)keys.nextElement();
			float time;
			try {
				time = Float.parseFloat(key);
			} catch(NumberFormatException e) {
				continue; // wasn't a number; try the next key
			}
			String value = conf.getProperty(key);
			int comma = value.indexOf(',');
			String action = value.substring(0, comma).trim();
			String cmd = value.substring(comma + 1).trim();
			timer.schedule(new ScheduledTask(action, cmd),
				(int)((time - startTime) * 1000));
			if(time > endTime)
				endTime = time;
		}

		timer.schedule(new EndCountdown(), (int)((endTime - startTime) * 1000));
	}

	public synchronized void abortCountdown()
	{
		if(timer == null)
			throw new IllegalStateException("timer not running");
		timer.cancel();
		timer = null;
		new ScheduledTask("sound", conf.getProperty("abortSound")).run();
		conf = null;
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

	private class EndCountdown extends TimerTask
	{
		public void run()
		{
			timer.cancel();
			timer = null;
			System.out.println("Countdown end.");
		}
	}
}
