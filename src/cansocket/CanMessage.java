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

}