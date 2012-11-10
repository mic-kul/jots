/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jots.packet.out.login;

import jots.packet.Login;

/**
 *
 * @author trou
 */
public class Disconnect extends OutLoginPacket {
    public Disconnect(String reason)
    {
        om.putByte(Login.DISCONNECT);
        om.writeString(reason);
        
    }
}
