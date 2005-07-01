package widgets;

import cansocket.*;

import java.lang.reflect.*;

public class NodeFlagLabel extends NodeStateLabel
{
	protected final int id;

	public NodeFlagLabel(String name, int bit)
		throws NoSuchFieldException, IllegalAccessException
	{
		super(name, bit);
		int idx = name.indexOf('_');
		String prefix = name.substring(0, idx);
		String suffix = name.substring(idx);
		id = CanBusIDs.class.getField(prefix + "_REPORT" + suffix).getInt(null);
		setDetail("-");
	}

	public void message(CanMessage msg)
	{
		super.message(msg);
		if(msg.getId() != id)
			return;
		if(msg.getData8(0) == 0)
			setDetail("Off");
		else
			setDetail("On");
	}
}
