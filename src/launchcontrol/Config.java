/* Copyright 2005 Ian Osgood, Jamey Sharp
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

import java.io.*;
import java.util.*;

public class Config
{
	protected static final String file = "main.conf";
	protected static Properties conf = new Properties();
	static {
		try
		{
			conf.load(new FileInputStream(file));
		}
		catch(Exception e)
		{
			System.err.println("warning: failed to open configuration " + file);
			e.printStackTrace();
		}
	}
	
	public static String getString(String name)
	{
		return conf.getProperty(name);
	}
	
	public static String getString(String name, String def)
	{
		return conf.getProperty(name, def);
	}

	public static Integer getInt(String name)
		throws NumberFormatException
	{
		String val = conf.getProperty(name);
		if(val == null)
			return null;
		return new Integer(val);
	}

	public static Integer getInt(String name, Integer def)
		throws NumberFormatException
	{
		String val = conf.getProperty(name);
		if(val == null)
			return def;
		return new Integer(val);
	}

	public static int getInt(String name, int def)
		throws NumberFormatException
	{
		String val = conf.getProperty(name);
		if(val == null)
			return def;
		return Integer.parseInt(val);
	}

	public static Double getInt(String name, Double def)
		throws NumberFormatException
	{
		String val = conf.getProperty(name);
		if(val == null)
			return def;
		return new Double(val);
	}

	public static double getDouble(String name, double def)
		throws NumberFormatException
	{
		String val = conf.getProperty(name);
		if (val == null)
			return def;
		return Double.parseDouble(val);
	}

}
