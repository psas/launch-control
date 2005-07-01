package widgets;

import cansocket.*;

import java.lang.reflect.*;

public class NodeFlagLabel extends NodeStateLabel
{
	protected String name;
	protected final int id;
	protected String value = "-";

	public NodeFlagLabel(String name, int bit)
		throws NoSuchFieldException, IllegalAccessException
	{
		super(name, bit);
		this.name = name;
		int idx = name.indexOf('_');
		String prefix = name.substring(0, idx);
		String suffix = name.substring(idx);
		id = CanBusIDs.class.getField(prefix + "_REPORT" + suffix).getInt(null);
		setText();
	}

	public void message(CanMessage msg)
	{
		super.message(msg);
		if(msg.getId() != id)
			return;
		if(msg.getData8(0) == 0)
			value = "Off";
		else
			value = "On";
		setText();
	}

	public void setText(String name)
	{
		this.name = name;
		setText();
	}

	protected void setText()
	{
		super.setText(name + ": " + value);
	}
}
