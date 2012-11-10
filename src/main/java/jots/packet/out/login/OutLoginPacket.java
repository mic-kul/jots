
package jots.packet.out.login;

import java.util.Arrays;
import jots.net.OutputMessage;
import org.jboss.netty.buffer.LittleEndianHeapChannelBuffer;

public class OutLoginPacket {
    OutputMessage om = new OutputMessage(1024);
    OutLoginPacket()
    {
        om.writerIndex(8);
    }
    
    public void prepare(int[] k)
    {
        int tmpIndex = om.writerIndex();
        om.writerIndex(6);

        om.writeShort(om.length());
        om.writerIndex(tmpIndex);
        om.addPadBytes(8-om.length()%8);
        om.encrypt(k);
        om.addChecksum();
    
    }
    
    public byte[] getRaw()
    {
        byte[] buf = new byte[om.getBuffer().writerIndex() - 8];
        om.getBuffer().getBytes(8, buf);
        return buf;
    }
    
    public void injectBefore()
    {
        
    }
    
    public LittleEndianHeapChannelBuffer getBuffer()
    {
        return om.getBuffer();
    }
    
    
}
