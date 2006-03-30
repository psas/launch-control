/* Copyright 2005 Peter Welte
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
package ping;

import java.io.IOException;
import java.io.ByteArrayOutputStream;
import java.io.ByteArrayInputStream;
import java.io.DataOutputStream;
import java.io.DataInputStream;

/** Class to facilitate pinging. 
 * This class provides methods to create a Ping object
 * from a long or array of bytes, to compare Pings,
 * and to get a string version of a Ping.
 */
public class Ping {
	public final static short BODY_LENGTH = 8; // length in bytes
	public long data; //ping data (8 bytes)

	/** Create a Ping object from the long value */
	public Ping (long l) 
	{ 
		data = l; 
	}

	/** Create a Ping object from an array of (up to 8) bytes */
	public Ping (byte[] bytes)
	{
		data = toLong(bytes);
	}

	/** Test for equality with another object.
	 * @return true if Object o is a Ping object with
	 * identical data, false otherwise.
	 */
	public boolean equals(Object o)
	{
		if (!(o instanceof Ping))
		{
			return false;
		} 
		else
		{
			return data == ((Ping)o).data;
		}
	}

	/** return a string representation of this object */
	public String toString()
	{
		// just print ping as array of bytes
		StringBuffer b = new StringBuffer("body: " + data + ": ");
		byte[] body = toBytes(data);
		for (int i = 0; i < BODY_LENGTH; ++i)
			b.append("" + body[i] + " ");
		return b.toString();
	}

	

	/** get data as 8 bytes */
	public byte[] getDataBytes() 
	{
		return toBytes(data);
	}

	/** get data as long */
	public long getDataLong() 
	{
		return data;
	}
	
	
	/** convert the long value to an array of bytes.
	 * returns an array of 8 bytes (a long is 8 bytes) */
	protected byte[] toBytes(long n) 
	{
		// create bytes so it holds 8 bytes (size of long)
		ByteArrayOutputStream bytes = new ByteArrayOutputStream(8); 
		DataOutputStream data = new DataOutputStream(bytes);

		try { 
			data.writeLong(n);
		} catch (IOException e) {
			System.err.println("error converting long->bytes: " + e);
		}

		return bytes.toByteArray();
	}


	/** returns long from bytes, or -1 on IOException.
	 * Note: the IOException probably never happens, 
	 * so -1 is probably not returned for that reason. */
	protected long toLong(byte[] bytes)
	{
		long retVal = -1;

		ByteArrayInputStream byteInput = new ByteArrayInputStream(bytes); 
		DataInputStream data = new DataInputStream(byteInput);

		try { 
			retVal= data.readLong();
		} catch (IOException e) {
			System.err.println("error converting bytes->long: " + e);
		}

		return retVal;
	}


	protected void testLogic() {
		long value = 1; 
		byte[] data;

		//test long->bytes
		data = toBytes(value);
		System.out.print("Ping long(" + value + ") to bytes: ");
		for (int i = 0; i < BODY_LENGTH; ++i)
			System.out.print(data[i] + " ");
	

		// TEST bytes->long
		long value2 = toLong(data);
		System.out.print(", to long again(" + value2+ ").\n");
		if (value2 != value) {
			System.err.println("Expected " + value2 + " == " +
					value);
		}
	}
}
