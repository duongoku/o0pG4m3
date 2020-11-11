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

public class MenuOption {
    private int width = 123;
    private int height = 23;
    private BufferedImage image;
    private boolean selected;
    private String optionName;

    public MenuOption(String optionName){
        try {
            image = ImageIO.read(new File("assets/Menu" + optionName + ".png"));
        } catch(IOException e) {
            e.printStackTrace();
        }
        this.optionName = optionName;
    }

    public String getName() {
        return optionName;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public boolean isSelected() {
        return selected;
    }

    public BufferedImage getImage() {
        return image;
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }
}