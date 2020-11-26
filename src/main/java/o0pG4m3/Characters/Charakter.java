package o0pG4m3.Characters;

import java.io.*;
import java.awt.image.*;

import java.awt.Rectangle;
import javax.imageio.ImageIO;
import java.awt.event.KeyEvent;

public abstract class Charakter {
    protected int posX;
    protected int posY;
    protected int initX;
    protected int initY;
    protected final int width = 40;
    protected final int height = 40;
    protected int[] frameOrderUp;
    protected int[] frameOrderDown;
    protected int[] frameOrderLeft;
    protected int[] frameOrderRight;
    protected int[] frameOrderDie;
    protected int botType; // Player ~ -1, Bot ~ >= 0
    protected int health;
    protected int dieFrame;
    protected int bombPower;
    protected int bombAmount;
    protected int bombPowerMAX;
    protected int bombAmountMAX;
    protected int[] directionIDList;
    protected int directionIDListSize;
    protected int redirectionID;
    protected Rectangle rect;
    protected Rectangle moveRect;
    protected int moveSpeed;
    protected int moveSpeedMAX;
    protected int moveSpeedMIN;
    protected BufferedImage image;
    protected BufferedImage border;
    protected BufferedImage dieImage;
    protected BufferedImage[] dieSubimage;
    protected BufferedImage[] subimageUp;
    protected BufferedImage[] subimageDown;
    protected BufferedImage[] subimageLeft;
    protected BufferedImage[] subimageRight;
    protected BufferedImage[] rawImage;

    protected int rawImageID;

    public final int KEY_UP;
    public final int KEY_DOWN;
    public final int KEY_LEFT;
    public final int KEY_RIGHT;
    public final int KEY_BOMB;
    public final int STANDING = 0;

    public Charakter(int keyup, int keydown, int keyleft, int keyright, int keybomb, int xID, int yID) {
        KEY_UP = keyup;
        KEY_DOWN = keydown;
        KEY_LEFT = keyleft;
        KEY_RIGHT = keyright;
        KEY_BOMB = keybomb;

        rect = new Rectangle();
        moveRect = new Rectangle();

        initX = xID;
        initY = yID;

        botType = -1;

        loadDieImage();
        reset();
    }

    public void loadDieImage() {
        frameOrderDie = new int[]{0, 1, 1, 2, 2, 3, 3};

        try {
            dieImage = ImageIO.read(new File("assets/Die.png"));
        } catch (IOException e) {
            System.out.println("Could not find \"Die.png\" file");
        }

        dieSubimage = new BufferedImage[frameOrderDie.length];
        for(int i=0;i<frameOrderDie.length;i++) {
            dieSubimage[i] = dieImage.getSubimage(frameOrderDie[i]*width, 0, width, height);
        }
    }

    public void reset() {
        health = 1;
        bombPower = 1;
        bombAmount = 3;
        bombPowerMAX = 5;
        bombAmountMAX = 5;
        moveSpeed = 4;
        moveSpeedMAX = 10;
        moveSpeedMIN = 2;
        
        dieFrame = -1;
        directionIDList = new int[8];
        directionIDList[0] = KEY_DOWN;
        directionIDListSize = 1;
        redirectionID = STANDING;
        posX = initX*width;
        posY = initY*height;

        if(botType >= 0) {
            addDirectionID(KEY_DOWN);
        }
    }

    public void setRedirectionID(int directionID) {
        redirectionID = directionID;
    }

    public void setRandomDirectionID() {
        int i = (int)(Math.random()*100000) % 4;
        switch(i) {
            case 0:
                directionIDList[directionIDListSize-1] = KEY_UP;
                break;
            case 1:
                directionIDList[directionIDListSize-1] = KEY_DOWN;
                break;
            case 2:
                directionIDList[directionIDListSize-1] = KEY_LEFT;
                break;
            case 3:
                directionIDList[directionIDListSize-1] = KEY_RIGHT;
                break;
            default:
                break;
        }
    }

    public void setDirectionID(int keyK0d3) {
        directionIDList[directionIDListSize-1] = keyK0d3;
    }

    public void addDirectionID(int directionID) {
        for(int i=1; i<directionIDListSize; i++) {
            if(directionIDList[i] == directionID) {
                return;
            }
        }
        directionIDList[directionIDListSize] = directionID;
        directionIDListSize++;
    }

    public void removeDirectionID(int keyKode) {
        if(directionIDListSize == 1) {
            return;
        } 
        if(directionIDListSize == 2) {
            directionIDList[0] = directionIDList[1];
        } else {
            for(int i=1; i<directionIDListSize; i++) {
                if(directionIDList[i] == keyKode) {
                    for(int j=i+1; j<directionIDListSize; j++) {
                        directionIDList[j-1] = directionIDList[j];
                    }
                }
            }
        }
        directionIDListSize--;
    }

    public void bombUp(int n) {
        bombAmount += n;
        bombAmount = Math.min(bombAmount, bombAmountMAX);
    }

    public void explodeUp(int n) {
        bombPower += n;
        bombPower = Math.min(bombPower, bombPowerMAX);
    }

    public void speedUp(int n) {
        moveSpeed += n;
        moveSpeed = Math.max(moveSpeed, moveSpeedMIN);
        moveSpeed = Math.min(moveSpeed, moveSpeedMAX);
    }

    public void healthDown(int n) {
        health -= n;
        if(health <= 0 && dieFrame == -1) {
            dieFrame = frameOrderDie.length;
        }
    }

    public void healthUp(int n) {
        health += n;
    }

    public boolean isAlive() {
        return (health > 0);
    }

    public boolean setBomb() {
        if(bombAmount>0) {
            bombAmount--;
            return true;
        }
        return false;
    }

    public void addBomb() {
        bombAmount++;
    }

    public boolean isDirectionUp() {
        return (directionIDList[directionIDListSize-1] == KEY_UP);
    }


    public boolean isDirectionDown() {
        return (directionIDList[directionIDListSize-1] == KEY_DOWN);
    }


    public boolean isDirectionLeft() {
        return (directionIDList[directionIDListSize-1] == KEY_LEFT);
    }


    public boolean isDirectionRight() {
        return (directionIDList[directionIDListSize-1] == KEY_RIGHT);
    }

    public int getDieFrame() {
        return dieFrame;
    }

    public int getBombPower() {
        return bombPower;
    }

    public int getBombAmount() {
        return bombAmount;
    }

    public int getBotType() {
        return botType;
    }

    public void setPosX(int posX) {
        this.posX = posX;
    }

    public void setPosY(int posY) {
        this.posY = posY;
    }

    public int getPosX() {
        return posX;
    }

    public int getPosY() {
        return posY;
    }

    public int getXID(int blockW) {
        return Math.round((float)posX/blockW);
    }

    public int getYID(int blockH) {
        return Math.round((float)posY/blockH);
    }

    public int getDirectionID() {
        return directionIDList[directionIDListSize-1];
    }

    public int getMoveSpeed() {
        return moveSpeed;
    }

    public Rectangle getRect() {
        rect.setBounds(posX, posY, width, height);
        return rect;
    }

    public Rectangle getMoveRect() {
        if(directionIDList[directionIDListSize-1] == KEY_UP) {
            moveRect.setBounds(posX, posY-moveSpeed, width, height);
            return moveRect;
        }
        if(directionIDList[directionIDListSize-1] == KEY_DOWN) {
            moveRect.setBounds(posX, posY+moveSpeed, width, height);
            return moveRect;
        }
        if(directionIDList[directionIDListSize-1] == KEY_LEFT) {
            moveRect.setBounds(posX-moveSpeed, posY, width, height);
            return moveRect;
        }
        if(directionIDList[directionIDListSize-1] == KEY_RIGHT) {
            moveRect.setBounds(posX+moveSpeed, posY, width, height);
            return moveRect;
        }
        if(directionIDList[directionIDListSize-1] == STANDING) {
            moveRect.setBounds(posX, posY, width, height);
            return moveRect;
        }
        return null;
    }

    public void moveWithDirectionID(int directionID, int step) {
        if(directionID == KEY_UP) {
            posY -= step;
        } else if(directionID == KEY_DOWN) {
            posY += step;
        } else if(directionID == KEY_LEFT) {
            posX -= step;
        } else if(directionID == KEY_RIGHT) {
            posX += step;
        }
    }

    public void move(int step) {
        if(directionIDListSize == 1 || dieFrame >= 0) {
            return;
        }
        if(step > moveSpeed) {
            moveWithDirectionID(redirectionID, moveSpeed);
            redirectionID = STANDING;
            return;
        } else {
            if(redirectionID != STANDING) {
                moveWithDirectionID(redirectionID, step);
                moveWithDirectionID(directionIDList[directionIDListSize-1], moveSpeed-step);
                redirectionID = STANDING;
            } else {
                moveWithDirectionID(directionIDList[directionIDListSize-1], step);
            }
        }
    }

    public BufferedImage getImage(int frameID) {
        frameID %= (frameOrderUp.length-1);
        frameID += 1;
        if(directionIDListSize == 1) {
            frameID = 0;
        }
        if(directionIDList[directionIDListSize-1] == KEY_UP) {
            return subimageUp[frameID];
        } else if(directionIDList[directionIDListSize-1] == KEY_DOWN) {
            return subimageDown[frameID];
        } else if(directionIDList[directionIDListSize-1] == KEY_LEFT) {
            return subimageLeft[frameID];
        } else if(directionIDList[directionIDListSize-1] == KEY_RIGHT) {
            return subimageRight[frameID];
        }
        return null;
    }

    public int getImageID(int frameID) {
        frameID %= (frameOrderUp.length-1);
        frameID += 1;
        if(directionIDListSize == 1) {
            frameID = 0;
        }
        if(directionIDList[directionIDListSize-1] == KEY_UP) {
            return frameOrderUp[frameID];
        } else if(directionIDList[directionIDListSize-1] == KEY_DOWN) {
            return frameOrderDown[frameID];
        } else if(directionIDList[directionIDListSize-1] == KEY_LEFT) {
            return frameOrderLeft[frameID];
        } else if(directionIDList[directionIDListSize-1] == KEY_RIGHT) {
            return frameOrderRight[frameID];
        }
        return 0;
    }

    public void setRawImageID(int rawImageID) {
        this.rawImageID = rawImageID;
    }

    public BufferedImage getRawImage() {
        return rawImage[rawImageID];
    }

    public BufferedImage getPortrait() {
        return subimageDown[0];
    }

    public BufferedImage getDieImage() {
        dieFrame--;
        return dieSubimage[dieFrame];
    }

    public void countsDieFrame() {
        if(dieFrame == -1) {
            return;
        }
        dieFrame--;
    }

    public BufferedImage getBorder(int frameID) {
        return border;
    }
}