package cansocket;

import java.io.*;

public interface CanSocket
{
	public CanMessage read() throws IOException;
	public void write(CanMessage msg) throws IOException;
	public void close() throws IOException;
	public void flush() throws IOException;
}
