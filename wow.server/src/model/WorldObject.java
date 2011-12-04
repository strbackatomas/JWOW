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
public abstract class WorldObject {

    protected Point position;
    protected String id;
    protected String name;

    public WorldObject(Point position, String id, String name) {
        this.position = position;
        this.id = id;
        this.name=name;

    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setPosition(Point position) {
        this.position = position;
    }

    
    public Point getPosition() {

        return position;

    }

    @Override
    public String toString() {
        return "WorldObject{" + "position=" + position + ", id=" + id + ", name=" + name + '}';
    }

}
