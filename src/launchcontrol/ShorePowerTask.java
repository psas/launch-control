/* Copyright 2005 Jamey Sharp, Peter Welte
 * 
 * This program is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by the Free
 * Software Foundation; either version 2 of the License, or (at your option)
 * any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for
 * more details.
 *
 * Portland State Aerospace Society (PSAS) is a student branch chapter of the
 * Institute of Electrical and Electronics Engineers Aerospace and Electronics
 * Systems Society. You can reach PSAS at info@psas.pdx.edu.  See also
 * http://psas.pdx.edu/
 */
package launchcontrol;

import cansocket.*;

import java.util.TimerTask;


/** A task, which when run, will set the shore power to
 * a desired state. 
 * This can be used by any code that needs to schedule
 * the shore power to be set to a certain state at a given time;
 * to do that, use this class and java.util.Timer. 
 * 
 * Note that this class needs a socket connection to the 
 * launch tower, but it only writes to the socket and will
 * never read from it. */
public class ShorePowerTask extends TimerTask 
{
	protected boolean power;
	protected CanMessage powerOn;
	protected CanMessage powerOff;
	protected CanMessage requestMessage;
	protected CanSocket towerSocket; // tower communication socket

	/** Create a new ShorewPowerTask. 
	 * When the Task is run, a message will be sent to the tower
	 * telling it to set the shore power to power_state.
	 * @param towerSocket the connection to the launch tower.
	 * @param power_state the power state shore power should be set to. 
	 */
	public ShorePowerTask (boolean power_state, CanSocket towerSocket) 
	{
		this.power = power_state;
		this.towerSocket = towerSocket;
		short id = CanBusIDs.LTR_SET_SPOWER;
		byte onBody[] = { 1 };
		byte offBody[] = { 0 };
		byte[] blank = new byte[8];
		powerOn = new CanMessage(id, 0, onBody );
		powerOff = new CanMessage(id, 0, offBody);
		requestMessage = new CanMessage(CanBusIDs.LTR_GET_SPOWER, 0, blank);
	}

	public void run() 
	{
		CanMessage powerMessage;
		if (power)  // set shore power on
			powerMessage = powerOn;
		else  // set shore power off
			powerMessage = powerOff;
			
		if (towerSocket == null) 
		{
			System.out.println(
				"can't send power command (towerSocket null); connected?");
		} 
		else 
		{
			try {
				if (power) 
				{
					System.out.println("LC: ShorePowerTask->powerOn");
				} 
				else 
				{
					System.out.println("LC: ShorePowerTask->powerOff");
				}

				towerSocket.write(powerMessage);
				towerSocket.write(requestMessage);
				towerSocket.flush();
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
	}
}

