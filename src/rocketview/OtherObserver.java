package rocketview;

import javax.swing.*;
import javax.swing.border.*;

import widgets.*;
import cansocket.*;

public class OtherObserver extends JPanel
{
	public OtherObserver(CanDispatch dispatch, FCStateLabel stateLabel)
	{
		setBorder(new TitledBorder("Other"));
		setLayout(new GridBoxLayout());

		add(stateLabel);

		StateGrid grid = StateGrid.getStateGrid();
		grid.setColumns(1);
		add(grid);
	}
}
