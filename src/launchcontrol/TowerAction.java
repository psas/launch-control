package launchcontrol;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;
import java.util.*;
import javax.swing.*;

public class TowerAction implements SchedulableAction
{
	public interface TowerListener
	{
		public void towerStatus(String status);
	}

	private static final Hashtable cmds = new Hashtable();
	static {
		cmds.put("strobe", "st");
		cmds.put("siren", "si");
		cmds.put("igniter", "ig");
		cmds.put("power", "a1");
	}
	private static final String statusbits[] =
	{ "ready", null, "strobe", "siren", null, "igniter", "power", null, null, null };

	private LinkedList cmdqueue = new LinkedList();
	private Object cts = new Object(); // clear-to-send
	private TowerListener listener = null;
	private boolean rocketready = false;

	public TowerAction(String host, int port) throws IOException
	{
		Socket s = new Socket(host, port);
		new ReaderThread(s.getInputStream()).start();
		new WriterThread(s.getOutputStream()).start();
	}

	public void addTowerListener(TowerListener l)
	{
		listener = l;
	}

	public void dispatch(String cmd)
	{
		cmd = cmd.trim();
		String remote;

		if(cmd.equals("status"))
			remote = "sta";
		else if(cmd.equals("launch"))
			if(rocketready)
				remote = "1ig";
			else
				return; // don't launch unless rocket is ready
		else
		{
			int lastidx = cmd.length() - 1;
			remote = cmd.charAt(lastidx) +
				(String)cmds.get(cmd.substring(0, lastidx).trim());
		}

		// queue the command
		synchronized(cmdqueue)
		{
			cmdqueue.add("at" + remote);
			cmdqueue.notify();
		}
	}

	private class ReaderThread extends Thread
	{
		InputStream in;

		public ReaderThread(InputStream s)
		{
			in = s;
		}

		public void run()
		{
			BufferedReader r = new BufferedReader(new InputStreamReader(in));
			String line;
			try {
				while(true)
				{
					if((line = r.readLine()) == null)
						break;
					if(line.startsWith("STATUS "))
					{
						rocketready = (line.charAt(7) == '1');
						if(listener != null)
							listener.towerStatus(line.substring(7));
					}
					if(line.startsWith("STATUS ") || line.startsWith("OK "))
						synchronized(cts)
						{
							cts.notify();
						}
				}
			} catch(IOException e) {
				e.printStackTrace();
			}
		}
	}

	private class WriterThread extends Thread
	{
		OutputStream out;

		public WriterThread(OutputStream s)
		{
			out = s;
		}

		public void run()
		{
			boolean sentsta = false;
			try {
				while(true)
				{
					String cmd = "atsta";
					synchronized(cmdqueue)
					{
						if(cmdqueue.size() == 0 && sentsta)
							try {
								cmdqueue.wait();
							} catch(InterruptedException intr) {
								continue; // try again
							}
						if(cmdqueue.size() != 0)
							cmd = (String)cmdqueue.removeFirst();
					}
					// execute the command
					out.write(cmd.getBytes());
					sentsta = cmd.equals("atsta");
					synchronized(cts)
					{
						try {
							cts.wait();
						} catch(InterruptedException intr) {
							// ignore
						}
					}
				}
			} catch(IOException io) {
				io.printStackTrace();
			}
		}
	}

	public Component getControls()
	{
		return new CommandPanel();
	}

	private class CommandPanel extends Box
		implements TowerListener, ActionListener
	{
		private Component btns[] = new Component[statusbits.length];

		public CommandPanel()
		{
			super(BoxLayout.X_AXIS);

			JLabel ready = new JLabel("not " + statusbits[0]);
			add(ready);
			btns[0] = ready;

			for(int i = /*skip RR*/1; i < statusbits.length; ++i)
			{
				if(statusbits[i] == null)
					continue;
				JToggleButton b = new JToggleButton(statusbits[i]);
				b.setActionCommand(statusbits[i]);
				b.addActionListener(this);
				add(b);
				btns[i] = b;
			}

			addTowerListener(this);
		}

		public void actionPerformed(ActionEvent event)
		{
			AbstractButton source = (AbstractButton)event.getSource();
			dispatch(event.getActionCommand() + (source.isSelected() ? "1" : "0"));
		}

		public void towerStatus(final String status)
		{
			SwingUtilities.invokeLater(new Runnable() {
				public void run()
				{
					for(int i = 0; i < status.length(); ++i)
					{
						if(btns[i] == null)
							continue;
						if(btns[i] instanceof AbstractButton)
							((AbstractButton)btns[i]).setSelected(status.charAt(i) == '1');
						else if(btns[i] instanceof JLabel)
							((JLabel)btns[i]).setText(
								(status.charAt(i) == '1' ? "is " : "not ") + statusbits[i]);
					}
				}
			});
		}
	}
}
