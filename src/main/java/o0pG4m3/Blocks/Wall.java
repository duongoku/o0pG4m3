package o0pG4m3.Blocks;

import java.io.*;
import java.awt.image.*;
import javax.imageio.ImageIO;

public class Wall extends Block {
    public Wall() {
        try {
            image = ImageIO.read(new File("assets/Wall.png"));
        } catch (IOException e) {
            System.out.println("Could not find \"Wall.png\" file");
        }

        frameOrder = new int[]{0};
        initSubimage();
    }
}