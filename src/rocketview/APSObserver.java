package rocketview;

import cansocket.*;

import java.util.*;
import javax.swing.*;

/*----------------------------------------------------------------
 * -- not implemented --
 * Handles message id PowerID
 * Updates the APS box with status
 *
 *   display: APS bus: xx.xxV x.xxA  batt: xx.xxxAHr
 */
class APSObserver extends JLabel implements Observer
{
    protected int voltage = -1;
    protected int current = -1;
    protected int charge = -1;

    public APSObserver() {
	setText();
	this.setText( "APS bus: xx.xxV x.xxA  batt: xx.xxxAHr" );
    }

    protected void setText()
    {
	StringBuffer b = new StringBuffer("APS bus: ");
	b.append(voltage).append("V ");
	b.append(current).append("A  batt: ");
	b.append(charge).append("AHr");
	super.setText(b.toString());
    }

    public void update(Observable o, Object arg)
    {
	if (!(arg instanceof CanMessage))
	    return;
			
	// filter on id
	CanMessage msg = (CanMessage) arg;
	switch(msg.getId11())
	{
	    case CanBusIDs.PWR_REPORT_VOLTAGE >> 5:
		voltage = msg.getData16(0);
		break;
	    case CanBusIDs.PWR_REPORT_CURRENT >> 5:
		current = msg.getData16(0);
		break;
	    case CanBusIDs.PWR_REPORT_CHARGE >> 5:
		charge = msg.getData16(0);
		break;
	    default:
		return;
	}
	setText();
    }
}
