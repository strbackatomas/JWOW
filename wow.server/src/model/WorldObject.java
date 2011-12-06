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
    protected String type;
    protected int hp;

    public WorldObject(Point position, String id, String name,String type,int hp) {
        this.position = position;
        this.id = id;
        this.name=name;
        this.type = type;
        this.hp = hp;

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
        return "WorldObject{" + "position=" + position + ", id=" + id + ", name=" + name + ", type=" + type + ", hp=" + hp + '}';
    }

    public String getType() {
        return type;
    }

    public int getHp() {
        return hp;
    }

    void setType(String string) {
        this.type =string;
    }

    void setHp(int hp){
        this.hp = hp;
    }
}
