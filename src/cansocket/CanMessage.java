package cansocket;

public class CanMessage
{
  protected short id;
  protected short timestamp;
  protected byte body[];

  public static final int MSG_LEN = 8; 
  
  public CanMessage(short id, short timestamp, byte body[])
  {
    this.id = id;
    this.timestamp = timestamp;
    this.body = body;
  }

  public short getId()
  {
    return id;
  }

  public short getTimestamp()
  {
    return timestamp;
  }

  public byte[] getBody()
  {
    return body;
  }

  public byte getData8(int i)
  {
    return body[i];
  }

  public short getData16(int i)
  {
    i <<= 1;
    return (short) (body[i] << 8 | body[i + 1]);
  }

  public int getData32(int i)
  {
    i <<= 2;
    return body[i] << 24 |
           body[i + 1] << 16 |
           body[i + 2] << 8 |
           body[i + 3];
  }
}
