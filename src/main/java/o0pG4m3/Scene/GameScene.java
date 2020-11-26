package o0pG4m3.Scene;

import java.io.*;
import java.net.*;
import java.awt.*;
import java.util.*;
import java.awt.event.*;
import java.awt.image.*;

import java.lang.Math;
import javax.imageio.ImageIO;
import java.awt.event.KeyEvent;

import o0pG4m3.*;
import o0pG4m3.Items.*;
import o0pG4m3.Blocks.*;
import o0pG4m3.Characters.*;

public class GameScene extends SceneManager {
    private int mapW;
    private int mapH;
    private int mapSeed;
    private int blockW = 40;
    private int blockH = 40;
    private Hud myHUD;
    private int hudInsetX = 10;
    private int hudInsetY = 10;
    private int[][] map; //0 == Tile, 1 == Wall, 2 == Bomb, 3 == Obstacle, 4 == New Item
    private int[][] dirMap;
    private boolean[][] checkMap;
    private Queue<Pair> myQueue = new LinkedList<>();
    private Bomb[][] bombMap;
    private Item[][] itemMap;
    private Bomb myBomb;
    private Tile myTile;
    private Wall myWall;
    private Charakter[] playerList;
    private int playerCount;
    private Charakter[] botList;
    private int botCount;
    private Obstacle myObstacle;

    private boolean isOnline;
    private int onlinePlayerID;
    private Socket socket = null;
    private InputStreamReader in = null;
    private BufferedReader bf = null;
    private PrintWriter pr = null;
    private String address;
    private int port;

    public GameScene(String mapPath, Charakter[] playerList, int playerCount, Charakter[] botList, int botCount, BufferStrategy bs, ImageObserver observer, SoundHandler sound, boolean isOnline) {
        super(bs, observer, sound);
        myBomb = new Bomb();
        myTile = new Tile();
        myWall = new Wall();
        myObstacle = new Obstacle();
        this.playerList = playerList;
        this.playerCount = playerCount;
        this.botList = botList;
        this.botCount = botCount;
        this.isOnline = isOnline;
        myHUD = new Hud();
        loadMap(mapPath);
        if(isOnline) {
            initOnline();
        }
    }

    public void initOnline() {
        try {
            Scanner sc = new Scanner(new File("assets/ServerInfo.txt"));
            address = sc.nextLine();
            port = sc.nextInt();
            sc.close();
            socket = new Socket(address, port);
            pr = new PrintWriter(socket.getOutputStream(), true);
            in = new InputStreamReader(socket.getInputStream());
            bf = new BufferedReader(in);
        } catch(Exception e) {
            e.printStackTrace();
        }
        pr.println("JOIN");
        try {
            onlinePlayerID = Integer.parseInt(bf.readLine());
        } catch(Exception e) {
            onlinePlayerID = 0;
            e.printStackTrace();
        }
        pr.println("SEED");
        try {
            mapSeed = Integer.parseInt(bf.readLine());
        } catch(Exception e) {
            mapSeed = 1;
            e.printStackTrace();
        }
    }

    public void getOnline() {
        String str = "GET";
        String[] args;
        str += " ";
        str += String.valueOf(playerList[onlinePlayerID].getPosX());
        str += " ";
        str += String.valueOf(playerList[onlinePlayerID].getPosY());
        str += " ";
        str += String.valueOf(playerList[onlinePlayerID].getImageID(frameID));
        pr.println(str);
        try {
            str = bf.readLine();
        } catch(Exception e) {
            e.printStackTrace();
        }
        args = str.split(" ", -1);
        if(args[0].equals("NEP")) {
            return;
        }
        for(int i=0; i<playerCount; i++) {
            int x = Integer.parseInt(args[i*3]);
            int y = Integer.parseInt(args[i*3+1]);
            int j = Integer.parseInt(args[i*3+2]);
            playerList[i].setPosX(x);
            playerList[i].setPosY(y);
            playerList[i].setRawImageID(j);
        }
        for(int i=0; i<playerCount; i++) {
            int k=Integer.parseInt(args[i+playerCount*3]);
            if(k==1) {
                keyPressed(playerList[i].KEY_BOMB, playerList[i]);
            }
        }
    }

    public void validatePlayer() {
        for(int i=0;i<playerCount;i++) {
            if(playerList[i].getXID(blockW) < 1) {
                playerList[i].setPosX(blockW);
            }
            if(playerList[i].getYID(blockH) < 1) {
                playerList[i].setPosY(blockH);
            }
            if(playerList[i].getXID(blockW) > (mapW-2)) {
                playerList[i].setPosX((mapW-2)*blockW);
            }
            if(playerList[i].getYID(blockH) > (mapH-2)) {
                playerList[i].setPosY((mapW-2)*blockH);
            }   
        }
    }

    public void validateBot() {
        for(int i=0;i<botCount;i++) {
            if(botList[i].getXID(blockW) < 1) {
                botList[i].setPosX(blockW);
            }
            if(botList[i].getYID(blockH) < 1) {
                botList[i].setPosY(blockH);
            }
            if(botList[i].getXID(blockW) > (mapW-2)) {
                botList[i].setPosX((mapW-2)*blockW);
            }
            if(botList[i].getYID(blockH) > (mapH-2)) {
                botList[i].setPosY((mapW-2)*blockH);
            }
        }
    }

    public int getMapWidth() {
        return mapW;
    }

    public int getMapHeight() {
        return mapH;
    }

    public void loadMap(String mapPath) {
        Scanner sc = null;
        String mapS = null;
        int cnt = 0;

        try {
            sc = new Scanner(new File(mapPath));
        } catch(IOException e) {
            System.out.println("Map not found");
        }

        mapW = sc.nextInt();
        mapH = sc.nextInt();
        sc.nextLine();

        map = new int[mapH][mapW];
        dirMap = new int[mapH][mapW];
        bombMap = new Bomb[mapH][mapW];
        itemMap = new Item[mapH][mapW];
        checkMap = new boolean[mapH][mapW];
        for(int i=0;i<mapH;i++) {
            mapS = sc.nextLine();
            for(int j=0;j<mapW;j++) {
                if(mapS.charAt(j) == 'B') {
                    map[i][j] = 0;
                    if(cnt < botCount) {
                        botList[cnt].setPosX(j*blockW);
                        botList[cnt].setPosY(i*blockH);
                    }
                    cnt++;
                } else {
                    map[i][j] = mapS.charAt(j) - '0';
                }
                bombMap[i][j] = null;
                itemMap[i][j] = null;
            }
        }
        if(botCount > cnt) {
            botCount = cnt;
        }
        //Add walls around
        for(int i=0;i<mapH;i++) {
            map[i][0] = 1;
            map[i][mapW-1] = 1;
        }
        for(int i=0;i<mapW;i++) {
            map[0][i] = 1;
            map[mapH-1][i] = 1;
        }
        sc.close();
    }

    public void keyPressed(int keyCode, Charakter player) {
        if(keyCode == player.KEY_UP
        || keyCode == player.KEY_DOWN
        || keyCode == player.KEY_LEFT
        || keyCode == player.KEY_RIGHT) {
            player.addDirectionID(keyCode);
        }
        if(keyCode == player.KEY_BOMB) {
            int xID = player.getXID(blockW);
            int yID = player.getYID(blockH);
            if(bombMap[yID][xID] == null) {
                if(player.setBomb()) {
                    map[yID][xID] = 2;
                    bombMap[yID][xID] = new Bomb(player);
                }
            }
        }
    }

    public void keyReleased(int keyCode, Charakter player) {
        if(keyCode == player.KEY_UP
        || keyCode == player.KEY_DOWN
        || keyCode == player.KEY_LEFT
        || keyCode == player.KEY_RIGHT) {
            player.removeDirectionID(keyCode);
        }
    }

    public void keyPressedOnline(int keyCode) {
        if(keyCode == KeyEvent.VK_UP
        || keyCode == KeyEvent.VK_DOWN
        || keyCode == KeyEvent.VK_LEFT
        || keyCode == KeyEvent.VK_RIGHT) {
            playerList[onlinePlayerID].addDirectionID(keyCode);
        }
        if(keyCode == KeyEvent.VK_SPACE) {
            pr.println("MOV " + String.valueOf(keyCode));
        }
    }

    public void keyReleasedOnline(int keyCode) {
        if(keyCode == KeyEvent.VK_UP
        || keyCode == KeyEvent.VK_DOWN
        || keyCode == KeyEvent.VK_LEFT
        || keyCode == KeyEvent.VK_RIGHT) {
            playerList[onlinePlayerID].removeDirectionID(keyCode);
        }
        if(keyCode == KeyEvent.VK_SPACE) {
            pr.println("STP " + String.valueOf(keyCode));
        }
    }

    public boolean isEnded() {
        int cnt=0;
        if(botCount>0) {
            for(int i=0;i<botCount;i++) {
                if(botList[i].getDieFrame()<0) {
                    cnt++;
                }
            }
            if(cnt==0 || playerList[0].getDieFrame()==0) {
                return true;
            }
            return false;
        }
        cnt=0;
        for(int i=0;i<playerCount;i++) {
            if(playerList[i].getDieFrame()<0) {
                cnt++;
            }
        }
        if(cnt<=1) {
            if(isOnline) {
                pr.println("DISCONNECT");
            }
            return true;
        }
        return false;
    }

    public boolean checkMapIntersect(int xID, int yID, Rectangle rect) {
        if(map[yID][xID] == 0) return false;
        return rect.intersects(xID*blockW, yID*blockH, blockW, blockH);
    }

    public int checkCollision(Charakter player) {
        Rectangle playerRect = player.getMoveRect();
        int midX = Math.round((float)playerRect.getX()/blockW);
        int midY = Math.round((float)playerRect.getY()/blockH);
        int playerX = (int)(player.getRect().getX());
        int playerY = (int)(player.getRect().getY());
        if(checkMapIntersect(midX, midY-1, playerRect) && player.isDirectionUp()) {
            return (playerY - midY*blockH);
        }
        if(checkMapIntersect(midX, midY+1, playerRect) && player.isDirectionDown()) {
            return (midY*blockH - playerY);
        }
        if(checkMapIntersect(midX-1, midY, playerRect) && player.isDirectionLeft()) {
            return (playerX - midX*blockW);
        }
        if(checkMapIntersect(midX+1, midY, playerRect) && player.isDirectionRight()) {
            return (midX*blockW - playerX);
        }
        if(checkMapIntersect(midX+1, midY+1, playerRect)){
            if(player.isDirectionRight()) {
                player.setRedirectionID(player.KEY_UP);
                return (playerY - midY*blockH);
            } else if(player.isDirectionDown()) {
                player.setRedirectionID(player.KEY_LEFT);
                return (playerX - midX*blockW);
            }
        }
        if(checkMapIntersect(midX+1, midY-1, playerRect)){
            if(player.isDirectionRight()) {
                player.setRedirectionID(player.KEY_DOWN);
                return (midY*blockH - playerY);
            } else if(player.isDirectionUp()) {
                player.setRedirectionID(player.KEY_LEFT);
                return (playerX - midX*blockW);
            }
        }
        if(checkMapIntersect(midX-1, midY+1, playerRect)){
            if(player.isDirectionLeft()) {
                player.setRedirectionID(player.KEY_UP);
                return (playerY - midY*blockH);
            } else if(player.isDirectionDown()) {
                player.setRedirectionID(player.KEY_RIGHT);
                return (midX*blockW - playerX);
            }
        }
        if(checkMapIntersect(midX-1, midY-1, playerRect)){
            if(player.isDirectionLeft()) {
                player.setRedirectionID(player.KEY_DOWN);
                return (midY*blockH - playerY);
            } else if(player.isDirectionUp()) {
                player.setRedirectionID(player.KEY_RIGHT);
                return (midX*blockW - playerX);
            }
        }
        return player.getMoveSpeed();
    }

    public void scanBomb() {
        for(int i=0;i<mapH;i++) {
            for(int j=0;j<mapW;j++) {
                if(bombMap[i][j] != null) {
                    if(bombMap[i][j].tickTock()) {
                        sound.playExplosionSound();
                        detonate(j, i, bombMap[i][j].getPower());
                    }
                }
            }
        }
    }

    public void scanItem() {
        for(int i=0;i<mapH;i++) {
            for(int j=0;j<mapW;j++) {
                if(itemMap[i][j] instanceof Explosion) {
                    if(((Explosion)itemMap[i][j]).tickTock()) {
                        if(map[i][j] == 4) {
                            setNewItem(j, i);
                            map[i][j] = 0;
                        } else {
                            itemMap[i][j] = null;
                        }
                    }
                
                }
                for(int k=0;k<playerCount;k++) {
                    if(itemMap[i][j] != null) {
                        if(playerList[k].getRect().intersects(itemMap[i][j].getRect())) {
                            itemMap[i][j].addEffect(playerList[k]);
                            if(!(itemMap[i][j] instanceof Explosion)) {
                                sound.playPowerupSound();
                                itemMap[i][j] = null;
                            }
                        }
                    }
                }
                for(int k=0;k<botCount;k++) {
                    if(itemMap[i][j] instanceof Explosion) {
                        if(botList[k].getRect().intersects(itemMap[i][j].getRect())) {
                            itemMap[i][j].addEffect(botList[k]);
                        }
                    }
                }
            }
        }
    }

    public void setNewItem(int xID, int yID) {
        int randomInt;
        if(isOnline) {
            randomInt = App.getRandomInt(4, xID, yID, mapSeed);
        } else {
            randomInt = App.getRandomInt(4);
        }
        switch(randomInt) {
            case 0:
                itemMap[yID][xID] = new ExplodeUp(xID, yID);
                break;
            case 1:
                itemMap[yID][xID] = new BombUp(xID, yID);
                break;
            case 2:
                itemMap[yID][xID] = new SpeedUp(xID, yID);
                break;
            default :
                itemMap[yID][xID] = null;
                break;  
        }
    }

    public void detonate(int xID, int yID, int power) {
        bombMap[yID][xID].getOwner().addBomb();
        bombMap[yID][xID] = null;
        map[yID][xID] = 0;
        for(int i=xID; i<Math.min(mapW, xID+power+1); i++) {
            if(map[yID][i] == 3) {
                map[yID][i] = 4;
                itemMap[yID][i] = new Explosion(i, yID);
                break;
            }
            if(map[yID][i] == 2) {
                detonate(i, yID, power);
            }
            if(map[yID][i] == 1) {
                break;
            }
            itemMap[yID][i] = new Explosion(i, yID);
        }
        for(int i=xID; i>=Math.max(0, xID-power); i--) {
            if(map[yID][i] == 3) {
                map[yID][i] = 4;
                itemMap[yID][i] = new Explosion(i, yID);
                break;
            }
            if(map[yID][i] == 2) {
                detonate(i, yID, power);
            }
            if(map[yID][i] == 1) {
                break;
            }
            itemMap[yID][i] = new Explosion(i, yID);
        }
        for(int i=yID; i<Math.min(mapH, yID+power+1); i++) {
            if(map[i][xID] == 3) {
                map[i][xID] = 4;
                itemMap[i][xID] = new Explosion(xID, i);
                break;
            }
            if(map[i][xID] == 2) {
                detonate(xID, i, power);
            }
            if(map[i][xID] == 1) {
                break;
            }
            itemMap[i][xID] = new Explosion(xID, i);
        }
        for(int i=yID; i>=Math.max(0, yID-power); i--) {
            if(map[i][xID] == 3) {
                map[i][xID] = 4;
                itemMap[i][xID] = new Explosion(xID, i);
                break;
            }
            if(map[i][xID] == 2) {
                detonate(xID, i, power);
            }
            if(map[i][xID] == 1) {
                break;
            }
            itemMap[i][xID] = new Explosion(xID, i);
        }
    }

    public void drawBlocks(Graphics g) {
        for(int i=0;i<mapH;i++) {
            for(int j=0;j<mapW;j++) {
                g.drawImage(myTile.getImage(frameID), j*blockW, i*blockH, observer);
                switch(map[i][j]) {
                    case 1:
                        g.drawImage(myWall.getImage(frameID), j*blockW, i*blockH, observer);
                        break;
                    case 2:
                        g.drawImage(myBomb.getImage(frameID), j*blockW, i*blockH, observer);
                        break;
                    case 3:
                        g.drawImage(myObstacle.getImage(frameID), j*blockW, i*blockH, observer);
                        break;
                    default:
                        break;
                }
            }
        }
    }

    public void drawItems(Graphics g) {
        for(int i=0;i<mapH;i++) {
            for(int j=0;j<mapW;j++) {
                if(itemMap[i][j] != null) {
                    g.drawImage(itemMap[i][j].getImage(frameID), j*blockW, i*blockH, observer);
                }
            }
        }
    }

    public void drawPlayer(Graphics g, Charakter player) {
        if(player.getDieFrame() > 0) {
            g.drawImage(player.getDieImage(), player.getPosX(), player.getPosY(), observer);
        } else if(player.getDieFrame() == 0) {
            return;
        } else {
            if(isOnline) {
                g.drawImage(player.getRawImage(), player.getPosX(), player.getPosY(), observer);
            } else {
                g.drawImage(player.getImage(frameID), player.getPosX(), player.getPosY(), observer);
            }
            g.drawImage(player.getBorder(frameID), player.getPosX(), player.getPosY(), observer);
        }
    }

    public void drawHUD(Graphics g) {
        g.drawImage(myHUD.getImage(), blockW*mapW, 0, observer);
        for(int i=0;i<playerCount;i++) {
            g.drawImage(playerList[i].getPortrait(), blockW*mapW + hudInsetX, blockW*3*i + hudInsetY, observer);
            int k=1;
            for(int j=0;j<playerList[i].getBombAmount();k++, j++) {
                g.drawImage(myHUD.getBombImage(), blockW*(mapW+(k%4)) + hudInsetX, blockW*(3*i+(int)(k/4)) + hudInsetY, observer);
            }
            for(int j=0;j<playerList[i].getBombPower();k++, j++) {
                g.drawImage(myHUD.getExplosionImage(), blockW*(mapW+(k%4)) + hudInsetX, blockW*(3*i+(int)(k/4)) + hudInsetY, observer);
            }
        }
    }

    public boolean checkValidCoord(int xID, int yID) {
        return !(
               (xID < 0)
            || (yID < 0)
            || (xID >= mapW)
            || (yID >= mapH)
        );
    }

    public void createDirectionMap(Charakter bot, Charakter player) {
        for(int i=0;i<mapH;i++) {
            for(int j=0;j<mapW;j++) {
                dirMap[i][j] = 9999;
                checkMap[i][j] = false;
            }
        }
        int xID = player.getXID(blockW);
        int yID = player.getYID(blockH);
        myQueue.add(new Pair(xID, yID));
        checkMap[yID][xID] = true;
        while(!myQueue.isEmpty()) {
            xID = myQueue.peek().getX();
            yID = myQueue.peek().getY();
            myQueue.remove();
            if(checkValidCoord(xID + 1, yID) && map[yID][xID + 1]%4 == 0 && checkMap[yID][xID + 1] == false) {
                dirMap[yID][xID + 1] = bot.KEY_LEFT;
                checkMap[yID][xID + 1] = true;
                myQueue.add(new Pair(xID + 1, yID));
            }
            if(checkValidCoord(xID - 1, yID) && map[yID][xID - 1]%4 == 0 && checkMap[yID][xID - 1] == false) {
                dirMap[yID][xID - 1] = bot.KEY_RIGHT;
                checkMap[yID][xID - 1] = true;
                myQueue.add(new Pair(xID - 1, yID));
            }
            if(checkValidCoord(xID, yID + 1) && map[yID + 1][xID]%4 == 0 && checkMap[yID + 1][xID] == false) {
                dirMap[yID + 1][xID] = bot.KEY_UP;
                checkMap[yID + 1][xID] = true;
                myQueue.add(new Pair(xID, yID + 1));
            }
            if(checkValidCoord(xID, yID - 1) && map[yID - 1][xID]%4 == 0 && checkMap[yID - 1][xID] == false) {
                dirMap[yID - 1][xID] = bot.KEY_DOWN;
                checkMap[yID - 1][xID] = true;
                myQueue.add(new Pair(xID, yID - 1));
            }
        }
        int dirToPlayer = 1;
        if(bot.getXID(blockW) > xID) {
            dirToPlayer = 2;
        } else if(bot.getXID(blockW) <= xID) {
            dirToPlayer = 3;
        } else if(bot.getYID(blockH) > yID) {
            dirToPlayer = 0;
        } else if(bot.getYID(blockH) <= yID) {
            dirToPlayer = 1;
        }
        for(int i=0;i<mapH;i++) {
            for(int j=0;j<mapW;j++) {
                if(dirMap[i][j] == 9999) {
                    if(dirToPlayer == 0) {
                        dirMap[i][j] = bot.KEY_UP;
                    } else if(dirToPlayer == 1) {
                        dirMap[i][j] = bot.KEY_DOWN;
                    } else if(dirToPlayer == 2) {
                        dirMap[i][j] = bot.KEY_LEFT;
                    } else if(dirToPlayer == 3) {
                        dirMap[i][j] = bot.KEY_RIGHT;;
                    }
                }
            }
        }
        // for(int i=0;i<mapH;i++) {
        //     for(int j=0;j<mapW;j++) {
        //         System.out.print(dirMap[i][j]);
        //     }
        //     System.out.println();
        // }
        // System.out.println();
    }

    public void scanBots() {
        for(int i=0;i<botCount;i++) {
            if(botList[i].getDieFrame() < 0) {
                for(int j=0;j<playerCount;j++) {
                    if(botList[i].getRect().intersects(playerList[j].getRect())) {
                        playerList[j].healthDown(1);
                    }
                }
            }
        }
    }

    public void preprocessing() {
        frameID++;
        for(int i=0;i<playerCount;i++) {
            if(checkCollision(playerList[i]) > 0) {
                playerList[i].move(checkCollision(playerList[i]));
            }
        }
        for(int i=0;i<botCount;i++) {
            switch(botList[i].getBotType()) {
                case 0:
                    break;
                case 1:
                    createDirectionMap(botList[i], playerList[0]);
                    if(frameID % 30 == 0) {
                        botList[i].speedUp(App.getRandomInt(2) - 1);
                    }
                    botList[i].setDirectionID(dirMap[botList[i].getYID(blockH)][botList[i].getXID(blockW)]);
                default:
                    break;
            }
            if(checkCollision(botList[i]) > 0) {
                botList[i].move(checkCollision(botList[i]));
            } else if(checkCollision(botList[i]) == 0) {
                if(botList[i].getBotType() == 0) {
                    botList[i].setRandomDirectionID();
                }
                botList[i].move(checkCollision(botList[i]));
            }
        }

        scanBomb();
        scanItem();
        scanBots();
    }

    public void render() {
        if(isOnline) {
            getOnline();
        }
        validatePlayer();
        validateBot();
        preprocessing();
        do {
            do {
                graphik = bs.getDrawGraphics();
                graphik.clearRect(0, 0, mapW*blockW, mapH*blockH);
                drawBlocks(graphik);
                drawItems(graphik);
                for(int i=0;i<playerCount;i++) {
                    drawPlayer(graphik, playerList[i]);
                }
                for(int i=0;i<botCount;i++) {
                    drawPlayer(graphik, botList[i]);
                }
                drawHUD(graphik);
                graphik.dispose();
            } while(bs.contentsRestored());
            bs.show();
        } while(bs.contentsLost());
    }
}