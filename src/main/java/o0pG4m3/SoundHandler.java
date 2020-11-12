package o0pG4m3;

import javax.sound.sampled.*;

import java.io.File; 
import java.io.IOException; 
import java.util.Scanner; 

public class SoundHandler {
    private Clip themeClip;
    private Clip powerupClip;
    private Clip explosionClip;
    private AudioInputStream themeSound;
    private AudioInputStream powerupSound;
    private AudioInputStream explosionSound;

    public SoundHandler() {
        try {
            themeSound = AudioSystem.getAudioInputStream(new File("assets/ThemeSound.wav").getAbsoluteFile());
            powerupSound = AudioSystem.getAudioInputStream(new File("assets/PowerupSound.wav").getAbsoluteFile());
            explosionSound = AudioSystem.getAudioInputStream(new File("assets/ExplosionSound.wav").getAbsoluteFile());

            themeClip = AudioSystem.getClip();
            themeClip.open(themeSound);
            powerupClip = AudioSystem.getClip();
            powerupClip.open(powerupSound);
            explosionClip = AudioSystem.getClip();
            explosionClip.open(explosionSound);
        } catch(Exception e) {
            e.printStackTrace();
        }
        
    }

    public void playThemeSound() {
        themeClip.loop(Clip.LOOP_CONTINUOUSLY);
    }

    public void playPowerupSound() {
        powerupClip.setFramePosition(0);
        powerupClip.start();
    }

    public void playExplosionSound() {
        explosionClip.setFramePosition(0);
        explosionClip.start();
    }
}