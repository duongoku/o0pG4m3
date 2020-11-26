package o0pG4m3.Items;

import java.io.*;
import java.awt.image.*;
import o0pG4m3.Characters.*;

import javax.imageio.ImageIO;

public class Portal extends PowerUp {
    public Portal(int xID, int yID) {
        super(xID, yID);
        try {
            image = ImageIO.read(new File("assets/Portal.png"));
        } catch (IOException e) {
            System.out.println("Could not find \"Portal.png\" file");
        }

        frameOrder = new int[]{0};
        initSubimage();
    }

    public void addEffect(Charakter character) {
        //nothing here
    }
}