package widgets;

import cansocket.*;

import java.util.*;
import javax.swing.*;

public class RocketState extends JPanel implements Observer, CanObserver
{
	protected final CanDispatch dispatch = new CanDispatch();
	protected final FCStateLabel stateLabel = new FCStateLabel(dispatch);

	public RocketState()
	{
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
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

		message((CanMessage) arg);
	}

	public void message(CanMessage msg)
	{
		dispatch.update(msg);
	}
}
