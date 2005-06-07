package widgets;

import cansocket.*;

import java.text.*;
import java.util.*;
import javax.swing.*;
import java.awt.GridLayout;

public class RocketState extends JPanel implements Observer
{

	protected static final String[] stateStrings = {
		"EvaluatePowerup",
		"Initialize",
		"Idle",
		"PreflightCheck",
		"Ready",
		"Arming",
		"Armed",
		"RocketReady",
		"LaunchAbort",
		"Boost",
		"Coast",
		"DeployDrogue",
		"DescendDrogue",
		"DeployMain",
		"DescendMain",
		"RecoveryWait",
		"RecoverySleep",
		"PowerDown",
		"LawnDart",
	};

	protected static final DateFormat df = new SimpleDateFormat("HH:mm:ss.SSS: ");

	protected ImageIcon greenled = new ImageIcon(ClassLoader.getSystemResource("widgets/greenled.png"));
	protected ImageIcon redled = new ImageIcon(ClassLoader.getSystemResource("widgets/redled.png"));

	protected int state = -1;
	protected int detail = -1;
	protected int signal = 0;		// units: dbm
	protected int noise = 0;
	protected String dateStr = "";

	protected static final long delay = 1200; /* link timeout delay (millisecs) */
	protected final java.util.Timer timer = new java.util.Timer(true /*daemon*/);
	protected LinkStateChecker task;
	protected LinkStateListener listener;

	protected JLabel zLabel;	// Z IMU
	protected JLabel stateLabel;
	protected CanDispatch dispatch = new CanDispatch();

	protected class LinkStateChecker extends TimerTask
	{
		public void run()
		{
			// If the task is activated, then it has been too long since
			// we recieved a message
			stateLabel.setIcon(redled);
			if(listener != null)
				listener.linkStateChanged(false);
		}
	}




	public RocketState()
	{
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

		zLabel = new JLabel();
		add(zLabel);

		stateLabel = new JLabel();
		//stateLabel.setHorizontalAlignment(SwingConstants.LEFT);
		stateLabel.setAlignmentX(0.0f);
		updateText(); // get label ready for display 
		add(stateLabel);

		add(new JSeparator());

		add(new StateGrid(dispatch));
	}

	public void addLinkStateListener(LinkStateListener listener)
	{
		this.listener = listener;
	}

	public void update(Observable o, Object arg)
	{
		if (!(arg instanceof CanMessage))
			return;

		update((CanMessage) arg);
	}

	public void update(CanMessage msg)
	{
		dispatch.update(msg);

		switch(msg.getId())
		{
			case CanBusIDs.IMU_ACCEL_DATA:
				setZ(msg.getData16(2));
				break;
			case CanBusIDs.FC_REPORT_STATE:
				setState(msg.getData8(0) & 0xff);
				break;
			case CanBusIDs.FC_REPORT_LINK_QUALITY:
				setQuality(msg.getData16(0), msg.getData16(1));
				break;
		}
	}

	protected void setZ(int rawZ)
	{
		// zero: 1918.72
		// gain: 77.0
		double accel = ((double)rawZ - 1918.72)/77.0;
		zLabel.setText(Double.toString(accel));
	}

	protected void setState(int state)
	{
		if(this.state != state)
			detail = -1;
		this.dateStr = df.format(new Date());
		this.state = state;
		updateText();

		// set a task to run after delay milliseconds, which will happen
		// unless we've recieved a message (and thus entered this function)
		// before that time
		stateLabel.setIcon(greenled);
		if(listener != null)
			listener.linkStateChanged(true);
		if (task != null)
			task.cancel();
		task = new LinkStateChecker(); // will set "led" to red
		timer.schedule(task, delay, 1000);
	}

	protected void setQuality(short signal, short noise)
	{
		this.signal = -signal;
		this.noise = -noise;
		updateText();
	}

	protected void updateText()
	{
		StringBuffer b = new StringBuffer("s/n: ");
		b.append(Integer.toString(signal)).append("/");
		b.append(Integer.toString(noise)).append("dBm, ");
		if(state < 0)
		{
			b.append("unknown FC state");
			stateLabel.setText(b.toString());
			stateLabel.setIcon(redled);
			if(listener != null)
				listener.linkStateChanged(false);
			return;
		}
		b.append(dateStr);
		if(state >= stateStrings.length)
			b.append("unknown (").append(state).append(")");
		else
			b.append(stateStrings[state]);
		if(detail >= 0)
			b.append(" [").append(Integer.toBinaryString(detail)).append("]");
		stateLabel.setText(b.toString());

	}
}
