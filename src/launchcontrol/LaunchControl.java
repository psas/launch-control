package launchcontrol;

import cansocket.*;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;
import java.util.*;
import javax.swing.*;

public class LaunchControl extends JFrame
{
        private static JLabel statusLabel = new JLabel("nothing to see here");

        private LaunchControl() throws IOException
        {
                super("LaunchControl");
                Container content = getContentPane();
                content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));

                Properties conf = new Properties();
                conf.load(new FileInputStream("main.conf"));
                Scheduler s = new Scheduler(new File(conf.getProperty("schedule")).toURL());
                content.add(s.getControls(
                        conf.getProperty("startSound"),
                        conf.getProperty("abortSound")
                ));

                String towerHost = (String) conf.getProperty("towerHost");
                int towerPort = Integer.
                    parseInt(conf.getProperty("towerPort"));
                int localPort = Integer.
                    parseInt(conf.getProperty("localTowerPort"));
                                                 

                CanAction tower = new CanAction(
		    Integer.parseInt(conf.getProperty("localTowerPort")),
                    conf.getProperty("towerHost"),
                    Integer.parseInt(conf.getProperty("towerPort")),
		    "tcp",
		    // ignore parameter "localTowerPort" for tcp connections
		    null
                );
		/*
                TowerAction tower = new TowerAction(
                        conf.getProperty("towerHost"),
                        Integer.parseInt(conf.getProperty("towerPort"))
                );
		*/
                //content.add(tower.getControls());

                Scheduler.addSchedulableAction("tower", tower);


                CanListener towerListener = new UDPCanListener(localPort);

                
                String rocketHost = (String) conf.getProperty("rocketHost");
                int rocketPort = Integer.
                    parseInt(conf.getProperty("rocketPort"));
                int localPort2 = Integer.
                    parseInt(conf.getProperty("localRocketPort"));

                CanAction rocket = new CanAction(
		    //Integer.parseInt(conf.getProperty("localRocketPort")),
                    6666,
                    conf.getProperty("rocketHost"),
                    Integer.parseInt(conf.getProperty("rocketPort")),
		    "udp",
		    null
                );

		/*                RocketAction rocket = new RocketAction(
                       conf.getProperty("rocketHost"),
                       Integer.
                    parseInt(conf.getProperty("rocketPort"))
		    );*/

                Scheduler.addSchedulableAction("rocket", rocket);

                CanListener rocketListener = new UDPCanListener(localPort2);

                content.add(new StatusPanel(towerListener, rocketListener));
                Container statusBar = new JPanel();
                statusBar.setLayout(new BorderLayout());
                statusLabel.setBorder(BorderFactory.createLoweredBevelBorder());
                statusBar.add(statusLabel, BorderLayout.CENTER);
                content.add(statusBar);

                setDefaultCloseOperation(EXIT_ON_CLOSE);

                pack();
        }

        public static void setStatus(final String msg)
        {
                SwingUtilities.invokeLater(new Runnable() {
                        public void run()
                        {
                                statusLabel.setText(msg);
                        }
                });
        }

        public static void main(String args[]) throws IOException
        {
                new LaunchControl().setVisible(true);
        }
}
