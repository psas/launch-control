// Class CanAction
 
//  By Karl Hallowell
//  for the Portland State Aerospace Society
//  June 26, 2003
 
 
// This class sends TCP or UDP CAN messages to a port via the appropriate
// CanSocket subclass.


package launchcontrol;

import cansocket.*;

import java.io.*;
import java.net.*;
import java.util.*;

public class CanAction implements SchedulableAction {

    protected CanSocket sock=null;
    protected String protocol = null;
    protected Hashtable cmdDict = null;

    public CanAction(int localPort, String remAddr, int remPort,
                     String aProtocol, Hashtable aCmdDict)
    throws IOException {
        // aside from here, it doesn't matter what protocol is used.

        // assert that all elements of aCmdDict are String, CanMessage object
        // pairs.
        // TODO: should I check cmdDict to verify that this is the case?
        // might prevent runtime errors later in the dispatch() method.

        if ("tcp".equalsIgnoreCase(aProtocol)) {
            sock = new TCPCanSocket(remAddr, remPort);
            protocol = aProtocol;
            cmdDict = aCmdDict;
        } else if ("udp".equalsIgnoreCase(aProtocol)) {
            sock = new UDPCanSocket(localPort, remAddr, remPort);
            protocol = aProtocol;
            cmdDict = aCmdDict;
        } else {
            throw new IOException ("Protocol " + protocol + " is not supported.");
        }
    }

    /*
     * dispatch() required by SchedulableAction interface. 
     */

    public void dispatch(String cmd) throws Exception {
        // Look up cmd in cmdTable. Then fire off appropriate CanMessage.

        CanMessage myCanMsg = null;

        // no processing of String cmd. Please clean it up before you pass it
        // here.
        
        myCanMsg = (CanMessage) cmdDict.get(cmd);
        // This is where the TODO about checking the cmdDict matters. If my
        // value object isn't castable as a CanMessage, I'll generate a
        // runtime exception.

        if (myCanMsg == null) {
            throw new IOException("Command \"" + cmd + "\" is not found.");
        }

        // fire this sucker off.
        // not sure how to retimestamp the message.

        sock.write(myCanMsg);
        sock.flush();
    }
}

