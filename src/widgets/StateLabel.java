/* Copyright 2005 Jamey Sharp, Travis Spencer, Max Ehrenfreund
 * 
 * This program is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by the Free
 * Software Foundation; either version 2 of the License, or (at your option)
 * any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for
 * more details.
 *
 * Portland State Aerospace Society (PSAS) is a student branch chapter of the
 * Institute of Electrical and Electronics Engineers Aerospace and Electronics
 * Systems Society. You can reach PSAS at info@psas.pdx.edu.  See also
 * http://psas.pdx.edu/
 */
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
