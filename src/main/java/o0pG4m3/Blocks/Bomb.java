package o0pG4m3.Blocks;

import java.io.*;
import java.awt.image.*;
import javax.imageio.ImageIO;

import o0pG4m3.Characters.*;

public class Bomb extends Block {
    protected int frameLeft = 0;
    protected int power = 0;
    protected Charakter owner;

    public Bomb(Charakter owner) {
        frameLeft = 60;
        this.owner = owner;
        this.power = owner.getBombPower();
    }

    public Bomb() {
        try {
            image = ImageIO.read(new File("assets/Bomb.png"));
        } catch (IOException e) {
            System.out.println("Could not find \"Bomb.png\" file");
        }
        frameOrder = new int[]{0, 1, 2, 1};
        initSubimage();
    }

    public int getPower() {
        return power;
    }

    public Charakter getOwner() {
        return owner;
    }

    public boolean tickTock() {
        if(frameLeft == 0) {
            return true;
        }
        frameLeft--;
        return false;
    }
}