/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.awt.Point;

/**
 *
 * @author beh01
 */
public abstract  class Beast extends WorldObject {

    public Beast(Point position, String id,String name) {
        super(position, id, name);
    }


}
