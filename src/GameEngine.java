
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class GameEngine implements Engine, KeyListener {
    DynamicSprite hero;

    public GameEngine(DynamicSprite hero) {
        this.hero = hero;
    }

    @Override
    public void update() {
        // Logique de mise à jour du jeu
    }

    @Override
    public void keyTyped(KeyEvent e) {
        // Ne fait rien pour le moment
    }

    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_Z:
                hero.setDirection(Direction.NORTH);
                break;
            case KeyEvent.VK_S:
                hero.setDirection(Direction.SOUTH);
                break;
            case KeyEvent.VK_Q:
                hero.setDirection(Direction.WEST);
                break;
            case KeyEvent.VK_D:
                hero.setDirection(Direction.EAST);
                break;
            case KeyEvent.VK_UP: // Lorsqu'on appuie sur la touche CTRL
                hero.setRunning(true); // Active la course
                System.out.println("Course activée : " + hero.isRunning());
                break;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_UP) {
            hero.setRunning(false); // Désactive la course lorsque CTRL est relâché
            System.out.println("Course désactivée : " + hero.isRunning());
        }
    }
}
