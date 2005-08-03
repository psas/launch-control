package rocketview;

import javax.swing.*;
import javax.swing.border.*;

import widgets.*;
import cansocket.*;

public class OtherObserver extends JPanel
{
	public OtherObserver(CanDispatch dispatch)
	{
		setBorder(new TitledBorder("Other"));
		setLayout(new GridBoxLayout());

		add(new FCStateLabel(dispatch));

		StateGrid grid = StateGrid.getStateGrid();
		grid.setColumns(1);
		add(grid);
	}
}
