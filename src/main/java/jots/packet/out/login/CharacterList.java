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
public class CharacterList extends OutLoginPacket {
    public CharacterList(String[] names, String[] worlds, int[] ips, short[] ports)
    {
        om.putByte(Login.CHARACTER_LIST);
        om.putByte((byte)names.length); // number of chars
        for(int i=0; i< names.length; i++)
        {
            om.writeString(names[i]);
            om.writeString(worlds[i]);
            om.writeInt(ips[i]);
            om.writeShort(ports[i]);
        }
        om.writeShort(30); // PACC DAYS
    }
}
