package rocketview;

import cansocket.*;

import java.awt.*;
import java.util.*;
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
class APSObserver extends JPanel implements Observer
{
	protected final DecimalFormat fmt = new DecimalFormat("0.000");

	protected final CanDispatch dispatch = new CanDispatch();
	protected final JLabel busLabel = new JLabel();

	protected double voltage = -1;
	protected double current = -1;
	protected double charge = -1;

	public APSObserver()
	{
		setLayout(new GridBoxLayout());
		BooleanStateLabel label[] = {
			new BooleanStateLabel("Charging", CanBusIDs.PWR_REPORT_CHARGER),
			new BooleanStateLabel("Umbilical", CanBusIDs.UMB_REPORT_CONNECTOR),
			new BooleanStateLabel("Shore power", CanBusIDs.UMB_REPORT_SHORE_POWER),
			new BooleanStateLabel("S1 (FC)", CanBusIDs.APS_REPORT_SWITCH_1),
			new BooleanStateLabel("S2 (CAN)", CanBusIDs.APS_REPORT_SWITCH_2),
			new BooleanStateLabel("S3 (ATV amp)", CanBusIDs.APS_REPORT_SWITCH_3),
			new BooleanStateLabel("S4 (wifi amp) ", CanBusIDs.APS_REPORT_SWITCH_4),
		};
		add(busLabel);
		for(int i = 0; i < label.length; ++i)
		{
			dispatch.add(label[i]);
			add(label[i]);
		}
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

    public void update(Observable o, Object arg)
    {
	if (!(arg instanceof CanMessage))
	    return;
			
	// filter on id
	CanMessage msg = (CanMessage) arg;

	dispatch.update(msg);

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
	    default:
		return;
	}
	setText();
    }
}
