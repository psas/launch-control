package cansocket;

import java.io.*;

public class PressureBaseMessage extends NetMessage
{
	public static final short fifo_tag = FMT_PRESSURE_BASE;
	public static final short MSG_SIZE = 
		8 /* pressure */ + 8 /* temp */ + 8 /* altitude */;
	public final double pressure, temperature, altitude; 

	public PressureBaseMessage(double pressure, double temp, double altitude)
	{
		this.pressure = pressure;
		this.temperature = temp;
		this.altitude = altitude;
	}

	public PressureBaseMessage(DataInputStream dis) throws IOException
	{
		this.pressure = dis.readDouble();
		this.temperature = dis.readDouble();
		this.altitude = dis.readDouble();
	}

	public byte[] toByteArray()
	{
		ByteArrayOutputStream bos = new ByteArrayOutputStream(HEADER_SIZE+MSG_SIZE);
		DataOutputStream dos = new DataOutputStream(bos);
		putMessage(dos);
		return bos.toByteArray();
	}
	
	public void putMessage(DataOutputStream dos)
	{
		try
		{
			putHeader(dos, MSG_SIZE, fifo_tag);
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

