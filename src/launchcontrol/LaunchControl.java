import java.io.*;
import java.net.*;
import java.text.*;
import java.util.*;

public class LaunchControl
{
	private static Properties conf = new Properties();
	public static void main(String args[]) throws Exception
	{
		conf.load(new FileInputStream("main.conf"));

		ScheduleListener l = new ScheduleListener() {
			private DecimalFormat fmt = new DecimalFormat("T+0.0;T-0.0");

			public void started()
			{
				try {
					SoundAction.playSound(conf.getProperty("startSound"));
				} catch(Exception e) {
					// ignore
				}
			}

			public void aborted()
			{
				try {
					SoundAction.playSound(conf.getProperty("abortSound"));
				} catch(Exception e) {
					// ignore
				}
			}

			public void ended()
			{
				System.out.println("Countdown end.");
				System.exit(0);
			}

			public void time(long millis)
			{
				System.out.println(fmt.format((float)millis / 1000.0));
			}
		};

		Scheduler s = new Scheduler(new File(conf.getProperty("schedule")).toURL());

		s.startCountdown(l, 1000);
	}
}
