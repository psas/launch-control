package widgets;

import cansocket.*;

import java.lang.reflect.*;

public class NodeFlagLabel extends NodeStateLabel
{
	protected final String name;
	protected final int id;

	public NodeFlagLabel(String name, int bit)
		throws NoSuchFieldException, IllegalAccessException
	{
		super(name + ": -", bit);
		this.name = name;
		int idx = name.indexOf('_');
		String prefix = name.substring(0, idx);
		String suffix = name.substring(idx);
		id = CanBusIDs.class.getField(prefix + "_REPORT" + suffix).getInt(null);
	}

	public void message(CanMessage msg)
	{
		super.message(msg);
		if(msg.getId() != id)
			return;
		if(msg.getData8(0) == 0)
			setText(name + ": Off");
		else
			setText(name + ": On");
	}
}
