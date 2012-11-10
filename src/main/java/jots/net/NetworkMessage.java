
package jots.net;


import java.nio.ByteBuffer;
import org.jboss.netty.buffer.LittleEndianHeapChannelBuffer;

public class NetworkMessage {

    LittleEndianHeapChannelBuffer buf;
    
    public LittleEndianHeapChannelBuffer getBuffer()
    {
        return buf;
    }
    
    public NetworkMessage(int length)
    {
        buf = new LittleEndianHeapChannelBuffer(length);
    }
    
    public NetworkMessage(LittleEndianHeapChannelBuffer chbf)
    {
        buf = chbf;
    }
    
    public void writeInt(int a)
    {
        buf.writeInt(a);
    }
    
    public void writeShort(short a)
    {
        buf.writeShort(a);
    }
    
    public void writeByte(byte a)
    {
        buf.writeByte(a);
    }
    
    public void writerIndex(int a)
    {
        
        buf.writerIndex(a);
    }
    
    public int writerIndex()
    {
        return buf.writerIndex();
    }
    
    public void writeString(String a)
    {
        buf.writeShort((short)a.length());
        buf.writeBytes(a.getBytes());
    }
    
    public byte[] array()
    {
        return buf.array();
    }
    
    public long readInt()
    {
        return buf.readUnsignedInt();
    }
    
    public int readShort()
    {
        return buf.readUnsignedShort();
    }
    
    public short readByte()
    {
        return buf.readUnsignedByte();
    }
    
    public void readBytes(byte[] dst)
    {
        buf.readBytes(dst);
    }
    
    public void readerIndex(int a)
    {
        buf.readerIndex(a);
    }
    
    public String readString()
    {
        int len = this.readShort();
        byte[] msg = new byte[len];
        for(int i=0; i<len; i++)
        {
            msg[i] = buf.readByte();
        }
        return new String(msg);
    }

    public void setBytes(int i, byte[] decrypted) {
     
        buf.setBytes(i, decrypted);
    }
    
}
