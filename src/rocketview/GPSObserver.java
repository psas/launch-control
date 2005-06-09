package rocketview;

import java.text.DecimalFormat;

import cansocket.*;
import widgets.*;

import javax.swing.*;

class GPSObserver extends JPanel implements CanObserver
{
	// static fields
	protected static DecimalFormat minFmt = new DecimalFormat("00.000");
	
	// fields
	int visible = 0;
	int used = 0;
	StringBuffer latString;
	StringBuffer lonString;

	protected final JLabel lat = new JLabel("Lat: - N");
	protected final JLabel lon = new JLabel("Lon: - W");
	protected final JLabel alt = new JLabel("Alt: -");
	protected final TimeObserver time = new TimeObserver();
	protected final JLabel sats = new JLabel("Sats: -/-");
	protected final LockStateLabel lock = new LockStateLabel();

	public GPSObserver(CanDispatch dispatch)
	{
		dispatch.add(this);
		dispatch.add(time);
		dispatch.add(lock);

		setLayout(new GridBoxLayout());
		add(lat);
		add(lon);
		add(alt);
		add(time);
		add(sats);
		add(lock);
	}

	public void message(CanMessage msg)
	{
		switch(msg.getId())
		{
			case CanBusIDs.FC_GPS_HEIGHT:
				alt.setText("Alt: " + (msg.getData32(0) / (float)100.0) + 'm');
				return;
			case CanBusIDs.FC_GPS_LATLON:
				latString = new StringBuffer();
				dir(latString, msg.getData32(0), 'N', 'S');
				lat.setText("Lat:  " + latString.toString());
				
				lonString = new StringBuffer();
				dir(lonString, msg.getData32(1), 'E', 'W');
				lon.setText("Lon: " + lonString.toString());
				break;
			case CanBusIDs.FC_GPS_SATS_VIS:
				visible = msg.getData8(0);
				break;
			case CanBusIDs.FC_GPS_SATS_USED:
				used = msg.getData8(0);
				break;
			default:
				return;
		}

		sats.setText("Sats: " + used + '/' + visible);
	}
	
	/**
	 * dir constructs formatted lat/lon string
	 * @param b - StringBuffer to put formatted position data
	 * @param mag - 32 bit fixed point lat or lon data
	 * @param pos - N or E
	 * @param neg - S or W
	 */
	protected static void dir(StringBuffer b, int mag, char pos, char neg)
	{
		char sgn = mag < 0 ? neg : pos;
		float rad = Math.abs(mag) / (float)100000000.0;
		double deg = Math.toDegrees(rad);
		double degOnly = Math.floor(deg);
		double minutes = (deg - degOnly) * 60.0;
		// unicode degree character
		b.append(Math.round(degOnly)).append("\u00b0 ").append(minFmt.format(minutes)).append(sgn);
	}

	private static class LockStateLabel extends StateLabel implements CanObserver
	{
		public LockStateLabel()
		{
			super(makeName(0));
		}

		private static String makeName(int lockbits)
		{
			StringBuffer buf = new StringBuffer("Lock: 0x");
			String hex = Integer.toHexString(lockbits);
			for(int i = 8 - hex.length(); i > 0; --i)
				buf.append('0');
			return buf.append(hex).toString();
		}

		public void message(CanMessage msg)
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
