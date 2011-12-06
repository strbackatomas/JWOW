/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package wow.client;

import java.awt.Point;

/**
 *
 * @author beh01
 */
public class WorldObjectData {
    private String name;
    private Point point;
    private String types;
    private int hp;

    public WorldObjectData(String name, Point point, String types,int hp) {
        this.name = name;
        this.point = point;
        this.types = types;
        this.hp = hp;
    }

    public String getName() {
        return name;
    }

    public Point getPoint() {
        return point;
    }

    public void setPoint(Point point) {
        this.point = point;
    }

    public void setName(String name) {
        this.name = name;
    }
    
    public void setTypes(String types){
        this.types = types;
    }
    public String getTypes(){
        return types;
    }
    
}
