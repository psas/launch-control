// CountdownPanel.java

// Started by Karl Hallowell
// August 5, 2003

// Takes Jamey's CountdownPanel out of Scheduler.java and sticks it here.

package launchcontrol;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.text.*;

public class CountdownPanel extends JPanel
    implements ScheduleListener, ActionListener {

    private final static String startMsg = "Start Countdown";
    private final static String stopMsg = "Abort Countdown";
    private final static String stoppedMsg = "Countdown stopped";
    private final DecimalFormat fmt = new DecimalFormat("T+0.0;T-0.0");

    private JButton button = new JButton();
    private JLabel clock = new JLabel();
    private String startSound;
    private String abortSound;
    private Scheduler myScheduler = null;


    public CountdownPanel(String start, String abort, Scheduler aScheduler) {
	startSound = start;
	abortSound = abort;
	
	setLayout(new BorderLayout());
	add(button, BorderLayout.WEST);
	add(clock, BorderLayout.CENTER);
	ended(); // reset the button and label

	myScheduler = aScheduler;
	myScheduler.addScheduleListener(this, 100);
	button.addActionListener(this);
    }

    public void actionPerformed(ActionEvent event) {
	try {
	    if(event.getActionCommand().equals("start")) {
		if(JOptionPane.
		   showConfirmDialog(this,
				     "Are you sure you want to start the countdown?", "Proceed?",
				     JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION)
		    myScheduler.startCountdown();
	    } else if(event.getActionCommand().equals("abort"))
		myScheduler.abortCountdown();
	} catch(Exception e) {
	    e.printStackTrace();
	}
    }

    public void started() {
	SwingUtilities.invokeLater(new Runnable() {
		public void run() {
		    button.setText(stopMsg);
		    button.setActionCommand("abort");
		}
	    });
	LaunchControl.setStatus("Countdown started");
	try {
	    SoundAction.playSound(startSound);
	} catch(Exception e) {
	    // ignore
	}
    }

    public void disableAbort() {
	SwingUtilities.invokeLater(new Runnable() {
		public void run() {
		    button.setEnabled(false);
		}
	    });
    }

    public void aborted() {
	disableAbort();
	LaunchControl.setStatus("Countdown aborted: cleaning up");
	try {
	    SoundAction.playSound(abortSound);
	} catch(Exception e) {
	    // ignore
	}
    }
	
    public void ended() {
	SwingUtilities.invokeLater(new Runnable() {
		public void run() {
		    button.setText(startMsg);
		    button.setActionCommand("start");
		    button.setEnabled(true);
		    clock.setText("");
		}
	    });
	LaunchControl.setStatus(stoppedMsg);
    }
	
    public void time(final long millis) {
	SwingUtilities.invokeLater(new Runnable() {
		public void run() {
		    clock.setText(fmt.format((float)millis / 1000.0));
		}
	    });
    }
}
