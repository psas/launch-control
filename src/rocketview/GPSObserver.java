package rocketview;

import cansocket.*;
import widgets.*;

import java.text.DecimalFormat;
import javax.swing.*;
import javax.swing.border.*;

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

	protected static String[][] lockTestNames = { {"Propagated", "Alt. used", null, "PM"}, {"Alt. used", null, "<4 sats", "EHPE", "EVPE"} };

	
	// fields
	protected int used = 0;
	protected int visible = 0;
	

	protected final NameDetailLabel latLabel = new NameDetailLabel("Lat", "-");
	protected final NameDetailLabel lonLabel = new NameDetailLabel("Lon", "-");
	protected final NameDetailLabel altLabel = new NameDetailLabel("Alt", "-");
	protected final TimeObserver time = new TimeObserver();
	protected final NameDetailLabel satsLabel = new NameDetailLabel("Sats", "-/-");
	protected final NameDetailLabel lockLabel;
	protected final NameDetailLabel solLabel = new NameDetailLabel("Solution", "-");
	protected final NameDetailLabel valLabel = new NameDetailLabel("Validity", "-");

	public GPSObserver(CanDispatch dispatch)
	{
		setLayout(new GridBoxLayout());

		dispatch.add(this);
		dispatch.add(time);

		add(StateGrid.getLabel("GPS"));
		add(latLabel);
		add(lonLabel);
		add(altLabel);
		add(time);
		add(satsLabel);
		lockLabel = StateGrid.getLabel("GPS_LOCKED");
		lockLabel.setText("Locked");
		lockLabel.setDetail("-");
		add(lockLabel);
		add(solLabel);
		add(valLabel);
		add(StateGrid.getLabel("GPS_POWER"));
		add(StateGrid.getLabel("GPS_UART_TRANSMIT"));
		add(StateGrid.getLabel("GOT_GPS"));
		add(StateGrid.getLabel("SANE_GPS"));
	}

	public void message(CanMessage msg)
	{
		
		switch(msg.getId())
		{
			case CanBusIDs.FC_GPS_HEIGHT:
				altLabel.setDetail((msg.getData32(0) / (float)100.0) + "m");
				return;
			case CanBusIDs.FC_GPS_LATLON:
				StringBuffer labelString = new StringBuffer();
				dir(labelString, msg.getData32(0), 'N', 'S');
				latLabel.setDetail(labelString.toString());
				
				labelString = new StringBuffer();
				dir(labelString, msg.getData32(1), 'E', 'W');
				lonLabel.setDetail(labelString.toString());
				return;
			case CanBusIDs.FC_GPS_SATS_VIS:
				visible = msg.getData8(0);
				break;
			case CanBusIDs.FC_GPS_SATS_USED:
				used = msg.getData8(0);
				break;
			case CanBusIDs.FC_GPS_NAVSOL:
				//navsol messages have two "interesting"
				//bytes, one for solution and one for
				//validity.
				StringBuffer[] navsolStrings = {new StringBuffer(), new StringBuffer()};
				boolean[] navsolChecks = {true, true};

				int[] navsolBytes = {msg.getData16(0), msg.getData16(1)};
				//cycle through the two bytes, then the
				//bits in each byte
				for(int byt = 0; byt < 2; byt++)
				{
					for(int bit = 0; bit < lockTestNames[byt].length; bit++)
					{
						if(lockTestNames[byt][bit] == null)
							continue; 
						if((navsolBytes[byt] & (1 << bit)) == 0)
							continue;
						//if navsolChecks[byt] is false,
						//some name has already been
						//added. Add a comma.
						if(!navsolChecks[byt])
							navsolStrings[byt].append(", ");
						else
							navsolChecks[byt] = false;
						navsolStrings[byt].append(lockTestNames[byt][bit]);
					}
					if(navsolChecks[byt])
						navsolStrings[byt].append("OK");
				}

				solLabel.setDetail(navsolStrings[0].toString());
				valLabel.setDetail(navsolStrings[1].toString());
				
				if (navsolChecks[0] && navsolChecks[1])
					lockLabel.setDetail("Yes");
				else
					lockLabel.setDetail("No");
				
				return;
			default:
				return;
		}

		satsLabel.setDetail(String.valueOf(used + "/" + visible));
	}

	/*
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
}
