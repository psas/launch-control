package rocketview;

import cansocket.*;

import java.awt.Font;
import java.util.*;
import javax.swing.*;

/*----------------------------------------------------------------
 * Prints all message to the message text box
 */
class TextObserver extends JTextArea implements Observer
{

    public TextObserver() {

	// construct a JTextArea
	super( "-- no messages from rocket yet --" + "\n",
		30, 40 ); // row, column
	this.setLineWrap( true );
	this.setFont( new Font( "Monospaced", Font.PLAIN, 10 ));

	// System.out.println ("textObserver constructor" );
    }

    public void update(Observable o, Object arg)
    {
	CanMessage msg = (CanMessage) arg;


	StringBuffer buf = new StringBuffer( msg.toString() );
	String str = new String( buf.toString() + "\n" );
	// System.out.print ("text got a message: " + str );

	this.append( str );
    }
}
