/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package unsigned;

import java.nio.ByteBuffer;

/**
 *
 * Written by Evangelos Haleplidis, e_halep at yahoo dot gr, and Sean
 * R Owens, sean at guild dot net, released to the public domain.  Share
 * and enjoy.  Since some people argue that it is impossible to release
 * software to the public domain, you are also free to use this code
 * under any version of the GPL, LPGL, or BSD licenses, or contact the
 * authors for use of another license.
 *
 * The unsigned Integer Class.
 * @author Sean Owens [sean at guild dot net].
 * @author Ehalep [e_halep at yahoo dot gr].
 */

public class uInt {

    private long unInt;

     /**
     * Unsigned Int constructor with no value.
     */
    public uInt(){
    }

    /**
     * Unsigned Int constructor with an already read unsigned Int to be converted to Java format.
     * @param unsigned The already read unsigned Int that needs to be converted to Java format.
     */
    public uInt(int unsigned){
        this.read(unsigned);
    }

    /**
     * Unsigned Int constructor with a value in Java format.
     * @param tobecomeunsigned The value in javaformat.
     */
    public uInt(long tobecomeunsigned){
        this.setValue(tobecomeunsigned);
    }

    /**
     * Unsigned Int constructor that read gets an already read unsigned Int
     * that exists inside a ByteBuffer at position offset and converts it into
     * the proper format for Java
     * Caution, the position of the bytebuffer is not changed inside the constructor.
     * @param bb The ByteBuffer in which the already read unsigned Int exists
     * @param offset The position that the 4 bytes of the unsigned Int starts
     */
    public uInt(ByteBuffer bb, int offset){
        this.read(bb, offset);
    }

    /**
     * Unsigned Int constructor that gets an already read unsigned Integer
     * that exists inside a Byte Array at position offset and converts it into
     * the proper format for Java
     * @param bytes The byte array
     * @param offset The position that the 4 bytes of the unsigned integer starts
     */
    public uInt(byte[] bytes, int offset){
        this.read(bytes, offset);
    }

    /**
     * Read gets an already read unsigned Integer and converts it into the proper format for Java
     * @param i The read unsigned integer that needs to be converted to Java format.
     */
    public void read(int i){

        byte[] buf = new byte[4];
        ByteBuffer bb = ByteBuffer.wrap(buf);
        bb.putInt(i);

        int firstByte = (0x000000FF & ((int)buf[0]));
        int secondByte = (0x000000FF & ((int)buf[1]));
        int thirdByte = (0x000000FF & ((int)buf[2]));
        int fourthByte = (0x000000FF & ((int)buf[3]));

        unInt  = ((long) (firstByte << 24 | secondByte << 16 | thirdByte << 8 | fourthByte)) & 0xFFFFFFFFL;
    }

    /**
     * Read gets an already read unsigned Integer that exists inside a ByteBuffer at position offset and converts it into the proper format for Java
     * Caution, the position of the bytebuffer is not changed inside this read call.
     * @param bb The ByteBuffer in which the already read unsigned Integer exists
     * @param offset The position that the 4 bytes of the unsigned integer starts
     */
    public void read(ByteBuffer bb, int offset){

        int initial_pos = bb.position();
        bb.position(offset);

        int firstByte = (0x000000FF & ((int)bb.get()));
        int secondByte = (0x000000FF & ((int)bb.get()));
        int thirdByte = (0x000000FF & ((int)bb.get()));
        int fourthByte = (0x000000FF & ((int)bb.get()));

        unInt  = ((long) (firstByte << 24 | secondByte << 16 | thirdByte << 8 | fourthByte)) & 0xFFFFFFFFL;
        bb.position(initial_pos);
    }

    /**
     * Read gets an already read unsigned Integer that exists inside a Byte Array at position offset and converts it into the proper format for Java
     * @param bytes The byte array
     * @param offset The position that the 4 bytes of the unsigned integer starts
     */
    public void read(byte[] bytes, int offset){

        int firstByte = (0x000000FF & ((int)bytes[offset]));
        int secondByte = (0x000000FF & ((int)bytes[offset]));
        int thirdByte = (0x000000FF & ((int)bytes[offset]));
        int fourthByte = (0x000000FF & ((int)bytes[offset]));

        unInt  = ((long) (firstByte << 24 | secondByte << 16 | thirdByte << 8 | fourthByte)) & 0xFFFFFFFFL;
    }

    /**
     * Write prepares the unsigned Int from Java format to be written into the proper unsigned format for the wire.
     * @return Returns the unsigned int in a byte[] format
     */
    public byte[] write(){
        byte[] buf = new byte[4];

    	buf[0] = (byte)((unInt & 0xFF000000L) >> 24);
    	buf[1] = (byte)((unInt & 0x00FF0000L) >> 16);
    	buf[2] = (byte)((unInt & 0x0000FF00L) >> 8);
    	buf[3] = (byte)(unInt & 0x000000FFL);

        return buf;
    }

    /**
     * Write prepares the unsigned Int from Java format to be written into the proper unsigned format for the wire and puts it at a ByteBuffer at position equal to offset.
     * @param bb The Bytebuffer to put the Unsigned Int
     * @param offset The position to start putting the 4 bytes
     * @return Returns true if the integer was written, false if the offset was wrong.
     */
    public boolean write(ByteBuffer bb, int offset){
        if(bb.limit()-4<offset){
            return false;
        }
        bb.position(offset);

        byte[] buf = new byte[4];

    	buf[0] = (byte)((unInt & 0xFF000000L) >> 24);
    	buf[1] = (byte)((unInt & 0x00FF0000L) >> 16);
    	buf[2] = (byte)((unInt & 0x0000FF00L) >> 8);
    	buf[3] = (byte)(unInt & 0x000000FFL);

        bb.put(buf);
        return true;
    }

    /**
     * Write prepares the unsigned Int from Java format to be written into the proper unsigned format for the wire and puts it at a ByteArray at position equal to offset.
     * @param bytes The Byte Array to put the Unsigned Int
     * @param offset The position to start putting the 4 bytes
     * @return Returns true if the integer was written, false if the offset was wrong.
     */
    public boolean write(byte[] bytes, int offset){
        if(bytes.length-4<offset){
            return false;
        }

    	bytes[offset] = (byte)((unInt & 0xFF000000L) >> 24);
    	bytes[offset+1] = (byte)((unInt & 0x00FF0000L) >> 16);
    	bytes[offset+2] = (byte)((unInt & 0x0000FF00L) >> 8);
    	bytes[offset+3] = (byte)(unInt & 0x000000FFL);

        return true;
    }

    /**
     * getValue returns the actual value of the unsigned int. Caution, returns a long int.
     * @return Returns the actual value of the unsigned int, in long data type.
     */
    public long getValue(){
        return unInt;
    }

    /**
     * SetValue sets an int value.
     * @param value The value.
     */
    public void setValue(long value){
        unInt = value;
    }
}
