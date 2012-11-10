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
public class uByte {

    private short unByte;

     /**
     * Unsigned Byte constructor with no value.
     */
    public uByte(){
    }

    /**
     * Unsigned Byte constructor with an already read unsigned Byte to be converted to Java format.
     * @param unsigned The already read unsigned Int that needs to be converted to Java format.
     */
    public uByte(byte unsigned){
        this.read(unsigned);
    }

    /**
     * Unsigned Byte constructor with a value in Java format.
     * @param tobecomeunsigned The value in javaformat.
     */
    public uByte(short tobecomeunsigned){
        this.setValue(tobecomeunsigned);
    }

    /**
     * Unsigned Byte constructor that read gets an already read unsigned Byte
     * that exists inside a ByteBuffer at position offset and converts it into
     * the proper format for Java
     * Caution, the position of the bytebuffer is not changed inside the constructor.
     * @param bb The ByteBuffer in which the already read unsigned Byte exists
     * @param offset The position that the byte of the unsigned Byte starts
     */
    public uByte(ByteBuffer bb, int offset){
        this.read(bb, offset);
    }

    /**
     * Unsigned Byte constructor that gets an already read unsigned Byte
     * that exists inside a Byte Array at position offset and converts it into
     * the proper format for Java
     * @param bytes The byte array
     * @param offset The position that the byte of the unsigned Byte starts
     */
    public uByte(byte[] bytes, int offset){
        this.read(bytes, offset);
    }

    /**
     * Read gets an already read unsigned byte and converts it into the proper format for Java
     * @param i The read unsigned byte that needs to be converted to Java format.
     */
    public void read(byte i){

        int firstByte = (0x000000FF & ((int)i));
        unByte = (short)firstByte;
    }

    /**
     * Read gets an already read unsigned Byte that exists inside a ByteBuffer at position offset and converts it into the proper format for Java
     * Caution, the position of the bytebuffer is not changed inside this read call.
     * @param bb The ByteBuffer in which the already read unsigned Byte exists
     * @param offset The position that the byte of the unsigned Byte starts
     */
    public void read(ByteBuffer bb, int offset){

        int initial_pos = bb.position();
        bb.position(offset);

        int firstByte = (0x000000FF & ((int)bb.get()));

        unByte = (short)firstByte;
        bb.position(initial_pos);
    }

    /**
     * Read gets an already read unsigned Short that exists inside a Byte Array at position offset and converts it into the proper format for Java
     * @param bytes The byte array
     * @param offset The position that the byte of the unsigned byte starts
     */
    public void read(byte[] bytes, int offset){

        int firstByte = (0x000000FF & ((int)bytes[offset]));
        unByte = (short)firstByte;
   }

    /**
     * WriteUnByte prepares the unsigned Byte from Java format to be written into the proper unsigned format for the wire.
     * @return Returns the unsigned byte in a byte format
     */
    public byte write(){
        return (byte)(unByte & 0xFF);
    }

    /**
     * Write prepares the unsigned Byte from Java format to be written into the proper unsigned format for the wire and puts it at a ByteBuffer at position equal to offset.
     * @param bb The Bytebuffer to put the Unsigned Byte
     * @param offset The position to start putting the byte
     * @return Returns true if the byte was written, false if the offset was wrong.
     */
    public boolean write(ByteBuffer bb, int offset){
        if(bb.limit()-1<offset){
            return false;
        }
        bb.position(offset);

        bb.put((byte)(unByte & 0xFF));
        return true;
    }

    /**
     * Write prepares the unsigned Byte from Java format to be written into the proper unsigned format for the wire and puts it at a ByteArray at position equal to offset.
     * @param bytes The Byte Array to put the Unsigned Short
     * @param offset The position to start putting the byte
     * @return Returns true if the Byte was written, false if the offset was wrong.
     */
    public boolean write(byte[] bytes, int offset){
        if(bytes.length-1<offset){
            return false;
        }

    	bytes[offset] = (byte)(unByte & 0xFF);
        return true;
    }

    /**
     * WriteUnByte prepares the unsigned Byte from Java format to be written into the proper unsigned format for the wire in a byte array.
     * @return Returns the unsigned byte in a byte array format
     */
    public byte[] writeByteArray(){
        byte[] b = new byte[1];
        b[0]=(byte)(unByte & 0xFF);
        return b;
    }

    /**
     * getValue returns the actual value of the unsigned byte. Caution, returns a short.
     * @return Returns the actual value of the unsigned byte, in short data type.
     */
    public short getValue(){
        return unByte;
    }
    

    /**
     * SetValue sets an byte value.
     * @param value The value.
     */
    public void setValue(short value){
        unByte = value;
    }
}
