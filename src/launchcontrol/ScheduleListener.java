package launchcontrol;

public interface ScheduleListener
{
    public void started();
    public void disableAbort();
    public void aborted();
    public void ended();
    public void time(long millis);
}
