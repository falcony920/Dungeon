
import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

public class DynamicSprite extends SolidSprite {
    private Direction direction = Direction.EAST;
    private double speed = 5;
    private double timeBetweenFrame = 250;
    private boolean isWalking = true;
    private boolean isRunning = false; // on fera accélérer le perso en appuyant sur la touche ctrl
    private final int spriteSheetNumberOfColumn = 10;

    public DynamicSprite(double x, double y, Image image, double width, double height) {
        super(x, y, image, width, height);
    }

    // Méthode pour obtenir la vitesse actuelle
    public double getSpeed() {
        double currentSpeed = isRunning ? speed * 2 : speed; // Double la vitesse si en course
        System.out.println("Vitesse actuelle : " + currentSpeed);
        return currentSpeed;
    }

    // Méthode pour définir la vitesse du sprite
    public void setSpeed(double speed) {
        this.speed = speed; // Met à jour la vitesse normale
    }

    private boolean isMovingPossible(ArrayList<Sprite> environment) {
        Rectangle2D.Double moved = new Rectangle2D.Double();
        double currentSpeed = getSpeed();
        System.out.println("Test de déplacement avec vitesse : " + currentSpeed);

        switch (direction) {
            case EAST:
                moved.setRect(super.getHitBox().getX() + currentSpeed, super.getHitBox().getY(),
                        super.getHitBox().getWidth(), super.getHitBox().getHeight());
                break;
            case WEST:
                moved.setRect(super.getHitBox().getX() - currentSpeed, super.getHitBox().getY(),
                        super.getHitBox().getWidth(), super.getHitBox().getHeight());
                break;
            case NORTH:
                moved.setRect(super.getHitBox().getX(), super.getHitBox().getY() - currentSpeed,
                        super.getHitBox().getWidth(), super.getHitBox().getHeight());
                break;
            case SOUTH:
                moved.setRect(super.getHitBox().getX(), super.getHitBox().getY() + currentSpeed,
                        super.getHitBox().getWidth(), super.getHitBox().getHeight());
                break;
        }

        for (Sprite s : environment) {
            if ((s instanceof SolidSprite) && (s != this)) {
                if (((SolidSprite) s).intersect(moved)) {
                    return false;
                }
            }
        }
        return true;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    private void move() {
        double currentSpeed = getSpeed(); // Utiliser la vitesse dynamique
        switch (direction) {
            case NORTH -> {
                this.y -= currentSpeed;
            }
            case SOUTH -> {
                this.y += currentSpeed;
            }
            case EAST -> {
                this.x += currentSpeed;
            }
            case WEST -> {
                this.x -= currentSpeed;
            }
        }
    }

    public void moveIfPossible(ArrayList<Sprite> environment) {
        if (isMovingPossible(environment)) {
            move();
        }
    }

    // Méthode pour savoir si le héros court
    public boolean isRunning() {
        return isRunning;
    }

    // Méthode pour activer ou désactiver la course
    public void setRunning(boolean running) {
        isRunning = running;
    }

    @Override
    public void draw(Graphics g) {
        int index = (int) (System.currentTimeMillis() / timeBetweenFrame % spriteSheetNumberOfColumn);

        g.drawImage(image, (int) x, (int) y, (int) (x + width), (int) (y + height),
                (int) (index * this.width), (int) (direction.getFrameLineNumber() * height),
                (int) ((index + 1) * this.width), (int) ((direction.getFrameLineNumber() + 1) * this.height), null);
    }
}
