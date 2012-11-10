/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jots.game;

public class GameServer {
    
    GameServer(String address, int port)
    {
        init(address, port);
    }
    
    GameServer(int port)
    {
        init("0.0.0.0", port);
    }
    
    GameServer()
    {
        init("0.0.0.0", 7172);
    }
    private void init(String address, int port)
    {
        
    }
}
