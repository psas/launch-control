package rocketview;

import cansocket.*;
import widgets.*;

import java.awt.*;
import java.util.*;
import javax.swing.*;

class GPSObserver extends JPanel implements Observer
{
	int visible = 0;
	int used = 0;

	protected final GPSPositionObserver pos = new GPSPositionObserver();
	protected final JLabel alt = new JLabel("altitude: unknown");
	protected final JLabel sats = new JLabel("sats: 0/0");
	protected final LockStateLabel lock = new LockStateLabel();

	public GPSObserver()
	{
		setLayout(new GridBoxLayout());
		add(pos);
		add(alt);
		add(sats);
		add(lock);
	}

	public void update(Observable o, Object arg)
	{
		if (!(arg instanceof CanMessage))
			return;
			
		CanMessage msg = (CanMessage) arg;

		pos.update(o, arg);
		lock.update(msg);

		switch(msg.getId())
		{
			case CanBusIDs.FC_GPS_HEIGHT:
				alt.setText("altitude: " + (msg.getData32(0) / (float)100.0) + 'm');
				return;
			case CanBusIDs.FC_GPS_SATS_VIS:
				visible = msg.getData8(0);
				break;
			case CanBusIDs.FC_GPS_SATS_USED:
				used = msg.getData8(0);
				break;
			default:
				return;
		}

		sats.setText("sats: " + used + '/' + visible);
	}

	private static class LockStateLabel extends StateLabel
	{
		public LockStateLabel()
		{
			super(makeName(0));
		}

		private static String makeName(int lockbits)
		{
			StringBuffer buf = new StringBuffer("lock: 0x");
			String hex = Integer.toHexString(lockbits);
			for(int i = 8 - hex.length(); i > 0; --i)
				buf.append('0');
			return buf.append(hex).toString();
		}

		public void update(CanMessage msg)
		{
			if(msg.getId() != CanBusIDs.FC_GPS_NAVSOL)
				return;
			setKnown(true);
			int lockbits = msg.getData32(0);
			// testing bits 0 2 3 4 16 17 19
			// indicating: Altitude used, Not enough sattillites, Exceeded max EHorPE
			// Exceeded EVelPE, Propogated SOL, Altitude Used, PM
			setState((lockbits & 0xb001d) != 0);
			setText(makeName(lockbits));
		}
	}
}
