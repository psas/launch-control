package rocketview;

import java.text.*;
import java.util.*;
import javax.swing.*;
import javax.swing.border.*;

import widgets.*;
import cansocket.*;

public class OtherObserver extends JPanel implements CanObserver
{
	protected static final DecimalFormat nf = new DecimalFormat("###0.000MB");
	protected static final DecimalFormat lf = new DecimalFormat("0.00 sec");
	protected final NameDetailLabel signal = new NameDetailLabel("s/n", "-");
	protected final NameDetailLabel logAvail;
	protected final NameDetailLabel lag = new NameDetailLabel("Lag", "- sec");
	protected long dmsInitial;
	protected int nextTime = 0;

	public OtherObserver(CanDispatch dispatch)
	{
		setLayout(new GridBoxLayout());

		dispatch.add(this);

		add(new FCStateLabel(dispatch));
		
		add(signal);
		add(lag);
		logAvail = StateGrid.getLabel("LOG_AVAIL");
		logAvail.setDetail("?MB");
		add(logAvail);
		add(StateGrid.getLabel("SANE_ANTENNAS"));
	}

	private long delta(int timestamp)
	{
		return (long)((new Date()).getTime() - timestamp*1000*11932.0/1193182.0);
	}
	public void message(CanMessage msg)
	{
		if (dmsInitial == 0 && msg.getTimestamp() != 0)
		{
			dmsInitial = delta(msg.getTimestamp());
		}
		if (msg.getTimestamp() > nextTime)
		{
			long dmsNow = delta(msg.getTimestamp());
			
			lag.setDetail(lf.format((dmsNow - dmsInitial)/1000.0));

			nextTime = msg.getTimestamp()+25;
		}
		
		switch(msg.getId())
		{
			case CanBusIDs.FC_REPORT_LOG_AVAIL:
				logAvail.setDetail(nf.format(msg.getData32(0) / 1024.0));
				break;
			case CanBusIDs.FC_REPORT_LINK_QUALITY:
				signal.setDetail("" + -msg.getData16(0) + "/" + -msg.getData16(1) + "dBm");
				break;
		}
	}
}
