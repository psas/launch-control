package rocketview;

import cansocket.*;
import widgets.*;

public class BooleanStateLabel extends NameDetailLabel implements CanObserver
{
	protected final int id;

	public BooleanStateLabel(String name, int id)
	{		
		super(name);
		setDetail("-");
		this.id = id;
	}

	public void message(CanMessage msg)
	{
		if(msg.getId() != id)
			return;

		if (msg.getData8(0) == 0)
			setDetail("Off");
		else
			setDetail("On");
	}
}
