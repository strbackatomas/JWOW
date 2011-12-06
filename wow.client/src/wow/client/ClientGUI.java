/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * ClientGUI.java
 *
 * Created on 15.11.2011, 18:29:18
 */
package wow.client;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.awt.image.IndexColorModel;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.util.HashMap;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import org.w3c.dom.css.RGBColor;

/**
 *
 * @author beh01
 */
public class ClientGUI extends javax.swing.JFrame {

    private String myID = null;
    private int smer=0;
    private Thread thread;
    private final int viewSize = 9;
    private JLabel labels[][];
    private HashMap<String, WorldObjectData> worldObjects = new HashMap<String, WorldObjectData>();

    
    /** Creates new form ClientGUI */
    public ClientGUI() {
        initComponents();
        labels = new JLabel[viewSize][viewSize];
        GridLayout grid = new GridLayout(viewSize, viewSize);
        grid.setHgap(0);
        grid.setVgap(0);
        mainPanel.setLayout(grid);

        for (int i = 0; i < viewSize; i++) {
            for (int j = 0; j < viewSize; j++) {
                labels[i][j] = new JLabel();
                labels[i][j].setBackground(new Color(0,128,0));
                labels[i][j].setIcon(new javax.swing.ImageIcon(getClass().getResource("/wow/client/black.png")));
                labels[i][j].setOpaque(true);
                mainPanel.add(labels[i][j]);
            }
        }
        mainPanel.validate();
        this.requestFocus(true);

        thread = new Thread(new Runnable() {

            @Override
            public void run() {

                try {
                    DatagramSocket socket = new DatagramSocket(6544);
                    byte[] receiveData = new byte[1024];
                    DatagramPacket recievePacket = new DatagramPacket(receiveData, 1024);
                    for (;;) {
                        socket.receive(recievePacket);
                        String command = new String(recievePacket.getData(), recievePacket.getOffset(), recievePacket.getLength());
                        StringTokenizer st = new StringTokenizer(command, " ", false);
                        String commandType = st.nextToken();
                        if (commandType.equals("update")) {
                            String id = st.nextToken();
                            String name = st.nextToken();
                            int x = Integer.parseInt(st.nextToken());
                            int y = Integer.parseInt(st.nextToken());
                            String type = st.nextToken();
                            int hp = Integer.parseInt(st.nextToken());

                            if (worldObjects.containsKey(id)) {
                                WorldObjectData old = worldObjects.get(id);
                                old.setName(name);
                                old.setPoint(new Point(x, y));
                            } else {
                                worldObjects.put(id, new WorldObjectData(name, new Point(x, y), type, hp));  // snad napícha vše do seznamu
                            }
                        } else if (commandType.equals("remove")) {
                            String id = st.nextToken();
                            if (worldObjects.containsKey(id)) {
                                worldObjects.remove(id);
                            }
                        } else if (commandType.equals("result")) {
                                String id = st.nextToken();
                                String type = st.nextToken();
                                int wohp = Integer.parseInt(st.nextToken());
                                int myhp = Integer.parseInt(st.nextToken());
                                jehoHp.setMaximum(1000);
                                jehoHp.setMinimum(1);
                                jehoHp.setValue(wohp);
                                if (worldObjects.containsKey(id)) {
                                    WorldObjectData old = worldObjects.get(id);
                                    old.setHp(myhp);
                                    old.setType(type);
                                }
                        }
                        if (myID!=null && worldObjects.containsKey(myID)) {
                            WorldObjectData my = worldObjects.get(myID);
                            hpBar.setMaximum(600);
                            hpBar.setValue(my.getHp());
                            
                        }
                        updateView();
                    }
                } catch (SocketException ex) {
                    System.err.println("Unable to open datagram socket.");
                } catch (IOException ex) {
                    System.err.println("Unable to read data from datagram socket.");
                }
            }
        });
        thread.setDaemon(true);

    }
    private Icon createIcon(String name,int x,int y) {
        try {
            WorldObjectData my = worldObjects.get(myID);
            
            BufferedImage hero;
   if (name == my.getName()){
            
            switch(smer){
                case 0:
                    hero = ImageIO.read(getClass().getResource("/wow/client/heroB.png"));
                    break;
                case 1:
                    hero = ImageIO.read(getClass().getResource("/wow/client/heroR.png"));
                    break;
                    
                case 2:
                    hero = ImageIO.read(getClass().getResource("/wow/client/heroL.png"));
                    break;
                case 3:
                    hero = ImageIO.read(getClass().getResource("/wow/client/heroT.png"));
                    break;
                    
               default:
               hero = ImageIO.read(getClass().getResource("/wow/client/heroB.png"));    
            
            }
   }
   else{  
       if("|#_tree1_#|".equals(name)){hero = ImageIO.read(getClass().getResource("/wow/client/tree.png")); name="";}
       else{
           if("|#_wall1_#|".equals(name)){hero = ImageIO.read(getClass().getResource("/wow/client/wall1.png"));name ="";}
           else {
                if("|#_wall2_#|".equals(name)){hero = ImageIO.read(getClass().getResource("/wow/client/wall2.png"));name ="";}
                     
                    else {
                    if("|#_truhla_#|".equals(name)){hero = ImageIO.read(getClass().getResource("/wow/client/box1.png"));name ="";}
                    else hero = ImageIO.read(getClass().getResource("/wow/client/heroB.png"));
                }
                    
                }
       }       
            
   }
            //BufferedImage hero = ImageIO.read(getClass().getResource("/wow/client/heroR.png"));
            BufferedImage bi = new BufferedImage(
                  64,
                  64,
                  IndexColorModel.BITMASK);
            Graphics2D g = bi.createGraphics();
            g.drawImage(hero,0, 0, null);
            g.drawString(name, 2,bi.getHeight()-2);
            return  new ImageIcon(bi);
        } catch (IOException ex) {
            
        }
        return null;
    }
    
    private void updateView() {
        for (int i = 0; i < viewSize; i++) {
            for (int j = 0; j < viewSize; j++) {
                labels[i][j].setIcon(null);
            }
        }
        if (myID!=null && worldObjects.containsKey(myID)) {
            WorldObjectData my = worldObjects.get(myID);
            labels[viewSize/2][viewSize/2].setIcon(createIcon(my.getName(),my.getPoint().x,my.getPoint().y));
            jLabel4.setText("Pozice: "+my.getPoint().x+"|"+my.getPoint().y);
            for(String other:worldObjects.keySet()) {
                if(!other.equals(myID)) {
                    WorldObjectData otherData = worldObjects.get(other);
                    
                    int realx = otherData.getPoint().x-my.getPoint().x+viewSize/2;
                    int realy = otherData.getPoint().y-my.getPoint().y+viewSize/2; 
                    if (realx>=0 && realx<viewSize && realy>=0 && realy<viewSize) {
                        labels[realy][realx].setIcon(createIcon(otherData.getName(),realx,realy));
                    }
                }
            }
        }
    }

    @Override
    public void setVisible(boolean b) {
        super.setVisible(b);
        if (b) {
            thread.start();
        }
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        mainPanel = new javax.swing.JPanel();
        nameText = new javax.swing.JTextField();
        loginButton = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        serverName = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        logoutButton = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        hpBar = new javax.swing.JProgressBar();
        jehoHp = new javax.swing.JProgressBar();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });
        addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                formKeyPressed(evt);
            }
        });

        jPanel1.setBackground(new java.awt.Color(1, 1, 1));

        mainPanel.setBackground(java.awt.Color.green);
        mainPanel.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));
        mainPanel.setPreferredSize(new java.awt.Dimension(600, 600));

        javax.swing.GroupLayout mainPanelLayout = new javax.swing.GroupLayout(mainPanel);
        mainPanel.setLayout(mainPanelLayout);
        mainPanelLayout.setHorizontalGroup(
            mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 570, Short.MAX_VALUE)
        );
        mainPanelLayout.setVerticalGroup(
            mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 589, Short.MAX_VALUE)
        );

        nameText.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                nameTextActionPerformed(evt);
            }
        });

        loginButton.setText("Login");
        loginButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                loginButtonActionPerformed(evt);
            }
        });

        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("Server");

        serverName.setText("localhost");

        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("User name");

        logoutButton.setText("Logout");
        logoutButton.setEnabled(false);
        logoutButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                logoutButtonActionPerformed(evt);
            }
        });

        jLabel3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/wow/client/jwow.png"))); // NOI18N
        jLabel3.setText("jLabel3");

        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        jLabel5.setForeground(new java.awt.Color(255, 255, 255));
        jLabel5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/wow/client/thunt.png"))); // NOI18N

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 315, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 43, Short.MAX_VALUE)
                                .addComponent(jLabel1)
                                .addGap(4, 4, 4))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jLabel2)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)))
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(serverName, javax.swing.GroupLayout.DEFAULT_SIZE, 120, Short.MAX_VALUE)
                            .addComponent(nameText, javax.swing.GroupLayout.DEFAULT_SIZE, 120, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(loginButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(logoutButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(12, 12, 12)
                        .addComponent(mainPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 572, Short.MAX_VALUE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel5, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(hpBar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 164, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jehoHp, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, 76, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(serverName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(loginButton)
                            .addComponent(jLabel1))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(nameText, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(logoutButton)
                            .addComponent(jLabel2))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(hpBar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jehoHp, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(71, 71, 71)
                        .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap())
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(mainPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 591, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(6, 6, 6))))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 126, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private String sendCommandToServer(String command) throws IOException {
        Socket server = new Socket(InetAddress.getByName(serverName.getText()), 6543);
        PrintWriter pw = new PrintWriter(server.getOutputStream(), true);
        pw.println(command);
        pw.flush();

        BufferedReader br = new BufferedReader(new InputStreamReader(server.getInputStream()));
        String answer = br.readLine();
        return answer;
    }

    private void loginButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_loginButtonActionPerformed
        // TODO add your handling code here:
        try {
            nameText.setText(nameText.getText().replaceAll(" ", "_"));
            String answer = sendCommandToServer("login " + nameText.getText());
            myID = answer;
        } catch (Exception e) {
            return;
        }
        serverName.setEnabled(false);
        nameText.setEnabled(false);
        loginButton.setEnabled(false);
        logoutButton.setEnabled(true);
        this.requestFocus(true);
    }//GEN-LAST:event_loginButtonActionPerformed

    private void logoutButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_logoutButtonActionPerformed
        String answer = "";
        try {
            answer = sendCommandToServer("logout " + myID);
        } catch (IOException ex) {
        }
        if (!answer.equals("ok")) {
            return;
        }
        myID = null;

        serverName.setEnabled(true);
        nameText.setEnabled(true);
        loginButton.setEnabled(true);
        logoutButton.setEnabled(false);
        
        for (int i = 0; i < viewSize; i++) {
            for (int j = 0; j < viewSize; j++) {
                labels[i][j].setIcon(new javax.swing.ImageIcon(getClass().getResource("/wow/client/black.png")));
            }
        }
        jLabel4.setText("");

    }//GEN-LAST:event_logoutButtonActionPerformed

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        // TODO add your handling code here:
        if (myID != null) {
            try {
                sendCommandToServer("logout " + myID);
            } catch (IOException ex) {
            }
        }
    }//GEN-LAST:event_formWindowClosing

    private void formKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_formKeyPressed
        if (myID != null) {
            try {
                if (evt.getKeyCode() == 37) {
                    sendCommandToServer("move " + myID + " left");
                    smer=1;
                }
                if (evt.getKeyCode() == 39) {
                    sendCommandToServer("move " + myID + " right");
                    smer=2;
                }
                if (evt.getKeyCode() == 38) {
                    sendCommandToServer("move " + myID + " up");
                    smer=3;
                }
                if (evt.getKeyCode() == 40) {
                    sendCommandToServer("move " + myID + " down");
                    smer=0;
                }
                if (evt.getKeyCode() == 32) {
                    sendCommandToServer("attack " + myID);
                }
            } catch (IOException ex) {
            }
        }
    }//GEN-LAST:event_formKeyPressed

private void nameTextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_nameTextActionPerformed
// TODO add your handling code here:
}//GEN-LAST:event_nameTextActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JProgressBar hpBar;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JProgressBar jehoHp;
    private javax.swing.JButton loginButton;
    private javax.swing.JButton logoutButton;
    private javax.swing.JPanel mainPanel;
    private javax.swing.JTextField nameText;
    private javax.swing.JTextField serverName;
    // End of variables declaration//GEN-END:variables
}
