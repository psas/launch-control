package rocketview;

import cansocket.*;

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
//class APSObserver extends JPanel implements Observer
class APSObserver extends JLabel implements Observer
{
	protected final DecimalFormat fmt = new DecimalFormat("0.000");

    protected double voltage = -1;
    protected double current = -1;
    protected double charge = -1;

    protected short pwr_charger = -1; //is battery being charged? 
    protected short umb_shore_pwr = -1; // is voltage coming over umbellical?
    protected short umb_connector = -1; // is umbellical connected? 
    protected short aps_switch_1 = -1; // is power to FC?
    protected short aps_switch_2 = -1; // is power to all can nodes?
    protected short aps_switch_3 = -1; // is power to atv power amp?
    protected short aps_switch_4 = -1; // is power to wifi power amp?

    public APSObserver() {
	setText();
    }

    /** Turn a number (1 or 0) to "Yes" or "No", respectively. 
     * also, if number is -1, it reports "Unknown" */
    protected String nToStr(int num) 
    {
	switch (num) {
	    case 0:
		return "No";
	    case 1:
		return "Yes";
	    case -1:
		return "Unknown";
	    default:
		return "???";
	}
    }

    /* append state description to buffer.
     * output will look like "desc: YES|NO <br>"
     * @param b: buffer to append to
     * @param desc: description before the "YES/NO" part.
     * @param bool: 0 = NO, 1 = YES
     */
    protected void appendState(StringBuffer b, String desc, int bool)
    {
	b.append(desc + ": " + nToStr(bool) + "<br>");
    }


    protected void setText()
    {
	StringBuffer b = new StringBuffer("<html><body>");
	b.append("APS bus: ");

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

	//state info from the APS:
	b.append("<br>");
	appendState(b, "Charging?", pwr_charger);
	appendState(b, "UMB connected?", umb_connector);
	appendState(b, "Shore power?", umb_shore_pwr);
	appendState(b, "Power to S1 (FC)?", aps_switch_1);
	appendState(b, "Power to S2 (CAN)?", aps_switch_2);
	appendState(b, "Power to S3 (ATV pwr amp)?", aps_switch_3);
	appendState(b, "Power to S4 (wifi pwr amp)? ", aps_switch_4);
	b.append("</body></html>");
	super.setText(b.toString());
    }

    public void update(Observable o, Object arg)
    {
	//int counts;
	short counts;
	if (!(arg instanceof CanMessage))
	    return;
			
	// filter on id
	CanMessage msg = (CanMessage) arg;
	switch(msg.getId())
	{
	    case CanBusIDs.APS_DATA_VOLTS:
		counts = msg.getData16(0);
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
	    case CanBusIDs.PWR_REPORT_CHARGER:
		pwr_charger = msg.getData8(0);
		break;
	    case CanBusIDs.UMB_REPORT_SHORE_POWER:
		umb_shore_pwr = msg.getData8(0);
		break;
	    case CanBusIDs.UMB_REPORT_CONNECTOR:
		umb_connector = msg.getData8(0);
		break;
	    case CanBusIDs.APS_REPORT_SWITCH_1:
		aps_switch_1 = msg.getData8(0);
		break;
	    case CanBusIDs.APS_REPORT_SWITCH_2:
		aps_switch_2 = msg.getData8(0);
		break;
	    case CanBusIDs.APS_REPORT_SWITCH_3:
		aps_switch_3 = msg.getData8(0);
		break;
	    case CanBusIDs.APS_REPORT_SWITCH_4:
		aps_switch_4 = msg.getData8(0);
		break;
	    default:
		return;
	}
	setText();
    }
}
