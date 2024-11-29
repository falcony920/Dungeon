import java.awt.Image;

public class Trap extends Sprite {
    private int damage;

    public Trap(double x, double y, Image image, double width, double height, int damage) {
        super(x, y, image, width, height);
        this.damage = damage;
        System.out.println("Trap created at (" + x + ", " + y + ") with damage: " + damage); // Debug
    }

    public int getDamage() {
        return damage;
    }
}
