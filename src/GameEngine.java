import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class GameEngine implements Engine, KeyListener {
    DynamicSprite hero;

    public GameEngine(DynamicSprite hero) {
        this.hero = hero; // Initialize hero
    }

    @Override
    public void update() {
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_Z:
                hero.setDirection(Direction.NORTH); // Move up
                break;
            case KeyEvent.VK_S:
                hero.setDirection(Direction.SOUTH); // Move down
                break;
            case KeyEvent.VK_Q:
                hero.setDirection(Direction.WEST); // Move left
                break;
            case KeyEvent.VK_D:
                hero.setDirection(Direction.EAST); // Move right
                break;
            case KeyEvent.VK_UP: // When UP arrow is pressed
                hero.setRunning(true); // Enable running
                System.out.println("Running enabled: " + hero.isRunning());
                break;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_UP) {
            hero.setRunning(false); // Disable running when UP arrow is released
            System.out.println("Running disabled: " + hero.isRunning());
        }
    }
}
