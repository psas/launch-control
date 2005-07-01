package widgets;

import cansocket.*;

import java.lang.reflect.*;
import java.util.*;

public class NodeModeLabel extends NodeStateLabel
{
	protected final String name;
	protected final int id;

	/** Map from mode numbers to names. */
	protected final Map modes = new HashMap();

	protected Byte mode;

	public NodeModeLabel(String name, int bit)
		throws NoSuchFieldException, IllegalAccessException
	{
		super(name, bit);
		this.name = name;
		id = CanBusIDs.class.getField(name + "_REPORT_MODE").getInt(null);

		/* Find all the legal modes for the current node. */
		Field[] fields = CanBusIDs.class.getFields();
		final String prefix = name + "_MODE_";
		for(int i = 0; i < fields.length; ++i)
		{
			String fname = fields[i].getName();
			if(!fname.startsWith(prefix))
				continue;
			byte code = (byte) fields[i].getInt(null);
			modes.put(new Byte(code), fname.substring(prefix.length()));
		}

		setText();
	}

	public void message(CanMessage msg)
	{
		super.message(msg);
		if(msg.getId() != id)
			return;
		mode = new Byte(msg.getData8(0));
		setText();
	}

	protected void setText()
	{
		StringBuffer b = new StringBuffer(name).append(": ");
		if(mode == null)
			b.append("-");
		else if(modes.containsKey(mode))
			b.append(modes.get(mode));
		else
			b.append("0x").append(Integer.toHexString(mode.intValue() & 0xff));
		setText(b.toString());
	}
}
