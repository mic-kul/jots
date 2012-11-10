/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jots.packet;


public enum Login {
    DISCONNECT (0x0A),
    MOTD (0x14),
    CHARACTER_LIST (0x64),
    STATUS_XML (0x01),
    STATUS_BIN (0x00);
    
    private final byte opCode;
    Login(int packet)
    {
        this.opCode = (byte)packet;
    }
    
    public byte getType()
    {
        return opCode;
    }
}
    
    

