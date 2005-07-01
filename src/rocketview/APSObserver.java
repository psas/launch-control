package rocketview;

import cansocket.*;

import java.text.*;
import javax.swing.*;

/*----------------------------------------------------------------
 * -- not implemented --
 * Handles message id PowerID
 * Updates the APS box with status
 *
 *   display: APS bus: xx.xxV  batt: x.xxA xx.xxxAHr
 *          : Umb: [on/off] Power: [on/off] Charge: [on/off] 
 */
class APSObserver extends JPanel implements CanObserver
{
	protected final DecimalFormat fmt = new DecimalFormat("0.000");

	protected final JLabel umbLabel = new JLabel("Umbilical: -");
	protected final JLabel busLabel = new JLabel();

	protected double voltage = -1;
	protected double current = -1;
	protected double charge = -1;
	
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
	StringBuffer b = new StringBuffer("APS bus: ");

	if (voltage != -1) 
		b.append(fmt.format(voltage)).append(" V, ");
	else 
		b.append("xx.xxV ");

	if (current != -1)
		b.append(fmt.format(current)).append(" A, ");
	else 
		b.append("x.xxA ");

	if (charge != -1)
		b.append(fmt.format(charge)).append(" Ah");
	else 
		b.append("batt: xx.xxxAHr");

	busLabel.setText(b.toString());
    }

    public void message(CanMessage msg)
    {
	switch(msg.getId())
	{
	    case CanBusIDs.APS_DATA_VOLTS:
		short counts = msg.getData16(0);
		//counts &= 0xffff;   // unsigned.  What the heck?
		voltage = counts * 0.033488;  // * 5 / 1024 / 0.14581;
		break;
	    case CanBusIDs.APS_DATA_AMPS:
		current = 768.05 / msg.getData32(0);
		break;
	    case CanBusIDs.APS_DATA_CHARGE:
			// 853.4 * 10^-6 Ah / count
		charge = 853.4e-6 * msg.getData16(0);
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
