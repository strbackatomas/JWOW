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

    public WorldObjectData(String name, Point point) {
        this.name = name;
        this.point = point;
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
    
}
