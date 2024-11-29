import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

public class DynamicSprite extends SolidSprite {
    private Direction direction = Direction.EAST;
    private double speed = 5;
    private int hearts = 3;
    private double timeBetweenFrame = 250;
    private boolean isWalking = true;
    private boolean isRunning = false; // Enables running when the key is pressed
    private long sprintStartTime;
    private final int sprintDuration = 2000; // 3 seconds for sprint
    private final int spriteSheetNumberOfColumn = 10;
    private long lastTrapCollisionTime = 0;

    public DynamicSprite(double x, double y, Image image, double width, double height) {
        super(x, y, image, width, height);
    }

    public double getHearts() {
        return hearts; // Returns the number of hearts
    }

    // Returns current speed considering running state
    public double getSpeed() {
        double currentSpeed = isRunning ? speed * 2 : speed; // Doubles speed if running
        System.out.println("Current speed: " + currentSpeed);
        return currentSpeed;
    }

    // Sets the sprite's normal speed
    public void setSpeed(double speed) {
        this.speed = speed;
    }

    // Getter for sprint start time
    public long getSprintStartTime() {
        return sprintStartTime;
    }

    // Setter for sprint start time
    public void setSprintStartTime(long sprintStartTime) {
        this.sprintStartTime = sprintStartTime;
    }

    // Getter for sprint duration
    public int getSprintDuration() {
        return sprintDuration;
    }

    private boolean isMovingPossible(ArrayList<Sprite> environment) {
        Rectangle2D.Double moved = new Rectangle2D.Double();
        double currentSpeed = getSpeed(); // Get current speed

        // Calculate new position based on direction
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

        // Check for collisions with other solid objects
        for (Sprite s : environment) {
            if (s instanceof SolidSprite && s != this) { // Ensure s is a SolidSprite and not the current object
                if (((SolidSprite) s).intersect(moved)) {
                    if (((SolidSprite) s).isTrap) { // Check if it's a trap
                        long currentTime = System.currentTimeMillis();
                        // Only decrease hearts if 1 second has passed since last trap hit
                        if (currentTime - lastTrapCollisionTime >= 1000) {
                            hearts--; // Decrease hearts if trap is hit
                            System.out.println("Hearts left: " + hearts);

                            if (hearts <= 0) {
                                System.out.println("Game Over!"); // Game over logic
                            }

                            lastTrapCollisionTime = currentTime; // Update time of last collision
                        }
                    }
                    return false; // Stop movement if collided with solid object
                }
            }
        }

        return true; // Movement possible
    }

    public void setDirection(Direction direction) {
        this.direction = direction; // Set movement direction
    }

    private void move() {
        double currentSpeed = getSpeed(); // Use dynamic speed
        // Update position based on direction
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
            move(); // Move if possible
        }
    }

    // Returns whether the hero is running
    public boolean isRunning() {
        return isRunning;
    }

    public void setRunning(boolean running) {
        if (running && !isRunning) { // Start sprint and record start time
            sprintStartTime = System.currentTimeMillis();
            System.out.println("Sprint started at: " + sprintStartTime);
        }

        if (running) {
            long currentTime = System.currentTimeMillis();
            // Continue running if within sprint duration
            if (currentTime - sprintStartTime < sprintDuration) {
                isRunning = true; // Hero can run
                System.out.println("Hero is running.");
            } else {
                isRunning = false; // Sprint duration over
                System.out.println("Sprint time over. Hero cannot run anymore.");
            }
        } else {
            isRunning = false; // Stop running
            System.out.println("Hero stopped running.");
        }
        System.out.println("Running state changed: " + (isRunning ? "Enabled" : "Disabled"));
    }

    @Override
    public void draw(Graphics g) {
        int index = (int) (System.currentTimeMillis() / timeBetweenFrame % spriteSheetNumberOfColumn);
        // Draw sprite from sprite sheet based on current frame and direction
        g.drawImage(image, (int) x, (int) y, (int) (x + width), (int) (y + height),
                (int) (index * this.width), (int) (direction.getFrameLineNumber() * height),
                (int) ((index + 1) * this.width), (int) ((direction.getFrameLineNumber() + 1) * this.height), null);
    }
}
