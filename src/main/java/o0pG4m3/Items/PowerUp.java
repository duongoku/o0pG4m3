package o0pG4m3.Items;

public abstract class PowerUp extends Item {
    protected int bombAmount = 0;
    protected int explodeAmount = 0;
    protected int speedAmount = 0;

    public PowerUp(int xID, int yID) {
        super(xID, yID);
    }
}