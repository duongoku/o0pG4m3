package o0pG4m3.Items;

import java.io.*;
import java.awt.image.*;
import o0pG4m3.Characters.*;

import javax.imageio.ImageIO;

public class BombUp extends PowerUp {
    public BombUp(int xID, int yID) {
        super(xID, yID);
        try {
            image = ImageIO.read(new File("assets/BombUp.png"));
        } catch (IOException e) {
            System.out.println("Could not find \"BombUp.png\" file");
        }
        bombAmount = 1;

        frameOrder = new int[]{0};
        initSubimage();
    }

    public void addEffect(Charakter character) {
        character.bombUp(1);
    }
}