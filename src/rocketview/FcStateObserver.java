package rocketview;

import cansocket.*;

import java.util.*;
import javax.swing.*;

class FcStateObserver extends JLabel implements Observer
{
    public void update(Observable o, Object arg)
    {
	if (!(arg instanceof CanMessage))
		return;
			
	CanMessage msg = (CanMessage) arg;
	if(msg.getId11() != CanBusIDs.FC_REPORT_STATE >> 5)
	    return;

	setText(String.valueOf(msg.getData8(0)));
    }
}
