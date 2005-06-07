package widgets;

import cansocket.*;

public class NodeStateLabel extends StateLabel implements CanObserver
{
	protected final int index;
	protected final int mask;

	public NodeStateLabel(String name, int id)
	{
		super(name);
		index = id / 8;
		mask = 1 << (id % 8);
	}

	public void message(CanMessage msg)
	{
		switch(msg.getId())
		{
			case CanBusIDs.FC_REPORT_NODE_STATUS:
				setState((msg.getData8(index) & mask) != 0);
				break;
			case CanBusIDs.FC_REPORT_IMPORTANCE_MASK:
				setKnown((msg.getData8(index) & mask) != 0);
				break;
			default:
				return;
		}
	}
}
