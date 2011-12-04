/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.awt.Point;
import java.net.InetAddress;

/**
 *
 * @author beh01
 */
public class Player extends Beast{

    InetAddress playerAddress;
    
    public Player(Point position, String id, String name, InetAddress address) {
        super(position, id, name);
        this.playerAddress=address;
    }

    public InetAddress getPlayerAddress() {
        return playerAddress;
    }

    
}
