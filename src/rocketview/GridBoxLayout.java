package rocketview;

import java.awt.*;

public class GridBoxLayout extends GridBagLayout
{
	public GridBoxLayout()
	{
		defaultConstraints = new GridBagConstraints();
		defaultConstraints.gridwidth = GridBagConstraints.REMAINDER;
		defaultConstraints.weightx = 1.0;
		defaultConstraints.fill = GridBagConstraints.HORIZONTAL;
	}
}
