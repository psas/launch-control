package rocketview;

import cansocket.*;

import java.util.*;
import javax.swing.*;

/*----------------------------------------------------------------
 * Handles message id PressValue
 * Updates the barometric altitude box with altitude
 *
 * expects one 4-byte integer interpreted as
 * display = 44331.514 - Math.pow(18411.8956 * value, 0.1902632)
 *
 * display: bar.alt: xxxm
 */
class PressureHeightObserver extends JLabel implements Observer
{
    protected float pressHeight = Float.MAX_VALUE;

    public PressureHeightObserver() {
	this.setText( "-- no pressure data from rocket yet --" );
    }

    public void update(Observable o, Object arg)
    {
	CanMessage msg = (CanMessage) arg;
	if( msg.getId11() != CanBusIDs.PressValue)
	    return;

	// get raw value out of packet
	short rawValue = msg.getData16(0);
	// System.out.println( "pressvalue " + rawValue );
	StringBuffer buf = new StringBuffer( "pressure: " );
	buf.append( rawValue );

	// calculate height
	buf.append( "  height: " );
	pressHeight = toHeight( rawValue );
	if(pressHeight < Float.MAX_VALUE)
	    buf.append( pressHeight ).append( "m " );
	else
	    buf.append( "*bad value*" );

	setText( buf.toString() );
    }

    // this is where the rubbber really meets the sky
    protected float toHeight(short pressure)
    {
	return (float) (44331.514 - Math.pow(18411.8956 * pressure, 0.1902632));
    }
}
