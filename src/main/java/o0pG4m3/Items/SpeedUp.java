package o0pG4m3.Items;

import java.io.*;
import java.awt.image.*;
import o0pG4m3.Characters.*;

import javax.imageio.ImageIO;

public class SpeedUp extends PowerUp {
    public SpeedUp(int xID, int yID) {
        super(xID, yID);
        try {
            image = ImageIO.read(new File("assets/SpeedUp.png"));
        } catch (IOException e) {
            System.out.println("Could not find \"SpeedUp.png\" file");
        }
        explodeAmount = 1;

        frameOrder = new int[]{0};
        initSubimage();
    }

    public void addEffect(Charakter character) {
        character.speedUp(2);
    }
}