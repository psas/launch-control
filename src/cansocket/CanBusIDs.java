/* This file was automatically generated from canbusid.m4. Do not edit. */
package cansocket;

public interface CanBusIDs
{
	public static final int UplinkID = (0x01 << 5);
	public static final int UplinkSleep = (UplinkID | 0);
	public static final int UplinkReceived = (UplinkID | 4);
	public static final int UplinkExecuted = (UplinkID | 8);

	public static final int StatusID = (0x02 << 5);
	public static final int StatusEvent = (StatusID | 0);
	public static final int StatusChange = (StatusID | 1);
	public static final int StatusCurrent = (StatusID | 2);

	public static final int PowerID = (0x03 << 5);
	public static final int PowerSleep = (PowerID | 0);
	public static final int PowerShore = (PowerID | 4);
	public static final int PowerBusCurrent = (PowerID | 8);
	public static final int PowerBusVoltage = (PowerID | 9);
	public static final int PowerPowerDown = (PowerID | 16);

	public static final int IMUID = (0x04 << 5);
	public static final int IMUSleep = (IMUID | 0);
	public static final int IMURate = (IMUID | 4);
	public static final int IMURateDiv = (IMUID | 8);
	public static final int IMUAccel = (IMUID | (4 << 2));
	public static final int IMUGyro = (IMUID | (5 << 2));

	public static final int GPSID = (0x06 << 5);
	public static final int GPSSleep = (GPSID | 0);
	public static final int GPSTransmit = (GPSID | 1);
	public static final int GPSReceive = (GPSID | 2);

	public static final int PressID = (0x07 << 5);
	public static final int PressSleep = (PressID | (0 << 3));
	public static final int PressRate = (PressID | (1 << 3));
	public static final int PressRateDiv = (PressID | (2 << 3));
	public static final int PressValue = (PressID | (3 << 3));

	public static final int UmbilicalID = (0x08 << 5);
	public static final int UmbilicalSleep = (UmbilicalID | 0);
	public static final int UmbilicalStatus = (UmbilicalID | 4);
	public static final int UmbilicalReady = (UmbilicalID | 8);

	public static final int ATVID = (0x10 << 5);
	public static final int ATVSleep = (ATVID | 0);
	public static final int ATVStatus = (ATVID | 1);
	public static final int ATVAmpPower = (ATVID | 2);
	public static final int ATVOverlay = (ATVID | 3);
	public static final int ATVTransmit = (ATVID | 4);
	public static final int ATVReceive = (ATVID | 5);

	public static final int TempID = (0x14 << 5);
	public static final int TempSleep = (TempID | (0 << 3));
	public static final int TempRate = (TempID | (1 << 3));
	public static final int TempRateDiv = (TempID | (2 << 3));
	public static final int TempValue = (TempID | (3 << 3));
}
