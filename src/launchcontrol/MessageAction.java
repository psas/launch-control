package launchcontrol;

/**
 * This action is intended for debugging only, as its output goes to a
 * terminal rather than the GUI.
 */
public class MessageAction implements SchedulableAction
{
	public void dispatch(String cmd) throws Exception
	{
		System.out.println(cmd);
	}
}
