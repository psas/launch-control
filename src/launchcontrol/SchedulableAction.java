package launchcontrol;

public interface SchedulableAction
{
	public void dispatch(String cmd) throws Exception;
}
