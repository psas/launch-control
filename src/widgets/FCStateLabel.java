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
				map.put(new Integer(fields[i].getInt(null)), name);
			} catch(IllegalAccessException e) {
				System.err.println("CanBusIDs field " + i + ": " + e);
			}
		stateStrings = (String[]) map.values().toArray(new String[map.size()]);
	}

	protected static final DateFormat df = new SimpleDateFormat("HH:mm:ss.SSS: ");
	protected static final long delay = 1200; /* link timeout delay (millisecs) */

	protected final Timer timer = new Timer(true /*daemon*/);

	protected String signal = "-/-";
	protected String state = "-";

	protected LinkStateChecker task;
	protected LinkStateListener listener;

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
		super("");
		updateText();
		dispatch.add(this);
	}

	public void addLinkStateListener(LinkStateListener listener)
	{
		this.listener = listener;
	}

	public void message(CanMessage msg)
	{
		switch(msg.getId())
		{
			case CanBusIDs.FC_REPORT_STATE:
				setState(msg.getData8(0) & 0xff);
				break;
			case CanBusIDs.FC_REPORT_LINK_QUALITY:
				setQuality(msg.getData16(0), msg.getData16(1));
				break;
		}
	}

	protected void setKnown(boolean known)
	{
		super.setKnown(known);
		if(listener != null)
			listener.linkStateChanged(known);
	}

	protected void setState(int state)
	{
		String date = df.format(new Date());
		if(state >= stateStrings.length)
			this.state = date + "unknown (" + state + ")";
		else
			this.state = date + stateStrings[state];
		updateText();
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

	protected void setQuality(short signal, short noise)
	{
		this.signal = "" + -signal + "/" + -noise + "dBm";
		updateText();
	}

	protected void updateText()
	{
		setText("s/n: " + signal + ", " + state);
	}
}
