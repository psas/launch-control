package cansocket;

import java.io.*;

public interface CanSocket
{
    public NetMessage read() throws IOException;
    public void write(NetMessage msg) throws IOException;
    public void close() throws IOException;
    public void flush() throws IOException;
}
