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
public class MOTD extends OutLoginPacket {
    
    public MOTD(int n, String motd)
    {
        om.putByte(Login.MOTD);
        om.writeString(n+"\n"+motd);
    }
    
}
