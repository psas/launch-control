package cansocket;

import java.io.*;

public abstract class NetMessage
{
	/* factory */
	public static final int HEADER_SIZE               = 2 + 2 + 2;
	public static final int MAX_MSG_SIZE              = HEADER_SIZE + CanMessage.MSG_SIZE;  // later: use max(...)

	public static final short FC_PROT_VER             = 2;

	public static final short NET_MSG_TYPE_CAN        = 0;
	// 1 is timer message (FC only)
	public static final short NET_MSG_TYPE_GPS_ORIGIN = 2;
	public static final short NET_MSG_TYPE_PRESSURE   = 3;
	
	/* abstract messages */
	public abstract byte[] toByteArray();
	public abstract void putMessage(DataOutputStream dos);

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
			case NET_MSG_TYPE_CAN:
				return new CanMessage(s);

			case NET_MSG_TYPE_GPS_ORIGIN:
			{
				double lat  = s.readDouble();
				double lng  = s.readDouble();
				double ht   = s.readDouble();
				// FIXME: return new GPSMessage(s);
			}
			break;
			case NET_MSG_TYPE_PRESSURE:
			{
				double press = s.readDouble();
				double temp  = s.readDouble();
				// FIXME: return new PressureMessage(s);
			}
			break;
		}
		return null;		// FIXME: return new NullMessage;
	}
}
