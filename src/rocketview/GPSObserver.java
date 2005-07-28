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
	
	// fields
	protected int used = 0;
	protected int visible = 0;
	

	protected final NameDetailLabel latLabel = new NameDetailLabel("Lat");
	protected final NameDetailLabel lonLabel = new NameDetailLabel("Lon");
	protected final NameDetailLabel altLabel = new NameDetailLabel("Alt");
	protected final TimeObserver time = new TimeObserver();
	protected final JLabel satsLabel = new JLabel("Sats: -/-");
	protected final NameDetailLabel lockLabel;
	protected final NameDetailLabel solLabel = new NameDetailLabel("Solution");
	protected final NameDetailLabel valLabel = new NameDetailLabel("Validity");

	public GPSObserver(CanDispatch dispatch)
	{
		setBorder(new TitledBorder("GPS"));
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
		add(lockLabel);
		add(solLabel);
		add(valLabel);
		add(StateGrid.getLabel("GPS_POWER"));
		add(StateGrid.getLabel("GPS_UART_TRANSMIT"));
		add(StateGrid.getLabel("SAFE_DESCENT_GPS"));
		add(StateGrid.getLabel("DROGUE_DEPLOY_SAFE_GPS"));
		add(StateGrid.getLabel("GOT_GPS"));
		add(StateGrid.getLabel("SANE_GPS"));
		add(StateGrid.getLabel("BOOST_GPS"));
		add(StateGrid.getLabel("DROGUE_GPS"));
		add(StateGrid.getLabel("DESCEND_GPS"));
		add(StateGrid.getLabel("TOUCHDOWN_GPS"));
		add(StateGrid.getLabel("HEIGHT_MATCH_GPS_PRESSURE"));
		
	}

	private void add(NameDetailLabel label)
	{
		label.setDetail("-");
		super.add(label);
	}

	public void message(CanMessage msg)
	{
		StringBuffer labelString = new StringBuffer();
		StringBuffer solString = new StringBuffer();
		StringBuffer valString = new StringBuffer();
		
		boolean solCheck = false;
		boolean valCheck = false;
		
		switch(msg.getId())
		{
			case CanBusIDs.FC_GPS_HEIGHT:
				altLabel.setDetail("" + (msg.getData32(0) / (float)100.0) + 'm');
				return;
			case CanBusIDs.FC_GPS_LATLON:
				labelString = new StringBuffer();
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
				int solution = msg.getData16(0);
				int validity = msg.getData16(1);
				
				// if bits 0,1,3 of byte 0 are 1 then there is a quality solution, else there's not so
				// list the reasons why
				if ((solution & 11) == 0)
				{
					solString.append("OK");
					solCheck = true;
				} else
				{
					if ((solution & 1) != 0)
					{
						labelString.append("Propogated, ");
					} 
					
					if ((solution & 2) != 0)
					{
						labelString.append("Alt. Used, ");
					} 
					
					if ((solution & 8) != 0)
					{
						labelString.append("PM");
					}
				}
				solLabel.setDetail(solString.toString());
				
				// if bits 0,2,3,4 of byte 3 are 1 then the solution is valid, else it's not so
				// list the reasons why
				if ((validity & 29) == 0)
				{
					valString.append("OK");
					valCheck = true;
				} else
				{
					if ((validity & 1) != 0)
					{
						valString.append("Alt. Used, ");
					} 
					
					if((validity & 4) != 0) 
					{
						valString.append("<4 Sats, ");
					} 
					
					if ((validity & 8) != 0)
					{
						valString.append("EHPE, ");
					} 
					
					if ((validity & 16) != 0)
					{
						valString.append("EVPE");
					}
				}
				valLabel.setDetail(valString.toString());
				
				if (solCheck && valCheck)
					lockLabel.setDetail("Yes");
				else
					lockLabel.setDetail("No");
				
				return;
			default:
				return;
		}

		satsLabel.setText("Sats: " + used + '/' + visible);
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
