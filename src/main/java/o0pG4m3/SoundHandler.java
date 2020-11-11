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
            themeClip.setFramePosition(themeClip.getFrameLength());
            powerupClip = AudioSystem.getClip();
            powerupClip.open(powerupSound);
            powerupClip.setFramePosition(powerupClip.getFrameLength());
            explosionClip = AudioSystem.getClip();
            explosionClip.open(explosionSound);
            explosionClip.setFramePosition(explosionClip.getFrameLength());
        } catch(Exception e) {
            e.printStackTrace();
        }
        
    }

    public void playThemeSound() {
        themeClip.loop(Clip.LOOP_CONTINUOUSLY);
    }

    public void playPowerupSound() {
        powerupClip.loop(1);
    }

    public void playExplosionSound() {
        explosionClip.loop(1);
    }
}