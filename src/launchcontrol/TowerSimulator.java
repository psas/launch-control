import java.io.*;
import java.net.*;
import java.util.*;

// Not implemented: "atr" and "atg" commands, which set some LED to red or
// green, respectively.

// Parameters: If "rr" is the first argument, RocketReady will be always on;
// otherwise it's always off.

public class TowerSimulator
{
	private static InputStream in;
	private static OutputStream out;

	private static boolean state[] = new boolean[10];

	public static void main(String args[]) throws IOException
	{
		// Load settings and wait for connection
		Properties conf = new Properties();
		conf.load(new FileInputStream("main.conf"));
		Socket s = new ServerSocket(
			Integer.parseInt(conf.getProperty("towerPort")),
			/*backlog*/ 0
		).accept();
		in = s.getInputStream();
		out = s.getOutputStream();

		if(args.length > 0 && args[0].equals("rr"))
			state[0] = true;

		// Loop accepting commands
		int cmd[] = new int[3];
		while(true)
		{
			writeNewline();
			write("LTC>");
			error: {
				if(read() != 'a' || read() != 't') // read and check first two bytes
					break error;
				while((cmd[0] = read()) == ' ') // skip spaces after "at"
					/*empty*/ ;
				switch(cmd[0])
				{
					case '0': case '1':
						switch(cmd[1] = read())
						{
							case 'a': case 'i': case 'r': case 's': case 't':
								break;
							default:
								break error;
						}
						// It just so happens that the second character of the relay ID
						// is enough to uniquely identify a relay. This doesn't seem to
						// have been intentional, as Matt's assembly code doesn't take
						// advantage of it. It shortens my code, though.
						switch(cmd[2] = read())
						{
							case 'o': // must be "ro"
								if(cmd[1] != 'r')
									break error;
								setStatus(1, "Rollerons", cmd[0]);
								break;
							case 't': // must be "st"
								if(cmd[1] != 's')
									break error;
								setStatus(2, "Strobe", cmd[0]);
								break;
							case 'i': // must be "si"
								if(cmd[1] != 's')
									break error;
								setStatus(3, "Siren", cmd[0]);
								break;
							case '5': // must be "t5"
								if(cmd[1] != 't')
									break error;
								setStatus(4, "T-5 Signal", cmd[0]);
								break;
							case 'g': // must be "ig"
								if(cmd[1] != 'i')
									break error;
								setStatus(5, "Ignitor", cmd[0]);
								break;
							case '1': case '2': case '3': case '4': // "a1" through "a4"
								if(cmd[1] != 'a')
									break error;
								setStatus(6 + cmd[2] - '1', "AUX No. " + cmd[2], cmd[0]);
								break;
							default:
								break error;
						}
						break;
					case 's':
						if(read() != 't' || read() != 'a') // read and check last two bytes
							break error;
						writeStatus();
						break;
					case 13: // carriage return
						writeOK();
						break;
					default:
						break error;
				}
				continue; /* no error */
			} /* error */
			writeError();
		}
	}

	private static void writeNewline() throws IOException
	{
		write("\r\n");
	}

	private static void writeOK() throws IOException
	{
		writeNewline();
		write("OK ");
		// no following newline
	}

	private static void writeError() throws IOException
	{
		writeNewline();
		write("Error");
		writeNewline();
	}

	private static void writeStatus() throws IOException
	{
		writeNewline();
		write("STATUS ");
		for(int i = 0; i < state.length; ++i)
			write(state[i] ? '1' : '0');
		writeNewline();
	}

	private static void setStatus(int id, String name, int value)
		throws IOException
	{
		writeOK();
		state[id] = (value == '1');
		write(name);
		write(' ');
		write(state[id] ? "ON" : "OFF");
		if(id == /*T-5 Signal*/ 4)
			write("-LINE");
		write(' ');
		writeNewline();
	}

	private static int read() throws IOException
	{
		int ch = in.read();
		if(ch == /*EOF*/ -1)
			System.exit(0);
		write(ch);
		return Character.toLowerCase((char)ch);
	}

	private static void write(String text) throws IOException
	{
		byte b[] = text.getBytes();
		System.out.write(b);
		out.write(b);
	}

	private static void write(int ch) throws IOException
	{
		System.out.write(ch);
		out.write(ch);
	}
}
