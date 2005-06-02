package rocketmon;

import cansocket.*;

import java.awt.*;
import javax.swing.*;

public class StateLabel extends JLabel implements CanObserver
{
	protected final int index;
	protected final int mask;
	protected final Color bg;

	protected boolean state = false;
	protected boolean known = false;

	public StateLabel(String name, int id)
	{
		super(name);
		index = id / 8;
		mask = 1 << (id % 8);
		bg = getBackground();
	}

	public void message(CanMessage msg)
	{
		switch(msg.getId())
		{
			case CanBusIDs.FC_REPORT_NODE_STATUS:
				state = (msg.getData8(index) & mask) != 0;
				break;
			case CanBusIDs.FC_REPORT_IMPORTANCE_MASK:
				known = (msg.getData8(index) & mask) != 0;
				break;
			default:
				return;
		}

		Color c;
		if(state)
			c = Color.GREEN;
		else
			c = Color.RED;
		if(known)
			setBackground(c);
		else
			setBackground(bg);
		setBorder(BorderFactory.createLineBorder(c));
	}
}
