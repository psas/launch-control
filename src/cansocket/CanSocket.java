package cansocket;

import java.io.*;

public interface CanSocket
{
    // used by the TCP client/server
    public static final int DEFAULT_PORT = 5349;

    public CanMessage read() throws IOException;
    public void write(CanMessage msg) throws IOException;

    // used by the UDP client/server

    public static final int PORT_SEND = 5348;
    public static final int PORT_RECV = 5347;

    public CanMessage recv() throws IOException;
    public void send(CanMessage msg) throws IOException;

    /* used by both TCP and UDP */
    public void close() throws IOException;
    public void flush() throws IOException;
}
