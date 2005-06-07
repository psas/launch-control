package rocketview;

import cansocket.*;
import widgets.*;

public class BooleanStateLabel extends StateLabel implements CanObserver
{
	protected final int id;

	public BooleanStateLabel(String name, int id)
	{
		super(name);
		this.id = id;
	}

	public void message(CanMessage msg)
	{
		if(msg.getId() != id)
			return;

		byte b = msg.getData8(0);
		setKnown(b == 0 || b == 1);
		setState(b != 0);
	}
}
