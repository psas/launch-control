package widgets;

import cansocket.*;

import java.util.*;
import javax.swing.*;

public class RocketState extends JPanel implements Observer
{
	protected final FCStateLabel stateLabel = new FCStateLabel();
	protected final CanDispatch dispatch = new CanDispatch();

	public RocketState()
	{
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		dispatch.add(stateLabel);
		add(stateLabel);
		add(new JSeparator());
		add(new StateGrid(dispatch));
	}

	public void addLinkStateListener(LinkStateListener listener)
	{
		stateLabel.addLinkStateListener(listener);
	}

	public void update(Observable o, Object arg)
	{
		if (!(arg instanceof CanMessage))
			return;

		update((CanMessage) arg);
	}

	public void update(CanMessage msg)
	{
		dispatch.update(msg);
	}
}
