package rocketview;

import java.text.DecimalFormat;
import cansocket.*;
import widgets.*;
import javax.swing.*;

/* layout
GPS Node ([-|Safe|Armed])

Lat:  [-|xx deg xx.xxx']N
Lon:  [-|xxx deg xx.xxx']W
Alt:  [-|[xx,xxx]m (xx,xxxft)]

Sats: [-/-|0/0]

Locked:   [-|Yes|No]
Solution: [-|OK|Propated|Alt. used|PM]
Validity: [-|OK|Alt. used|Num sats|EHPE|EVPE]
*/

class GPSObserver extends JPanel implements CanObserver
{
	// static fields
	protected static DecimalFormat minFmt = new DecimalFormat("00.000");
	
	// fields
	protected int visible = 0;
	protected int used = 0;
	protected StringBuffer latString;
	protected StringBuffer lonString;

	protected final JLabel stateLabel = new JLabel("GPS Node: (-)");
	protected final JLabel latLabel = new JLabel("Lat: -");
	protected final JLabel lonLabel = new JLabel("Lon: -");
	protected final JLabel altLabel = new JLabel("Alt: -");
	protected final TimeObserver time = new TimeObserver();
	protected final JLabel satsLabel = new JLabel("Sats: -/-");
	protected final LockStateLabel lockLabel = new LockStateLabel();
	protected final JLabel solutionLabel = new JLabel("Solution: -");
	protected final JLabel validityLabel = new JLabel("Validity: -");

	public GPSObserver(CanDispatch dispatch)
	{
		dispatch.add(this);
		dispatch.add(time);
		dispatch.add(lockLabel);
		setLayout(new GridBoxLayout());
		add(stateLabel);
		add(latLabel);
		add(lonLabel);
		add(altLabel);
		add(time);
		add(satsLabel);
		add(lockLabel);
		add(solutionLabel);
		add(validityLabel);
	}

	public void message(CanMessage msg)
	{
		switch(msg.getId())
		{
			case CanBusIDs.GPS_REPORT_MODE:
				stateLabel.setText("GPS node: " + stateText(msg.getData8(0)));
				return;
			case CanBusIDs.FC_GPS_HEIGHT:
				altLabel.setText("Alt: " + (msg.getData32(0) / (float)100.0) + 'm');
				return;
			case CanBusIDs.FC_GPS_LATLON:
				latString = new StringBuffer();
				dir(latString, msg.getData32(0), 'N', 'S');
				latLabel.setText("Lat:  " + latString.toString());
				
				lonString = new StringBuffer();
				dir(lonString, msg.getData32(1), 'E', 'W');
				lonLabel.setText("Lon: " + lonString.toString());
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

		satsLabel.setText("Sats: " + used + '/' + visible);
	}
	
	/* 
	 * Determines GPS mode and returns string representation 
	 */
	private String stateText(int code)
	{
		switch(code)
		{
			case 0x34: return "(Safe)";
			case 0x88: return "(Armed)";
		}
		return "Unknown: " + code;
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
