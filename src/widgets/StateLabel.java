package widgets;

import java.awt.*;
import javax.swing.*;

public class StateLabel extends NameDetailLabel
{
	protected final Color bg;

	protected Color fg;
	protected boolean known;
	protected boolean state;

	protected ImageIcon grayoff = new ImageIcon(ClassLoader.getSystemResource("widgets/grayoff.png"));
	protected ImageIcon redled = new ImageIcon(ClassLoader.getSystemResource("widgets/redled.png"));
	protected ImageIcon greenled = new ImageIcon(ClassLoader.getSystemResource("widgets/greenled.png"));

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
		this.state = state;
		if(this.state)
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

	//if known, "turn on" a green or red light.
	//if not known, turn off the light.
	private void update()
	{
		if(known)
		{
			setBackground(fg);
			if(state)
				setIcon(greenled);
			else
				setIcon(redled);
		}
		else
		{
			setBackground(bg);
			setIcon(grayoff);
		}
		setBorder(BorderFactory.createLineBorder(fg));
	}
}
