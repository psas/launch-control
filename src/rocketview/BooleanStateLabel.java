package rocketview;

import cansocket.*;
import javax.swing.*;

public class BooleanStateLabel extends JLabel implements CanObserver
{
	protected final String name;
	protected final int id;

	public BooleanStateLabel(String name, int id)
	{		
		super(name + ": -");
		this.name = name;
		this.id = id;
	}

	public void message(CanMessage msg)
	{
		if(msg.getId() != id)
			return;

		if (msg.getData8(0) == 0)
			setText(name + ": Off");
		else
			setText(name + ": On");
	}
}
