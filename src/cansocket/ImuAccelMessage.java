package cansocket;

import java.io.*;

public class ImuAccelMessage extends NetMessage
{
	public static final short fifo_tag = FMT_IMU_ACCEL;
	public final double Ax, Ay, Az, Aq;

	public ImuAccelMessage(DataInputStream dis) throws IOException
	{
		Ax = dis.readDouble();
		Ay = dis.readDouble();
		Az = dis.readDouble();
		Aq = dis.readDouble();
	}

	
	public void putMessage(DataOutputStream dos)
	{
		try
		{
			dos.writeDouble(Ax);
			dos.writeDouble(Ay);
			dos.writeDouble(Az);
			dos.writeDouble(Aq);
		} catch(IOException e) {
			// never happens
		}
	}

	public String toString()
	{
		return "AX:" + Double.toString(Ax) + " AY:" + Double.toString(Ay) +
		" AZ:" + Double.toString(Az) + " AQ:" + Double.toString(Aq);
	}
}

