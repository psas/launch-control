package widgets;

import java.awt.*;
import javax.swing.*;

public class StateLabel extends JLabel
{
	protected final Color bg;

	protected Color fg;
	protected boolean known;

	public StateLabel(String name)
	{
		super(name);
		bg = getBackground();
		setOpaque(true);

		setState(false);
		setKnown(false);
	}

	protected void setState(boolean state)
	{
		if(state)
			fg = Color.GREEN;
		else
			fg = Color.RED;
		update();
	}

	protected void setKnown(boolean known)
	{
		this.known = known;
		update();
	}

	private void update()
	{
		if(known)
			setBackground(fg);
		else
			setBackground(bg);
		setBorder(BorderFactory.createLineBorder(fg));
	}
}
