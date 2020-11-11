package o0pG4m3.Scene;

import java.io.*;
import java.awt.image.*;

import java.awt.Rectangle;
import javax.imageio.ImageIO;

public class Hud {
    private BufferedImage image;
    private BufferedImage bombImage;
    private BufferedImage explosionImage;

    public Hud() {
        try {
            image = ImageIO.read(new File("assets/HUD.png"));
            bombImage = ImageIO.read(new File("assets/BombIcon.png"));
            explosionImage = ImageIO.read(new File("assets/ExplosionIcon.png"));
        } catch (IOException e) {
            System.out.println("Could not find \"HUD.png\" file");
        }
    }

    public BufferedImage getImage() {
        return image;
    }

    public BufferedImage getBombImage() {
        return bombImage;
    }

    public BufferedImage getExplosionImage() {
        return explosionImage;
    }
}