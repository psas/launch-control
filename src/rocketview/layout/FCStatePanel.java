package rocketview.layout;

import java.awt.FlowLayout;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import widgets.*;

/**
 * Panel containing the flight computer state "lights"
 */
public class FCStatePanel extends JPanel
{
	
	public FCStatePanel () {
		
		this.setBorder(new TitledBorder("FC State"));
		this.setLayout(new FlowLayout( FlowLayout.LEFT, 5, 0 /* no vert spacing */));
		
		/** TODO: Group RocketState/StateGrid lights into separate
		 *  sections/panels IMU, GPS, APS, etc.*/
		this.add(new RocketState());
	}
	
	public static void main(String[] args)
	{
		System.out.println( "FCStatePanel test driver" );
		JFrame frame = new JFrame("FCstatePanel test driver");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.add(new FCStatePanel());
		frame.pack();
	    frame.setVisible(true);
	}
}
