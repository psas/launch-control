package rocketview;

import cansocket.*;

import java.awt.Font;
import java.lang.reflect.*;
import javax.swing.*;

/*----------------------------------------------------------------
 * Prints all message to the message text box
 */
class TextObserver extends JScrollPane implements CanObserver
{
	String msgSyms[];
	JTextArea text = new JTextArea(15, 40); // rows, columns

    protected int discardLength(int id)
    {
	return (id >>> 4) & 0xfff;
    }

    public TextObserver(CanDispatch dispatch) throws IllegalAccessException
    {
	dispatch.add(this);

	// initialize the map of CAN msg symbols
	int i, msg;
	msgSyms = new String[0x1000];
	Field fields[] = CanBusIDs.class.getFields();
	for (i=0; i<fields.length; i++)
	{
		msg = discardLength(fields[i].getInt(null));
		msgSyms[msg] = fields[i].getName();
	}

	// construct a JTextArea
	text.setLineWrap(true);
	text.setFont(new Font("Monospaced", Font.PLAIN, 10));
	setViewportView(text);
    }

    public void message(CanMessage msg)
    {
	int nid = msg.getId() & (0x1f << 11);
	switch(nid)
	{
		case CanBusIDs.FC_NID:
		case CanBusIDs.FC_IMU_NID:
		case CanBusIDs.FC_GPS_NID:
			return;
	}

	int verb = msg.getId() & ((0x3 << 8) | CanBusIDs.CID_REQUEST);
	switch(verb)
	{
		/* XXX: would like to filter CID_REPORT, but it's the
		 * same verb as CID_ERROR, so we can't. For the same
		 * reason, don't block CID_INFO or related. */
		case CanBusIDs.CID_ACTION_BC:
		case CanBusIDs.CID_TEST:
		case CanBusIDs.CID_SET:
		case CanBusIDs.CID_ACK:
		case CanBusIDs.CID_GET:
		case CanBusIDs.CID_DATA:
			return;
	}

	// filter out all id's that are handled elsewhere
	switch(msg.getId())
	{
		case CanBusIDs.REC_REPORT_MODE:
		case CanBusIDs.REC_REPORT_PYRO:
		case CanBusIDs.REC_REPORT_TIMER:
			return;
	}

	StringBuffer buf = new StringBuffer();
	if (msgSyms[discardLength(msg.getId())] != null)
		buf.append(msgSyms[discardLength(msg.getId())]).append(": ");
	buf.append(msg).append("\n");
	text.append(buf.toString());

	//Try to keep the scrollpane looking at the tail of the log
	final JScrollBar vertBar = getVerticalScrollBar();
	SwingUtilities.invokeLater(new Runnable() {
	  public void run() {
	    if (! vertBar.getValueIsAdjusting()) 
	      vertBar.setValue(vertBar.getMaximum());
	  }
	});
    }
}
