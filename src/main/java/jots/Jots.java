/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jots;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Arrays;
import jots.net.*;
/**
 *
 * @author michal
 */
public class Jots {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
            char[] handshake = new char[5];
            InetAddress addr = InetAddress.getByName("narozia.pl");
            int port = 7172;
            Socket socket = new Socket(addr, port);
            BufferedReader br  = new BufferedReader(
                    new InputStreamReader(socket.getInputStream()));
            br.skip(9);
            br.read(handshake);
            System.out.println(Arrays.toString(handshake));
            socket.close();
            
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
            
    }
}
