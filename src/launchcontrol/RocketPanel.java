package launchcontrol;

import cansocket.*;
import widgets.*;

public class RocketPanel extends RocketState
{
	public RocketPanel(CanSocket socket, LaunchControl parent)
	{
		addLinkStateListener(parent);
		CanDispatch dispatch = new CanDispatch(socket);
		dispatch.add(this);
		new Thread(dispatch).start();
	}
}
