package widgets;

import java.awt.GridLayout;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.ImageIcon;

/** A grid of leds displaying info about different elements, 
 * which are nodes in our case, and each of which is labeled. */
public class StateGrid extends JPanel
{
	// node states stored as bits
	protected byte[] states;
	// names of node states
	/* XXX TODO
	protected static String[] names = {}
		" get these from nodes.h 
		*/
	protected ImageIcon greenled = new ImageIcon(ClassLoader.getSystemResource("widgets/greenled.png"));
	protected ImageIcon redled = new ImageIcon(ClassLoader.getSystemResource("widgets/redled.png"));


	public StateGrid() {
		setLayout(new GridLayout(0,4)); // four rows

		// initialize state array to 0 
		states = new byte[8]; 
		/*
		for (int i = 0; i < 8; ++i) {
			states[i] = 0;
			// IDEA: we want to have things be greyed by default,
			// well we have 8 bytes and we only use like 4 so
			// maybe we could store 32 bits for whether things
			// are interesting or not.
		} */

		// create GUI
		draw();
	}



	/** Set state byte array and update LEDs.
	 * @param newStates 8 byte data from FC_REPORT_NODE_STATUS
	 * messages. */
	public void setStates(byte[] newStates)
	{
		redraw(states, newStates);
		states = newStates;
	}



	/** return specified bit of data.
	 * This assumes by bit 1 you mean greatest sig bit, 
	 * and by bit 8 you mean LSB. 
	 * ?? should it return boolean or number? */
	protected int getBit(byte data, int bit) 
	{
		return data & (0x80 >> bit);
	}



	/** Set the icon for the given element.
	 * @param element: the element number n for the component,
	 * note the the component must have been the n'th component
	 * added to this StateGrid. 
	 * @param isGood: if the element is in a good state, 
	 * if true the LED will be set green, else red */
	protected void setElementIcon(int element, boolean isGood) 
	{
		JLabel gridElement = (JLabel) getComponents()[element];
		setElementIcon(gridElement, isGood);
	}

	protected void setElementIcon(JLabel element, boolean isGood)
	{
		if (isGood) 
			element.setIcon(greenled);
		else 
			element.setIcon(redled);
	}

	
	
	
	// how to update?  Options:
	// 1) clear grid of state leds/names,
	// and recreate all of them.
	protected void draw() 
	{
		removeAll(); 
		String fakedesc = "Thing ";
		for (short i = 0; i < 32; ++i) {
			JLabel gridEntry = new JLabel(fakedesc + i);
			gridEntry.setIcon(redled); //XXX: change to GREY
			add(gridEntry);
			//setElementIcon(gridEntry, 0);
		}
	}
	


	// 2) assuming we can access element X of the
	// grid, we only change an element X of the grid 
	// if bit X of the state has changed.
	protected void redraw(byte[] oldStates, byte[] newStates)
	{
		//compare state to oldState 
		//and only change elements which have changed
		for (short byteIndex = 0; byteIndex < 4; ++byteIndex) {
			for (short bit = 0; bit < 8; ++bit) {
				int newBit = getBit(newStates[byteIndex], bit);
				if (getBit(oldStates[byteIndex], bit) != newBit)
					setElementIcon(byteIndex * 8 + bit, newBit == 1);
			}
		}
	}
}
