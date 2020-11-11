package o0pG4m3.Blocks;

import java.io.*;
import java.awt.image.*;
import javax.imageio.ImageIO;

public class Tile extends Block {
    public Tile() {
        try {
            image = ImageIO.read(new File("assets/Tile.png"));
        } catch (IOException e) {
            System.out.println("Could not find \"Tile.png\" file");
        }

        frameOrder = new int[]{0};
        initSubimage();
    }
}