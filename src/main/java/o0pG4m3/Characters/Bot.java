package o0pG4m3.Characters;

import java.io.*;
import java.awt.image.*;

import javax.imageio.ImageIO;
import java.awt.event.KeyEvent;

public class Bot extends Charakter {
    public Bot(int keyup, int keydown, int keyleft, int keyright, int keybomb, int xID, int yID, String costumeID, int botType) {
        super(keyup, keydown, keyleft, keyright, keybomb, xID, yID);
        this.botType = botType;

        try {
            image = ImageIO.read(new File("assets/Bot_" + costumeID + ".png"));
        } catch (IOException e) {
            System.out.println("Could not find \"Bot_" + costumeID + ".png\" file");
        }

        try {
            border = ImageIO.read(new File("assets/Bot_Border.png"));
        } catch (IOException e) {
            System.out.println("Could not find \"Bot_Border.png\" file");
        }

        frameOrderUp = new int[]{3, 4, 4, 4, 5, 5, 5};
        frameOrderDown = new int[]{0, 1, 1, 1, 2, 2, 2};
        frameOrderLeft = new int[]{6, 6, 6, 6, 7, 7, 7};
        frameOrderRight = new int[]{8, 8, 8, 8, 9, 9, 9};

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