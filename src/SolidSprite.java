import java.awt.*;
import java.awt.geom.Rectangle2D;

public class SolidSprite extends Sprite {
    boolean isTrap = false; // Use to differentiate a trap from others sprites

    public SolidSprite(double x, double y, Image image, double width, double height) {
        super(x, y, image, width, height); // Constructor to initialize sprite
    }

    public Rectangle2D getHitBox() {
        return new Rectangle2D.Double(x, y, (double) width, (double) height); // Returns the hitbox for collision
    }

    public boolean intersect(Rectangle2D.Double hitBox) {
        return this.getHitBox().intersects(hitBox); // Check if this sprite intersects with another hitbox
    }
}
