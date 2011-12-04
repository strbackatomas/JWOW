/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.awt.Point;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author beh01
 */
public class World {
    private  int counter=0;
    Random random = new Random();

    public  String getNextId() {
        return ""+counter++;
    }
    
    private World() {
        
    }
    public static World createWorld() {
        World world=new World();
        try {
           // world.addWorldObject(new Structure(new Point(3, 5), "-1", ""));
            
        //world.addWorldObject(new Player(new Point(3, 3), "-2", "Test", InetAddress.getLocalHost()));
        world.addWorldObject(new Player(new Point(3, 4), "-1", "test", InetAddress.getLocalHost()));
        for(int i =0; i<5000;i++){
            int X= new Random().nextInt(500)-250;
            int Y= new Random().nextInt(500)-250;
            world.addWorldObject(new Structure(world.getNewFreeRandom(), "T"+i, "|#_tree1_#|"));
        }
        //konec herniho pole
        for(int i =-251; i<=251;i++){
        world.addWorldObject(new Structure(new Point(251, i), "ET"+i, "|#_wall2_#|"));
        world.addWorldObject(new Structure(new Point(-251, i), "EB"+i, "|#_wall2_#|"));
        world.addWorldObject(new Structure(new Point(i, 251), "EL"+i, "|#_wall1_#|"));
        world.addWorldObject(new Structure(new Point(i,-251), "ER"+i, "|#_wall1_#|"));
        }
        
        // truhly
        for(int i =0; i<=10;i++){
           world.addWorldObject(new Structure(world.getNewFreeRandom(), "TR"+i, "|#_truhla_#|"));
       }
        
        
        
        } catch (UnknownHostException ex) {
        }
        return  world;
    }
    private Point getNewFreeRandom(){
    int X= new Random().nextInt(500)-250;
    int Y= new Random().nextInt(500)-250;
    Point result= new Point(X, Y);
    if (!isFree(result))return getNewFreeRandom();  
    else return result;
    }
    
    private ArrayList<WorldObject> data =new ArrayList<WorldObject>();
    public List<WorldObject> getAllWorldObjects() {
        return data;
    }
    
    public void addWorldObject(WorldObject wo) {
        data.add(wo);
    }
    public WorldObject removeObject(String id) {
        for(WorldObject wo:data) {
            if (wo.getId().equals(id)) {
                data.remove(wo);
                return wo;
            }
        }
        return null;
    }
    public Point getEmptypoint()
    {
        Point point = new Point(0,0);
        for(;;) {
            point.translate(0, 1);
            boolean same=false;
            for(WorldObject object: data) {
                if (object.getPosition().equals(point))
                    same=true;
            }
            if (same) continue;
            return point;
        }
    }
    public void dump() {
        for(WorldObject wo: data) {
            System.out.println(wo);
        }
    }

    public Iterable<Player> getAllPlayers() {
        ArrayList<Player> players = new ArrayList<Player>();
        for(WorldObject wo: data) {
            if (wo instanceof Player) players.add((Player)wo);
        }
        return players;
    }
    private boolean isFree(Point point) {
        for(WorldObject wo: data) {
            if (wo.getPosition().equals(point)) return false;
        }
        return true;
    }
    
    public WorldObject moveObject(String id, String direction) {
        for(WorldObject wo: data) {
            if (wo.getId().equals(id)) {
                Point newPosition;
                if (direction.equals("left")) newPosition=new Point(wo.getPosition().x-1, wo.getPosition().y);
                else if (direction.equals("right")) newPosition=new Point(wo.getPosition().x+1, wo.getPosition().y);
                else if (direction.equals("up")) newPosition=new Point(wo.getPosition().x, wo.getPosition().y-1);
                else newPosition=new Point(wo.getPosition().x, wo.getPosition().y+1);
                
                if (isFree(newPosition)) {
                    wo.setPosition(newPosition);
                    return wo;
                }
            }
        }
        return null;
    }
}
