package cansocket;

import java.io.*;

public class PressureBaseMessage extends NetMessage
{
	public static final short fifo_tag = FMT_PRESSURE_BASE;
	public final double pressure, temperature, altitude; 

	public PressureBaseMessage(DataInputStream dis) throws IOException
	{
		pressure = dis.readDouble();
		temperature = dis.readDouble();
		altitude = dis.readDouble();
	}

	
	public void putMessage(DataOutputStream dos)
	{
		try
		{
		dos.writeDouble(pressure);
		dos.writeDouble(temperature);
		dos.writeDouble(altitude);
		} catch(IOException e) {
			// never happens
		}
	}

	public String toString()
	{
		return "pressure base:" + Double.toString(pressure) + " temperature base:" + Double.toString(temperature) + "altitude base" + Double.toString(altitude);
	}
}

