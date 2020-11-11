package o0pG4m3.Items;

import java.io.*;
import java.awt.image.*;
import o0pG4m3.Characters.*;

import javax.imageio.ImageIO;

public class ExplodeUp extends PowerUp {
    public ExplodeUp(int xID, int yID) {
        super(xID, yID);
        try {
            image = ImageIO.read(new File("assets/ExplodeUp.png"));
        } catch (IOException e) {
            System.out.println("Could not find \"ExplodeUp.png\" file");
        }
        explodeAmount = 1;

        frameOrder = new int[]{0};
        initSubimage();
    }

    public void addEffect(Charakter character) {
        character.explodeUp(1);
    }
}