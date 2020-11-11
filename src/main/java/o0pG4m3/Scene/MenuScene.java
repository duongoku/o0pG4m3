package o0pG4m3.Scene;

import java.io.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;

import java.lang.Math;
import java.util.Scanner;
import java.util.ArrayList;
import javax.imageio.ImageIO;

import o0pG4m3.*;
import o0pG4m3.Items.*;
import o0pG4m3.Blocks.*;
import o0pG4m3.Characters.*;

public class MenuScene extends SceneManager{
    protected BufferedImage bg;
    protected MenuOption[] optionList;
    protected MenuBorder menuBorder;
    protected int cornerX = 437;
    protected int cornerY = 277;

    public MenuScene(BufferStrategy bs, ImageObserver observer, SoundHandler sound){
        super(bs, observer, sound);
        try {
            bg = ImageIO.read(new File("assets/MenuBackground.png"));
        } catch(IOException e) {
            e.printStackTrace();
        }

        optionList = new MenuOption[]{
            new MenuOption("Start"), 
            new MenuOption("PVP"), 
            new MenuOption("PVE")
        };

        optionList[0].setSelected(true);

        menuBorder = new MenuBorder();
    }

    public void keyPressed(KeyEvent e) {
        int offset = optionList.length;
        if(e.getKeyCode() == KeyEvent.VK_ENTER) {
            for(int i=0; i<optionList.length; i++) {
                if(optionList[i].isSelected()) {
                    switch(optionList[i].getName()) {
                        case "PVP":
                            gameState = GAMEPVP;
                            break;
                        case "PVE":
                            gameState = GAMEPVE;
                            break;
                        case "Restart":
                            gameState = RESTART;
                            break;
                        case "Main Menu":
                            gameState = MAINMENU;
                            break;
                        default:
                            break;
                    }
                }
            }
        }
        if(e.getKeyCode() == KeyEvent.VK_DOWN) {
            offset += 1;
        }
        if(e.getKeyCode() == KeyEvent.VK_UP) {
            offset -= 1;
        }
        for(int i=0; i<optionList.length; i++) {
            if(optionList[i].isSelected()) {
                optionList[i].setSelected(false);
                optionList[(i + offset) % optionList.length].setSelected(true);
                break;
            }
        }
    }

    public void drawOption(Graphics g) {
        for(int i=0; i<optionList.length; i++) {
            g.drawImage(
                optionList[i].getImage(), 
                cornerX, cornerY + i*optionList[i].getHeight(), 
                observer
            );
        }
    }

    public void drawOptionBorder(Graphics g) {
        for(int i=0; i<optionList.length; i++) {
            if(optionList[i].isSelected()) {
                g.drawImage(
                    menuBorder.getImage(frameID), 
                    cornerX, cornerY + i*optionList[i].getHeight(), 
                    observer
                );
                break;
            }
        }
    }

    public void render() {
        frameID++;
        do {
            do {
                graphik = bs.getDrawGraphics();
                graphik.drawImage(bg, 0, 0, observer);
                drawOption(graphik);
                drawOptionBorder(graphik);
                graphik.dispose();
            } while(bs.contentsRestored());
            bs.show();
        } while(bs.contentsLost());
    }
}