package o0pG4m3.Scene;

import java.io.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;

import java.lang.Math;
import java.util.Scanner;
import java.util.ArrayList;
import javax.imageio.ImageIO;

import o0pG4m3.*;
import o0pG4m3.Items.*;
import o0pG4m3.Blocks.*;
import o0pG4m3.Characters.*;

public class MenuBorder {
    private int width = 123;
    private int height = 23;
    private int[] frameOrder;
    private BufferedImage image;
    private BufferedImage[] subimage;

    public MenuBorder(){
        try {
            image = ImageIO.read(new File("assets/MenuBorder.png"));
        } catch(IOException e) {
            e.printStackTrace();
        }
        frameOrder = new int[]{0, 0, 1, 1, 2, 2};
        subimage = new BufferedImage[frameOrder.length];
        for(int i=0; i<subimage.length; i++) {
            subimage[i] = image.getSubimage(width*frameOrder[i], 0, width, height);
        }
    }

    public BufferedImage getImage(int frameID) {
        return subimage[frameID % subimage.length];
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }
}