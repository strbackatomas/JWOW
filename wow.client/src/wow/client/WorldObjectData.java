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
    private String type;
    private int hp;

    public WorldObjectData(String name, Point point, String type,int hp) {
        this.name = name;
        this.point = point;
        this.type = type;
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
    
    public String getType(){
        return type;
    }
     public int getHp(){
         return hp;
     }
    
     public void setType(String type){
         this.type = type;
     }
     public void setHp(int hp){
         this.hp = hp;
     }
}
