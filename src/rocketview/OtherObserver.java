package rocketview;

import java.text.*;
import javax.swing.*;
import javax.swing.border.*;

import widgets.*;
import cansocket.*;

public class OtherObserver extends JPanel implements CanObserver
{
	protected static final DecimalFormat nf = new DecimalFormat("###0.000MB");
	protected final NameDetailLabel logAvail;

	public OtherObserver(CanDispatch dispatch)
	{
		setBorder(new TitledBorder("Other"));
		setLayout(new GridBoxLayout());

		dispatch.add(this);

		add(new FCStateLabel(dispatch));

		logAvail = StateGrid.getLabel("LOG_AVAIL");
		logAvail.setDetail("?MB");
		add(logAvail);

		StateGrid grid = StateGrid.getStateGrid();
		grid.setColumns(1);
		add(grid);
	}

	public void message(CanMessage msg)
	{
		switch(msg.getId())
		{
			case CanBusIDs.FC_REPORT_LOG_AVAIL:
				logAvail.setDetail(nf.format(msg.getData32(0) / 1024.0));
				break;
		}
	}
}
