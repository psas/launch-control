package widgets;

import cansocket.*;

import java.text.*;
import java.util.*;
import javax.swing.*;

public class RocketState extends JLabel implements Observer
{
	protected static final String[] stateStrings = {
		"EvaluatePowerup",
		"Initialize",
		"Idle",
		"PreflightCheck",
		"Ready",
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

	protected int state = -1;
	protected int detail = -1;

	public RocketState()
	{
		updateText();
	}

	public void update(NetMessage msg)
	{
		update(null, msg);
	}

	public void update(Observable o, Object arg)
	{
		if (!(arg instanceof CanMessage))
			return;

		update((CanMessage) arg);
	}

	public void update(CanMessage msg)
	{
		switch(msg.getId11())
		{
			case CanBusIDs.FC_REPORT_STATE >> 5:
				setState((int) msg.getData8(0) & 0xff);
				break;
			case CanBusIDs.FC_REPORT_STATE_DETAIL >> 5:
				setDetail(msg.getData32(0));
				break;
		}
	}

	protected void setState(int state)
	{
		if(this.state != state)
			detail = -1;
		this.state = state;
		updateText();
	}

	protected void setDetail(int detail)
	{
		this.detail = detail;
		updateText();
	}

	protected void updateText()
	{
		if(state < 0)
		{
			setText("unknown");
			return;
		}

		StringBuffer b = new StringBuffer(df.format(new Date()));
		if(state >= stateStrings.length)
			b.append("unknown (").append(state).append(")");
		else
			b.append(stateStrings[state]);
		if(detail >= 0)
			b.append(" [").append(Integer.toBinaryString(detail)).append("]");
		setText(b.toString());
	}
}
