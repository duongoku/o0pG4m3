package o0pG4m3.Blocks;

import java.io.*;
import java.awt.image.*;
import javax.imageio.ImageIO;

public class Obstacle extends Block {
    public Obstacle() {
        try {
            image = ImageIO.read(new File("assets/Obstacle.png"));
        } catch (IOException e) {
            System.out.println("Could not find \"Obstacle.png\" file");
        }

        breakable = true;
        frameOrder = new int[]{0};
        initSubimage();
    }
}