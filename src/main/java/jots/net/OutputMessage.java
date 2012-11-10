/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jots.net;

import java.io.IOException;
import java.io.OutputStream;
import java.security.NoSuchProviderException;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.Adler32;
import java.util.zip.Checksum;
import jots.login.LoginServerHandler;
import jots.packet.Login;
import org.bouncycastle.crypto.CryptoException;
import org.bouncycastle.crypto.InvalidCipherTextException;
import org.jboss.netty.buffer.LittleEndianHeapChannelBuffer;

/**
 *
 * @author trou
 */
public class OutputMessage extends  NetworkMessage
{
 private static final Logger logger = Logger.getLogger(
            OutputMessage.class.getName());
    private static int defaultLength = 256;
    
    private int headerSize = 2;
    
    private int messageLength = 0;
    public OutputMessage(int length)
    {
        super(length);
    }
    
    public OutputMessage()
    {
        super(defaultLength);
    }
    
    public void setHeaderSize(int size)
    {
        this.headerSize = size;
    }
    
    public void putInt(long a)
    {
        this.putInt((int)a);
    }
    public void putInt(int a)
    {
        super.writeInt(a);
        messageLength += 4;
    }
    public void writeInt(int a)
    {
        this.putInt(a);
    }
    
    public void writeShort(int a)
    {
        this.writeShort((short)a);
    }
    
    public void putShort(short a)
    {
        super.writeShort(a);
        messageLength += 2;
    }
    public void writeShort(short a)
    {
        this.putShort(a);      
    }
    
    public void putByte(byte a)
    {
        super.writeByte(a);
        messageLength += 1;
    }
    
    public void writeByte(byte a)
    {
        this.putByte(a);
    }
    public void writeByte(int a)
    {
        this.writeByte((byte)a);
    }
    
    public int length()
    {
        return this.messageLength;
    }
    
    public void addPadBytes(int howMany)
    {
        int added = 0;
        while(howMany > added)
        {
            added++;
            this.writeByte(0x00);
        }
    }
    
    public void encrypt(int[] key)
    {
        try {
            byte[] tmp = new byte[messageLength];

            System.arraycopy(this.array(), 6, tmp, 0, messageLength );
            XTEA2 x = new XTEA2(key);
            tmp = x.mencrypt(tmp);
            buf.setBytes(6, tmp);
        } catch (Exception ex) {
            Logger.getLogger(OutputMessage.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    
    
    public void addChecksum()
    {
        Checksum adlr = new Adler32();
        adlr.update(this.array(), 6, messageLength);
        
        int pos = this.writerIndex();
        this.writerIndex(0);
        this.writeShort(4+messageLength);
        this.writeInt((int)adlr.getValue());
        this.writerIndex(pos);
    }
    
    @Override
    public void writeString(String msg)
    {
        messageLength += 2;
        messageLength += msg.length();
        super.writeString(msg);
    }

    public void putByte(Login login) {
        this.writeByte(login.getType());
    }
}
