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
	public static final int MAX_MSG_SIZE              = 512;

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
	public abstract String toString();

	
	/** put the NetMessage header into the DataOutputStream.
	 * @param dos: the DataOutputStream to write to
	 * @param msgSize: Number of bytes used by data of message. 
	 * Example: CanMessage.MSG_SIZE.
	 * @param type: type of message. ex: FMT_CAN.
	 */
	static void putHeader(DataOutputStream dos, int msgSize, int type)
	{
		try {
			dos.writeShort(FC_PROT_VER);
			dos.writeShort(msgSize);
			dos.writeShort(type);
		} catch(IOException e) {
			//never happens according to CanMessage's putMessage comment.
		}
	}

	/* factory */
	static NetMessage newNetMessage(byte packet[]) throws IOException
	{
		DataInputStream s = new DataInputStream(new ByteArrayInputStream(packet));
		try {
			return newNetMessage(s);
		} catch (IOException e) {
			int i;
			System.out.print("bad UDP packet: ");
			for (i=0; i<packet.length; i++)
				System.out.print(" " + Integer.toHexString(packet[i]));
			System.out.println(".");
			throw e;
		}
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
			case FMT_GPS_ORIGIN:
				return new GpsOriginMessage(s);
			case FMT_PRESSURE_BASE:
				return new PressureBaseMessage(s);
		}
		return null;		// FIXME: return new NullMessage;
	}
}
