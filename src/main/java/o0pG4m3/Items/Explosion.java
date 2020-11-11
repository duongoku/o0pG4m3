package o0pG4m3.Items;

import java.io.*;
import java.awt.image.*;
import o0pG4m3.Characters.*;

import javax.imageio.ImageIO;

public class Explosion extends Item {
    protected int frameLeft = 0;

    public Explosion(int xID, int yID) {
        super(xID, yID);
        try {
            image = ImageIO.read(new File("assets/Explosion.png"));
        } catch (IOException e) {
            System.out.println("Could not find \"Explosion.png\" file");
        }
        frameLeft = 5;

        frameOrder = new int[]{0};
        initSubimage();
    }

    public boolean tickTock() {
        if(frameLeft == 0) {
            return true;
        }
        frameLeft--;
        return false;
    }

    public void addEffect(Charakter character) {
        character.healthDown(1);
    }
}