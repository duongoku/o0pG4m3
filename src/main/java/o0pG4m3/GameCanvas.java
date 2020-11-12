package o0pG4m3;

import java.io.*;
import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.awt.image.*;

import java.lang.Math;
import java.util.Scanner;
import java.util.ArrayList;
import javax.imageio.ImageIO;

import o0pG4m3.*;
import o0pG4m3.Items.*;
import o0pG4m3.Scene.*;
import o0pG4m3.Blocks.*;
import o0pG4m3.Characters.*;

class GameCanvas extends Canvas {
    private BufferStrategy bs;
    private Graphics graphik;
    private int mapW = 25;
    private int mapH = 15;
    private int mapID = 1;
    private int blockW = 40;
    private int blockH = 40;
    private long currentTime = 0;
    private long previousTime = 0;
    private Charakter[] playerList;
    private Charakter[] botList;
    private GameScene myGame;
    private MenuScene myMenu;
    private EndGameScene myEndGame;
    private PlayerKeyListener[] gameKeyListener;
    private MenuKeyListener menuKeyListener;
    private EndGameKeyListener endGameKeyListener;
    private int gameState;
    private int gameStatePrev;
    private SoundHandler sound;

    private static final int MAINMENU = 0;
    private static final int GAMEPVP = 1;
    private static final int GAMEPVE = 2;
    private static final int GAMEEND = 3;
    private static final int RESTART = 4;

    public GameCanvas() {
        setSize(mapW*blockW, mapH*blockH);
        gameStatePrev = MAINMENU;
        gameState = MAINMENU;
        playerList = new Charakter[]{
        new Player(
            KeyEvent.VK_UP, 
            KeyEvent.VK_DOWN, 
            KeyEvent.VK_LEFT, 
            KeyEvent.VK_RIGHT, 
            KeyEvent.VK_SPACE, 
            1, 
            1,
            "00"
        ),
        new Player(
            KeyEvent.VK_W, 
            KeyEvent.VK_S, 
            KeyEvent.VK_A, 
            KeyEvent.VK_D, 
            KeyEvent.VK_ENTER, 
            mapW - 2,
            mapH - 2,
            "01"
        )
        };

        botList = new Charakter[]{
        new Bot(
            -1, 
            -2, 
            -3, 
            -4, 
            -5, 
            mapW - 2,
            1,
            "00",
            1
        ), 
        new Bot(
            -6, 
            -7, 
            -8, 
            -9, 
            -10, 
            mapW - 2,
            1,
            "00",
            0
        )
        };

        gameKeyListener = new PlayerKeyListener[playerList.length];
        for(int i=0; i<playerList.length; i++) {   
            gameKeyListener[i] = new PlayerKeyListener(playerList[i]);
        }
        menuKeyListener = new MenuKeyListener();
        endGameKeyListener = new EndGameKeyListener();

        sound = new SoundHandler();
        sound.playThemeSound();
    }

    public void resetPlayers() {
        for(int i=0; i<playerList.length; i++) {   
            playerList[i].reset();
        }
    }

    public void resetBots() {
        for(int i=0; i<botList.length; i++) {   
            botList[i].reset();
        }
    }

    public class PlayerKeyListener implements KeyListener {
        private Charakter player;

        public PlayerKeyListener(Charakter player) {
            this.player = player;
        }

        public void keyPressed(KeyEvent e) {
            myGame.keyPressed(e.getKeyCode(), player);
        }

        public void keyReleased(KeyEvent e) {
            myGame.keyReleased(e.getKeyCode(), player);
        }

        public void keyTyped(KeyEvent e) {

        }
    }

    public class MenuKeyListener implements KeyListener {
        public MenuKeyListener() {

        }

        public void keyPressed(KeyEvent e) {
            myMenu.keyPressed(e.getKeyCode());
        }

        public void keyReleased(KeyEvent e) {

        }

        public void keyTyped(KeyEvent e) {

        }
    }

    public class EndGameKeyListener implements KeyListener {
        public EndGameKeyListener() {

        }

        public void keyPressed(KeyEvent e) {
            myEndGame.keyPressed(e.getKeyCode());
        }

        public void keyReleased(KeyEvent e) {

        }

        public void keyTyped(KeyEvent e) {

        }
    }

    public void init() {
        requestFocus();
        createBufferStrategy(2);
        bs = getBufferStrategy();
    }

    public int getFPS() {
        previousTime = currentTime;
        currentTime = System.nanoTime();
        return (int)(1000000000/(currentTime - previousTime));
    }

    public void run() {
        removeAllActionLisenter();
        resetPlayers();
        resetBots();
        switch(gameState) {
            case MAINMENU:
                showMenu();
                break;
            case GAMEPVP:
                resetPVP();
                showGame();
                break;
            case GAMEPVE:
                resetPVE();
                showGame();
                break;
            case GAMEEND:
                showEndGame();
                break;
            default:
                break;
        }
    }

    public void resetMainMenu() {
        myMenu = new MenuScene(bs, this, sound);
        addKeyListener(menuKeyListener);
    }

    public void resetEndGame() {
        myEndGame = new EndGameScene(bs, this, sound);
        addKeyListener(endGameKeyListener);
    }

    public void resetPVP() {
        myGame = new GameScene(App.getMapPath(mapID), playerList, 2, botList, 0, bs, this, sound);
        for(int i=0; i<playerList.length; i++) {
            addKeyListener(gameKeyListener[i]);
        }
    }

    public void resetPVE() {
        myGame = new GameScene(App.getMapPath(mapID), playerList, 1, botList, 2, bs, this, sound);
        for(int i=0; i<playerList.length; i++) {
            addKeyListener(gameKeyListener[i]);
        }
    }

    public void removeAllActionLisenter() {
        for(int i=0; i<playerList.length; i++) {
            removeKeyListener(gameKeyListener[i]);
        }
        removeKeyListener(menuKeyListener);
        removeKeyListener(endGameKeyListener);
    }

    public void showMenu() {
        resetMainMenu();
        myMenu.setGameState(gameState);
        int delay = 30;
        ActionListener taskPerformer = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                myMenu.render();
                gameState = myMenu.getGameState();
                if(gameState != MAINMENU) {
                    ((Timer)e.getSource()).stop();
                    run();
                }
            }
        };

        Timer myTimer = new Timer(delay, taskPerformer);
        myTimer.start();
    }

    public void showGame() {
        gameStatePrev = gameState;
        myGame.setGameState(gameState);
        int delay = 30;
        ActionListener taskPerformer = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                myGame.render();
                if(myGame.isEnded()) {
                    myGame.setGameState(GAMEEND);
                }
                gameState = myGame.getGameState();
                if(gameState == GAMEEND) {
                    ((Timer)e.getSource()).stop();
                    run();
                }
            }
        };

        Timer myTimer = new Timer(delay, taskPerformer);
        myTimer.start();

        // boolean done = false;
        // while(!done) {
        //     myCanvas.render();
        // }
    }

    public void showEndGame() {
        resetEndGame();
        myEndGame.setGameState(gameState);
        int delay = 30;
        ActionListener taskPerformer = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                myEndGame.render();
                gameState = myEndGame.getGameState();
                if(gameState != GAMEEND) {
                    if(gameState == RESTART) {
                        gameState = gameStatePrev;
                    }
                    ((Timer)e.getSource()).stop();
                    run();
                }
            }
        };

        Timer myTimer = new Timer(delay, taskPerformer);
        myTimer.start();
    }
}