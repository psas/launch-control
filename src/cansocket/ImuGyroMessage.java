package cansocket;

import java.io.*;

public class ImuGyroMessage extends NetMessage
{
	public static final short fifo_tag = FMT_IMU_GYRO;
	public final double Pdot, Ydot, Rdot; 

	public ImuGyroMessage(DataInputStream dis) throws IOException
	{
		Pdot = dis.readDouble();
		Ydot = dis.readDouble();
		Rdot = dis.readDouble();
	}

	
	public void putMessage(DataOutputStream dos)
	{
		try
		{
			dos.writeDouble(Pdot);
			dos.writeDouble(Ydot);
			dos.writeDouble(Rdot);
		} catch(IOException e) {
			// never happens
		}
	}

	/** Return a string representation of this Imu Gyro Message.
	 * The returned string should include all data this object holds. */
	public String toString()
	{
		return "Pitch:" + Double.toString(Pdot) + 
			" Yaw:" + Double.toString(Ydot) +
			" Roll:" + Double.toString(Rdot);
	}
}

