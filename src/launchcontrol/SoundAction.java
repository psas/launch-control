/* Copyright 2004 Jamey Sharp
 * 
 * This program is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by the Free
 * Software Foundation; either version 2 of the License, or (at your option)
 * any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for
 * more details.
 *
 * Portland State Aerospace Society (PSAS) is a student branch chapter of the
 * Institute of Electrical and Electronics Engineers Aerospace and Electronics
 * Systems Society. You can reach PSAS at info@psas.pdx.edu.  See also
 * http://psas.pdx.edu/
 */
package launchcontrol;

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
		AudioInputStream stream = AudioSystem.getAudioInputStream(ClassLoader.getSystemResource(file));
		DataLine.Info info = new DataLine.Info(Clip.class, stream.getFormat());
		Clip line = (Clip)AudioSystem.getLine(info);
		line.open(stream);
		line.start();
	}
}
