package rocketview;

import cansocket.*;

import java.text.*;
import javax.swing.*;

class APSObserver extends JPanel implements CanObserver
{
	protected final DecimalFormat fmt = new DecimalFormat("0.000");

	protected final JLabel umbLabel = new JLabel("Umbilical: -");
	protected final JLabel busLabel = new JLabel();

	protected String voltage = "-";
	protected String current = "-";
	protected String charge = "-";
	
	protected void add(CanDispatch dispatch, BooleanStateLabel label)
	{
		dispatch.add(label);
		add(label);
	}

	public APSObserver(CanDispatch dispatch)
	{
		setBorder(new NodeBorder(this, dispatch, "APS", CanBusIDs.APS_REPORT_MODE)
				.addState(0x12,"Sleep").addState(0x23,"Awake")
				.addState(0x34,"Safe").addState(0x88,"Armed"));

		setLayout(new GridBoxLayout());

		dispatch.add(this);

		add(umbLabel);
		add(dispatch, new BooleanStateLabel("Shore power",  CanBusIDs.UMB_REPORT_SHORE_POWER));
		add(dispatch, new BooleanStateLabel("Rocket Ready", CanBusIDs.UMB_REPORT_ROCKETREADY));
		add(busLabel);
		add(dispatch, new BooleanStateLabel("Charging",     CanBusIDs.PWR_REPORT_CHARGER));
		add(dispatch, new BooleanStateLabel("S1 (FC)",      CanBusIDs.APS_REPORT_SWITCH_1));
		add(dispatch, new BooleanStateLabel("S2 (CAN)",     CanBusIDs.APS_REPORT_SWITCH_2));
		add(dispatch, new BooleanStateLabel("S3 (ATV)",     CanBusIDs.APS_REPORT_SWITCH_3));
		add(dispatch, new BooleanStateLabel("S4 (WIFI) ",   CanBusIDs.APS_REPORT_SWITCH_4));

		setText();
	}

	protected void setText()
	{
		busLabel.setText("Battery: " + voltage + "V " + current + "A " + charge + "Ah");
	}

    public void message(CanMessage msg)
    {
	switch(msg.getId())
	{
	    case CanBusIDs.APS_DATA_VOLTS:
		short counts = msg.getData16(0);
		//counts &= 0xffff;   // unsigned.  What the heck?
		voltage = fmt.format(counts * (5 / 1024 / 0.14581));
		break;
	    case CanBusIDs.APS_DATA_AMPS:
		current = fmt.format(768.05 / msg.getData32(0));
		break;
	    case CanBusIDs.APS_DATA_CHARGE:
			// 853.4 * 10^-6 Ah / count
		charge = fmt.format(853.4e-6 * msg.getData16(0));
		break;
		case CanBusIDs.UMB_REPORT_CONNECTOR:
			if (msg.getData8(0) == 0)
				umbLabel.setText("Umbilical: Removed");
			else
				umbLabel.setText("Umbilical: Connected");
			return;
	    default:
		return;
	}
	setText();
    }
}
