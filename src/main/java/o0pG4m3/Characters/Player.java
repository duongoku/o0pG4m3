package o0pG4m3.Characters;

import java.io.*;
import java.awt.image.*;

import javax.imageio.ImageIO;
import java.awt.event.KeyEvent;

public class Player extends Charakter {
    public Player(int keyup, int keydown, int keyleft, int keyright, int keybomb, int xID, int yID, String costumeID) {
        super(keyup, keydown, keyleft, keyright, keybomb, xID, yID);
        botType = -1;

        try {
            image = ImageIO.read(new File("assets/Player_" + costumeID + ".png"));
        } catch (IOException e) {
            System.out.println("Could not find \"Player_" + costumeID + ".png\" file");
        }

        try {
            border = ImageIO.read(new File("assets/Player_Border.png"));
        } catch (IOException e) {
            System.out.println("Could not find \"Player_Border.png\" file");
        }

        frameOrderUp = new int[]{1, 9, 9, 10, 10};
        frameOrderDown = new int[]{0, 3, 3, 4, 4};
        frameOrderLeft = new int[]{2, 5, 5, 6, 6};
        frameOrderRight = new int[]{11, 7, 7, 8, 8};

        rawImage = new BufferedImage[12];
        for(int i=0;i<12;i++) {
            rawImage[i] = image.getSubimage(i*width, 0, width, height);
        }

        subimageUp = new BufferedImage[frameOrderUp.length];
        for(int i=0;i<frameOrderUp.length;i++) {
            subimageUp[i] = image.getSubimage(frameOrderUp[i]*width, 0, width, height);
        }
        
        subimageDown = new BufferedImage[frameOrderDown.length];
        for(int i=0;i<frameOrderDown.length;i++) {
            subimageDown[i] = image.getSubimage(frameOrderDown[i]*width, 0, width, height);
        }
        
        subimageLeft = new BufferedImage[frameOrderLeft.length];
        for(int i=0;i<frameOrderLeft.length;i++) {
            subimageLeft[i] = image.getSubimage(frameOrderLeft[i]*width, 0, width, height);
        }
        
        subimageRight = new BufferedImage[frameOrderRight.length];
        for(int i=0;i<frameOrderRight.length;i++) {
            subimageRight[i] = image.getSubimage(frameOrderRight[i]*width, 0, width, height);
        }
    }
}