package rocketview;

import javax.swing.*;
import javax.swing.border.*;

import widgets.*;
import cansocket.*;

public class ATVObserver extends JPanel implements CanObserver 
{

	public ATVObserver(CanDispatch dispatch)
	{
		setBorder(new TitledBorder("ATV"));
		setLayout(new GridBoxLayout());

		dispatch.add(this);
		
		add(StateGrid.getLabel("ATV"));
		add(StateGrid.getLabel("ATV_POWER_CAMERA"));
		add(StateGrid.getLabel("ATV_POWER_OVERLAY"));
		add(StateGrid.getLabel("ATV_POWER_TX"));
		add(StateGrid.getLabel("ATV_POWER_PA"));
		
		
	}
	
	public void message(CanMessage msg)
   {
		
   }
}
