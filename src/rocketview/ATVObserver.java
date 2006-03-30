/* Copyright 2005 Jamey Sharp, Tim Welch
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
package rocketview;

import javax.swing.*;
import javax.swing.border.*;

import widgets.*;
import cansocket.*;

public class ATVObserver extends JPanel
{
	public ATVObserver(CanDispatch dispatch)
	{
		setLayout(new GridBoxLayout());

		add(StateGrid.getLabel("ATV"));
		add(StateGrid.getLabel("ATV_POWER_CAMERA"));
		add(StateGrid.getLabel("ATV_POWER_OVERLAY"));
		add(StateGrid.getLabel("ATV_POWER_TX"));
		add(StateGrid.getLabel("ATV_POWER_PA"));
	}
}
