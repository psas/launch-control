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
}
