package rocketview.layout;

import java.awt.FlowLayout;
import java.io.File;
import java.io.IOException;

import javax.swing.*;
import javax.swing.border.TitledBorder;

import cansocket.*;
import widgets.*;

/**
 * Panel containing rocketState widget
 */
public class FCStatePanel extends JPanel
{

	/** constructor */
	public FCStatePanel () 
	{
		this.setBorder(new TitledBorder("FC State"));
		this.setLayout(new FlowLayout( FlowLayout.LEFT, 5, 0 /* no vert spacing */));		
		this.add(new RocketState());
	}
	
	/**
	 * FCStatePanel test driver
	 * @throws IOException if canMessage logging fails
	 */
	public static void main(String[] args) throws IOException
	{
		//canMessage log
        StringBuffer tmpDir = new StringBuffer(System.getProperty("java.io.tmpdir"));
        String log = tmpDir.append(File.separatorChar).append("fcs.log").toString();
        
        CanSocket socket = new LogCanSocket(new UDPCanSocket(), log); 
        CanListener listener = new CanListener(socket);
        
		JFrame frame = new JFrame("FCstatePanel test driver");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.add(new FCStatePanel());
		frame.pack();
	    frame.setVisible(true);
	}
}
