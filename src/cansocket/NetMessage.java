package cansocket;

import java.io.*;

public abstract class NetMessage
{
	/* NEW PROTOCOL */
	/* byte 0-1:   protocol version = 2
	   byte 2-3:   size of packet
	   byte 4-5:   message type
	   remainder: type specific payload
	*/
	public static final int HEADER_SIZE               = 2 + 2 + 2;
	public static final int MAX_MSG_SIZE              = HEADER_SIZE + CanMessage.MSG_SIZE;  // later: use max(...)

	public static final short FC_PROT_VER             = 2;

	public static final short FMT_CAN        = 0;
	// 1 is timer message (FC only)
	public static final short FMT_GPS_ORIGIN = 1; // message from ground only
	public static final short FMT_PRESSURE_BASE   = 2; // message from ground only

	public static final short FMT_IMU_ACCEL = 3;
	public static final short FMT_IMU_GYRO = 4;
	public static final short FMT_PRESSURE_DATA = 5;
	
	/* abstract messages */
	public byte[] toByteArray() { return new byte[MAX_MSG_SIZE]; }
	public abstract void putMessage(DataOutputStream dos);

	/* factory */
	static NetMessage newNetMessage(byte packet[]) throws IOException
	{
		DataInputStream s = new DataInputStream(new ByteArrayInputStream(packet));
		return newNetMessage(s);
	}
	static NetMessage newNetMessage(DataInputStream s) throws IOException
	{
		short version = s.readShort();  // assert version == FC_PROT_VER
		short size    = s.readShort();
		short type    = s.readShort();
		
		switch (type)
		{
			case FMT_CAN:
				return new CanMessage(s);
			case FMT_IMU_ACCEL:
				return new ImuAccelMessage(s);
			case FMT_IMU_GYRO:
				return new ImuGyroMessage(s);
			case FMT_PRESSURE_DATA:
				return new PressureDataMessage(s);

			// The following two will only be sent to the rocket, 
			// so they can be implemented when we need to do that.
			case FMT_GPS_ORIGIN:
			{
				double lat  = s.readDouble();
				double lng  = s.readDouble();
				double ht   = s.readDouble();
				// FIXME: return new GPSMessage(s);
			}
			break;
			case FMT_PRESSURE_BASE:
			{
				double press = s.readDouble();
				double temp  = s.readDouble();
				// FIXME: return new PressureBaseMessage(s);
			}
			break;
		}
		return null;		// FIXME: return new NullMessage;
	}
}
