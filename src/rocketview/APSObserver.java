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

    public APSObserver() {
	this.setText( "APS bus: xx.xxV x.xxA  batt: xx.xxxAHr" );
    }

    public void update(Observable o, Object arg)
    {
	// filter on id
	CanMessage msg = (CanMessage) arg;
	if( msg.getId11() != CanBusIDs.PowerID )
	    return;

	// here we would do something useful

	//setText( buf.toString() );

    }
}
