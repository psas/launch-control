import java.io.*;
import javax.sound.sampled.*;
// import javax.media.*;

public class SoundAction implements SchedulableAction
{
	public void dispatch(String cmd) throws Exception
	{
		playSound(cmd);
	}

	public static void playSound(String file) throws Exception
	{
// 		Manager.createRealizedPlayer(new File(file).toURL()).start();
		AudioInputStream stream = AudioSystem.getAudioInputStream(new File(file));
		DataLine.Info info = new DataLine.Info(Clip.class, stream.getFormat());
		Clip line = (Clip)AudioSystem.getLine(info);
		line.open(stream);
		line.start();
	}
}
