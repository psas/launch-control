package rocketview;

import cansocket.*;

import java.util.*;
import javax.swing.*;

/*----------------------------------------------------------------
 * -- not implemented --
 * Handles message id
 * Updates the Rocket State box with status
 *
 *   display: RocketState:
 */
class RocketStateObserver extends JLabel implements Observer
{

    public RocketStateObserver() {
	this.setText( "-- unknown --" );
    }

    public void update(Observable o, Object arg)
    {
	// filter on id
	CanMessage msg = (CanMessage) arg;
	if( msg.getId11() != CanBusIDs.StatusID )
	    return;

	// here we would do something useful

	// setText( buf.toString() );

    }
}
