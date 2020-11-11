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

public class EndGameScene extends MenuScene{

    public EndGameScene(BufferStrategy bs, ImageObserver observer, SoundHandler sound) {
        super(bs, observer, sound);
        try {
            bg = ImageIO.read(new File("assets/EndGameBackground.png"));
        } catch(IOException e) {
            e.printStackTrace();
        }

        optionList = new MenuOption[]{
            new MenuOption("Restart"), 
            new MenuOption("Main Menu")
        };

        optionList[0].setSelected(true);
    }
}