package rocketview;

import cansocket.*;

import java.util.*;
import javax.swing.*;

/*----------------------------------------------------------------
 * -- not implemented --
 * Handles message id StatusID
 * Updates the FC State box with status
 *
 *   display: FC State:
 */
class FcStateObserver extends JLabel implements Observer
{

    public FcStateObserver() {
	this.setText( "-- unknown --" );
    }

    public void update(Observable o, Object arg)
    {
	if (!(arg instanceof CanMessage))
		return;
			
	// filter on id
	CanMessage msg = (CanMessage) arg;
	if( msg.getId11() != CanBusIDs.StatusID )
	    return;

	// here we would do something useful

	// setText( buf.toString() );

    }
}
