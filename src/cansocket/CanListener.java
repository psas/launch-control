/* CanListener.java
 * started June 20, 2003.
 *
 * based on Dave's rocketview.Dispatch. Karl
 *
 */

package cansocket;

import java.io.*;
import java.util.*;

class CanListener extends Observable implements Runnable
{
    public void run() {
        // This is a run time exception FWIW. TODO: make method abstract?

        throw new UnsupportedOperationException("CanListener.run() needs to be overloaded");
    }

    public void run(CanSocket sock) throws IOException
    {
	// TODO: Insert logging?

        CanMessage m;
        while ((m = sock.read()) != null)
            {
                setChanged();
                notifyObservers(m);
            }
    }
}
