/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package wow.server;

import java.awt.Point;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.NoSuchElementException;
import java.util.StringTokenizer;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.Player;
import model.Structure;
import model.World;
import model.WorldObject;

/**
 *
 * @author beh01
 */
public class WowServer {

    int port;
    World world;
    TreeMap<String, InetAddress> addresses = new TreeMap<String, InetAddress>();
    private String updateToAllAfterCommand = null;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        WowServer server = new WowServer(6543);
        server.start();
    }
    private void sentDatagram(String data, InetAddress address) {
            DatagramSocket socket;
            try {
                socket = new DatagramSocket();
                byte[] sendData = new byte[1024];
                sendData = data.getBytes();
                //System.out.println(address.toString() + " " + data);
                socket.send(new DatagramPacket(sendData, sendData.length, address, 6544));
            } catch (SocketException ex) {
                System.err.println("Unable to create socket.");
            } catch (IOException ex) {
                System.err.println("Unable to send data to address: " + address);
            }
    }
    
    private void sendToAllPlayers(String command) {
        for (Player p : world.getAllPlayers()) {
            sentDatagram(command, p.getPlayerAddress());
        }
    }
    private void sendToOneDataAboutAllObjects(Player target) {
        for(WorldObject wo:world.getAllWorldObjects()){
            String data="update " + wo.getId() + " " + wo.getName() + " " + wo.getPosition().x + " " + wo.getPosition().y + " " + wo.getType() + " " + wo.getHp() ;
            sentDatagram(data, target.getPlayerAddress());
        }
    }

    private String solveCommand(String command, InetAddress clientAddress) {
        try {
            //System.out.println(command);
            StringTokenizer st = new StringTokenizer(command, " ", false);
            String keyWord = st.nextToken();
            if (keyWord.equals("login")) {
                String name = st.nextToken();
                Player player = new Player(world.getEmptypoint(), world.getNextId(), name, clientAddress,"ALIVE",500);
     
                sendToOneDataAboutAllObjects(player);
                
                world.addWorldObject(player);

                updateToAllAfterCommand = "update " + player.getId() + " " + player.getName() + " " + player.getPosition().x + " " + player.getPosition().y + " " + player.getType() + " " + player.getHp() ;

                return player.getId();
            } else if (keyWord.equals("logout")) {
                String id = st.nextToken();
                WorldObject wo = world.removeObject(id);
                if (wo != null && wo instanceof Player) {
                    Player player = (Player) wo;

                    updateToAllAfterCommand = "remove " + player.getId();

                    return "ok";
                }
            } else if (keyWord.equals("move")) {
                String id = st.nextToken();
                String direction = st.nextToken();
                WorldObject wo = world.moveObject(id, direction);
                //sendToOneDataAboutAllObjects(player);
                if (wo != null) {
                    updateToAllAfterCommand = "update " + wo.getId() + " " + wo.getName() + " " + wo.getPosition().x + " " + wo.getPosition().y + " " + wo.getType() + " " + wo.getHp();
                    return "ok";
                }
            }    else if (keyWord.equals("attack")) {
                   
                    String id = st.nextToken();
                    for (Player p : world.getAllPlayers()) {
                        if (p.getId().equals(id)) {

                            WorldObject wo = world.attackObject(p);
    if (wo != null) {
                                if (wo.getType().equals("DEAD")) {
                                    updateToAllAfterCommand = "result "
                                            + wo.getId()
                                            + " " + wo.getType()
                                            + " " + wo.getHp();
                                           

                                } else {
                                    updateToAllAfterCommand = "result "
                                            + wo.getId()
                                            + " " + wo.getType()
                                            + " " + wo.getHp()
                                            + " " + p.getHp();
                                }
                                return "ok";
                            }
                        }

                    }

            }
        } catch (NoSuchElementException ex) {
            return "error";
        }
        return "error";
    }

    private void start() {
        try {
            ServerSocket server = new ServerSocket(port, 20);
            for (;;) {
                Socket client = server.accept();
                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(client.getInputStream()));
                String command = reader.readLine();

                String answer = solveCommand(command, client.getInetAddress());

                PrintWriter printer = new PrintWriter(client.getOutputStream(), true);
                printer.println(answer);
                printer.flush();
                printer.close();

                client.close();
                world.dump();

                if (updateToAllAfterCommand != null) {
                    sendToAllPlayers(updateToAllAfterCommand);
                    updateToAllAfterCommand = null;
                }
            }


        } catch (IOException ex) {
            ex.printStackTrace();
        }

    }

    private WowServer(int i) {
        port = i;
        world = World.createWorld();
    }
}
