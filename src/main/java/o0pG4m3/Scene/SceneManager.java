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

public class SceneManager {
    protected Graphics graphik;
    protected BufferStrategy bs;
    protected ImageObserver observer;
    protected SoundHandler sound;
    protected int frameID = -1;
    protected int gameState;

    protected static final int MAINMENU = 0;
    protected static final int GAMEPVP = 1;
    protected static final int GAMEPVE = 2;
    protected static final int GAMEEND = 3;
    protected static final int RESTART = 4;

    public SceneManager(BufferStrategy bs, ImageObserver observer, SoundHandler sound) {
        this.observer = observer;
        this.bs = bs;
        this.sound = sound;
    }

    public void setGameState(int gameState) {
        this.gameState = gameState;
    }

    public int getGameState() {
        return gameState;
    }
}