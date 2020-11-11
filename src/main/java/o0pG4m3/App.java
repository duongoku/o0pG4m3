package o0pG4m3;

import java.io.*;
import javax.swing.*;
import java.awt.event.*;

import o0pG4m3.Characters.*;

import java.lang.Math;
import java.util.Scanner;

public class App {
    public static void main(String[] args) {
        JFrame myFrame = new JFrame("Demo");

        myFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        myFrame.setResizable(false);

        GameCanvas myCanvas = new GameCanvas();
        myFrame.add(myCanvas);
        myFrame.pack();
        myFrame.setLocationRelativeTo(null);
        myFrame.setVisible(true);

        myCanvas.init();
        myCanvas.run();
    }

    public static String getMapPath(int n) {
        String mapPath = "maps/map_";
        if(n < 10) {
            mapPath += "0";
        }
        mapPath += String.valueOf(n);
        mapPath += ".txt";
        return mapPath;
    }

    public static int getMapWidth(String mapPath) {
        Scanner sc = null;
        try {
            sc = new Scanner(new File(mapPath));
        } catch(IOException e) {
            System.out.println("Map not found");
        }
        return sc.nextInt();
    }

    public static int getMapHeight(String mapPath) {
        Scanner sc = null;
        try {
            sc = new Scanner(new File(mapPath));
        } catch(IOException e) {
            System.out.println("Map not found");
        }
        sc.nextInt();
        return sc.nextInt();
    }

    public static int getRandomInt(int n) {
        return ((int)(100000 * Math.random()))%n;
    }
}
