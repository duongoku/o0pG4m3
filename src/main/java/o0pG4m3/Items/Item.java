package o0pG4m3.Items;

import java.awt.image.*;
import java.awt.*;
import o0pG4m3.Characters.*;


public abstract class Item {
    protected final int width = 40;
    protected final int height = 40;
    protected int[] frameOrder;
    protected Rectangle itemRect;
    protected BufferedImage image;
    protected BufferedImage[] subimage;

    public Item(int xID, int yID) {
        itemRect = new Rectangle(xID*width, yID*height, width, height);
    }

    public Rectangle getRect() {
        return itemRect;
    }

    public BufferedImage getImage(int frameID) {
        frameID %= frameOrder.length;
        return subimage[frameID];
    }

    public void initSubimage() {
        subimage = new BufferedImage[frameOrder.length];
        for(int i=0;i<subimage.length;i++) {
            subimage[i] = image.getSubimage(frameOrder[i]*width, 0, width, height);
        }
    }

    public abstract void addEffect(Charakter character);
}