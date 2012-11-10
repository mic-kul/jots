/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jots.net;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import jots.encryption.XTEAEngine2;
import org.bouncycastle.crypto.*;
import org.bouncycastle.crypto.engines.XTEAEngine;
import org.bouncycastle.crypto.paddings.PaddedBufferedBlockCipher;
import org.bouncycastle.crypto.params.KeyParameter;

public class XTEA2 {
    BlockCipher engine;
    BufferedBlockCipher cipher;
    KeyParameter kp;
    int[] key;
    public XTEA2(byte[] key) throws NoSuchProviderException, InvalidCipherTextException
    {
      
            engine = new XTEAEngine();
            cipher = new BufferedBlockCipher(engine);
            kp = new KeyParameter(key);
            
    }
    
    public XTEA2(int[] key) throws NoSuchProviderException, InvalidCipherTextException
    {
        XTEAEngine2 engine = new XTEAEngine2();
        engine.intKey = true;
        engine.setKey(key);
        this.key = key;
        this.engine = (BlockCipher)engine;
        cipher = new BufferedBlockCipher(engine);
        
        
    }
    public static void unpackInt(int v, byte[] out, int outOff)
    {
        out[outOff++] = (byte)(v >>> 24);
        out[outOff++] = (byte)(v >>> 16);
        out[outOff++] = (byte)(v >>>  8);
        out[outOff  ] = (byte)v;
    }
    private byte[] callCipher( byte[] data )
    throws CryptoException {
        int    size =
                cipher.getOutputSize( data.length );
        byte[] result = new byte[ size ];
        int    olen = cipher.processBytes( data, 0,
                data.length, result, 0 );
        olen += cipher.doFinal( result, olen );
        
        if( olen < size ){
            byte[] tmp = new byte[ olen ];
            System.arraycopy(
                    result, 0, tmp, 0, olen );
            result = tmp;
        }
        
        return result;
    }
  
    public synchronized byte[] encrypt( byte[] data )
    throws CryptoException {
        if( data == null || data.length == 0 ){
            return new byte[0];
        }
        
        cipher.init( true, kp );
        return callCipher( data );
    }
    
    public synchronized byte[] decrypt( byte[] data )
    throws CryptoException {
        if( data == null || data.length == 0 ){
            return new byte[0];
        }
        
        cipher.init( false, kp );
        return callCipher( data );
    }
    
    public int[] toIntArray(byte[] barr) { 
        //Pad the size to multiple of 4 
        int size = (barr.length / 4) + ((barr.length % 4 == 0) ? 0 : 1);      

        ByteBuffer bb = ByteBuffer.allocate(size *4); 
        bb.put(barr); 

        //Java uses Big Endian. Network program uses Little Endian. 
        bb.order(ByteOrder.LITTLE_ENDIAN); 
       

        int[] result = new int[size]; 
        bb.rewind(); 
        while (bb.remaining() > 0) { 
            result[bb.position()/4] =bb.getInt(); 
        } 

        return result; 
}
    public byte[] mencrypt(byte[] data) throws Exception
    {
        int[] k = new int[4];
	for(int i = 0; i < 4; i++)
		k[i] = key[i];

	int messageLength = data.length;
	//add bytes until reach 8 multiple
	int n;
	if((messageLength % 8) != 0)
	{
		throw new Exception("INVALID LENGTH");
	}

	int readPos = 0;
       // uint32_t* buffer = (uint32_t*)msg.getOutputBuffer();
        int[] buffer = toIntArray(data);
	while(readPos < messageLength / 4)
	{
		int v0 = buffer[readPos], v1 = buffer[readPos + 1];
		int delta = 0x61C88647;
		int sum = 0;
		for(int i = 0; i < 32; i++)
		{
			v0 += ((v1 << 4 ^ v1 >>> 5) + v1) ^ (sum + k[sum & 3]);
			sum -= delta;
			v1 += ((v0 << 4 ^ v0 >>> 5) + v0) ^ (sum + k[sum>>>11 & 3]);
		}

		buffer[readPos] = v0;
		buffer[readPos + 1] = v1;
		readPos += 2;
	}
        ByteBuffer byteBuffer = ByteBuffer.allocate(buffer.length * 4);
         byteBuffer.order(ByteOrder.LITTLE_ENDIAN);
        IntBuffer intBuffer = byteBuffer.asIntBuffer();
        intBuffer.put(buffer);
        byte[] array = byteBuffer.array();
        return array;
        
    }
    
    public byte[] mdecrypt(byte[] data) throws Exception
    {
        if((data.length) % 8 != 0)
	{
		throw new Exception("WRONG SIZE OF ENCRYPTED MESSAGE");
	}

	int[] k = this.key;
        

	int messageLength = data.length;
	//uint32_t* buffer = (uint32_t*)(msg.getBuffer() + msg.getReadPos());
        int[] buffer = toIntArray(data);
	int readPos = 0;
	while(readPos < messageLength / 4)
	{
		int v0 = buffer[readPos], v1 = buffer[readPos + 1];
		int delta = 0x61C88647;
		int sum = 0xC6EF3720;
		for(int i = 0; i < 32; i++)
		{
			v1 -= ((v0 << 4 ^ v0 >>> 5) + v0) ^ (sum + k[sum>>>11 & 3]);
			sum += delta;
			v0 -= ((v1 << 4 ^ v1 >>> 5) + v1) ^ (sum + k[sum & 3]);
		}

		buffer[readPos] = v0;
		buffer[readPos + 1] = v1;
		readPos += 2;
	}
	//
        ByteBuffer byteBuffer = ByteBuffer.allocate(buffer.length * 4);
        byteBuffer.order(ByteOrder.LITTLE_ENDIAN);
        IntBuffer intBuffer = byteBuffer.asIntBuffer();
        intBuffer.put(buffer);
        byte[] array = byteBuffer.array();
        return array;

    }
}