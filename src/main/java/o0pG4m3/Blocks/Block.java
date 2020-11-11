package o0pG4m3.Blocks;

import java.awt.image.*;

public abstract class Block {
    protected boolean breakable = false;
    protected final int width = 40;
    protected final int height = 40;
    protected int[] frameOrder;
    protected BufferedImage image;
    protected BufferedImage[] subimage;
    
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
}