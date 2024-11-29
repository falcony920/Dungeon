
import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

public class DynamicSprite extends SolidSprite {
    private Direction direction = Direction.EAST;
    private double speed = 5;
    private int hearts = 3;
    private double timeBetweenFrame = 250;
    private boolean isWalking = true;
    private boolean isRunning = false; // on fera accélérer le perso en appuyant sur la touche z
    private long sprintStartTime;
    private final int sprintDuration = 2000; // 3 seconds for sprint
    private final int spriteSheetNumberOfColumn = 10;
    private long lastTrapCollisionTime = 0;

    public DynamicSprite(double x, double y, Image image, double width, double height) {
        super(x, y, image, width, height);
    }

    public double getHearts() {
        return hearts;
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

    // Getter for sprintStartTime
    public long getSprintStartTime() {
        return sprintStartTime;
    }

    // Setter for sprintStartTime
    public void setSprintStartTime(long sprintStartTime) {
        this.sprintStartTime = sprintStartTime;
    }

    // Getter for sprintDuration
    public int getSprintDuration() {
        return sprintDuration;
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
            if (s instanceof SolidSprite && s != this) { // Ensure s is a SolidSprite and not the current object
                if (((SolidSprite) s).intersect(moved)) {
                    // Check if it's a trap
                    if (((SolidSprite) s).isTrap) {
                        long currentTime = System.currentTimeMillis();

                        // Only decrease hearts if 1 second has passed since last trap hit
                        if (currentTime - lastTrapCollisionTime >= 1000) {
                            System.out.println("Collision detected with a trap!");
                            hearts--; // Decrease hearts if a trap is hit
                            System.out.println("Hearts left: " + hearts);

                            if (hearts <= 0) {
                                System.out.println("Game Over!");
                                // Implement any game-over logic here
                            }

                            // Update the time of the last trap collision
                            lastTrapCollisionTime = currentTime;
                        }
                    }
                    return false; // Stop further movement after solid object collision
                }
            }
        }

        /**
         * // Check for collisions with traps and decrease hearts if collided
         * for (Sprite s : environment) {
         * if (s instanceof SolidSprite) { // Assuming traps are also SolidSprites
         * if (((SolidSprite) s).intersect(moved)) {
         * // Debug: Print the position and the hit box of the trap and the hero
         * System.out.println("Collision detected!");
         * System.out.println("Hero position: x = " + this.x + ", y = " + this.y);
         * // System.out.println("Trap position: x = " + s.getX() + ", y = " +
         * s.getY());
         * System.out.println("Hero hit box: " + moved);
         * System.out.println("Trap hit box: " + ((SolidSprite) s).getHitBox());
         * 
         * // Decrease hearts when a trap is encountered
         * hearts--;
         * System.out.println("Hit a trap! Hearts left: " + hearts);
         * 
         * // Check if the hero's hearts are depleted
         * if (hearts <= 0) {
         * System.out.println("Game Over!");
         * // Implement any game-over logic you need
         * }
         * 
         * break;
         * }
         * }
         * }
         **/

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

    public void setRunning(boolean running) {
        // Si le héros commence à courir
        if (running && !isRunning) {
            // Commence le sprint et enregistre le temps de départ
            sprintStartTime = System.currentTimeMillis();
            System.out.println("Sprint started at: " + sprintStartTime); // Debug : afficher le début du sprint
        }

        // Vérifie si le sprint peut continuer
        if (running) {
            long currentTime = System.currentTimeMillis();
            // Si le temps écoulé depuis le début du sprint est inférieur à la durée
            // autorisée, on peut continuer
            if (currentTime - sprintStartTime < sprintDuration) {
                isRunning = true; // Le héros peut courir
                System.out.println("Hero is running."); // Debug : afficher si le héros court
            } else {
                isRunning = false; // La barre de sprint est vide
                System.out.println("Sprint time over. Hero cannot run anymore."); // Debug : afficher que le sprint est
                                                                                  // terminé
            }
        } else {
            // Si le héros arrête de courir, on arrête le sprint
            isRunning = false;
            System.out.println("Hero stopped running."); // Debug : afficher si le héros arrête de courir
        }
        // Affiche le nouvel état de la course
        System.out.println("Course changée : " + (isRunning ? "Activée" : "Désactivée"));
    }

    /**
     * // Check for collision with traps
     * public void checkForTrapCollision(ArrayList<Trap> traps) {
     * Rectangle heroBounds = this.getBounds();
     * for (Trap trap : traps) {
     * if (trap.getBounds().intersects(heroBounds)) {
     * hearts--;
     * traps.remove(trap);
     * System.out.println("Hero hit a trap! Health: " + hearts);
     * if (hearts <= 0) {
     * System.out.println("Game Over!");
     * // Implement game-over logic
     * }
     * break;
     * }
     * }
     * }
     **/

    @Override
    public void draw(Graphics g) {
        int index = (int) (System.currentTimeMillis() / timeBetweenFrame % spriteSheetNumberOfColumn);

        g.drawImage(image, (int) x, (int) y, (int) (x + width), (int) (y + height),
                (int) (index * this.width), (int) (direction.getFrameLineNumber() * height),
                (int) ((index + 1) * this.width), (int) ((direction.getFrameLineNumber() + 1) * this.height), null);
    }
}
