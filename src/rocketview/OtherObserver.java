package rocketview;

import javax.swing.*;
import javax.swing.border.*;

import widgets.*;
import cansocket.*;

public class OtherObserver extends JPanel implements CanObserver 
{

	public OtherObserver(CanDispatch dispatch, FCStateLabel stateLabel)
	{
		setBorder(new TitledBorder("Other"));
		setLayout(new GridBoxLayout());

		dispatch.add(this);
		
		add(stateLabel);
		add(StateGrid.getLabel("SAFE_DESCENT_PRESSURE"));
		JLabel label = StateGrid.getLabel("DROGUE_DEPLOY_SAFE_PRESSURE");
		label.setText("DROGUE_DEPLOY_SAFE_PRESS");
		add(label);
		add(StateGrid.getLabel("SANE_ANTENNAS"));
		add(StateGrid.getLabel("LOG_AVAIL"));
		add(StateGrid.getLabel("BOOST_UMB"));
		add(StateGrid.getLabel("BOOST_PRESSURE"));
		add(StateGrid.getLabel("APOGEE_PRESSURE"));
		add(StateGrid.getLabel("DROGUE_PRESSURE"));
		add(StateGrid.getLabel("DROGUE_WORKING"));
		add(StateGrid.getLabel("DESCEND_PRESSURE"));
		add(StateGrid.getLabel("DESCEND_MAIN_FUTURE"));
		add(StateGrid.getLabel("TOUCHDOWN_PRESSURE"));
	}
	
	public void message(CanMessage msg)
   {
		
   }
}
