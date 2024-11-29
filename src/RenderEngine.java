import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

public class RenderEngine extends JPanel implements Engine {
    private ArrayList<Displayable> renderList; // Holds all objects to be rendered
    private int frameCount = 0; // Used to calculate frames per second (FPS)
    private long lastTime = System.nanoTime(); // Last time a frame was recorded
    private String fpsText = "FPS: 0"; // Display string for the current FPS
    private Image heartImage; // Image of a heart icon, used for displaying health
    private DynamicSprite hero; // Reference to the hero sprite
    private final int sprintBarWidth = 200; // Width of the sprint bar
    private final int sprintBarHeight = 20; // Height of the sprint bar

    private boolean gameOver = false;

    public RenderEngine(JFrame jFrame, DynamicSprite hero) {
        renderList = new ArrayList<>();
        this.hero = hero;
        try {
            // Load the heart image for the health display
            heartImage = ImageIO.read(new File("./img/heart.png"));
        } catch (IOException e) {
            e.printStackTrace(); // Print error if the image fails to load
        }
    }

    public void addToRenderList(Displayable displayable) {
        // Avoid duplicate entries in the render list
        if (!renderList.contains(displayable)) {
            renderList.add(displayable);
        }
    }

    public void addToRenderList(ArrayList<Displayable> displayable) {
        // Add multiple objects to the render list at once
        if (!renderList.contains(displayable)) {
            renderList.addAll(displayable);
        }
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g); // Clear the previous frame

        // Render all objects in the render list
        for (Displayable renderObject : renderList) {
            renderObject.draw(g);
        }

        // Draw FPS in the top-left corner
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.PLAIN, 18));
        g.drawString(fpsText, 10, 20);

        // Draw heart icons representing health
        double nb_hearts = hero.getHearts();
        System.out.println("la vie du héros est " + nb_hearts);
        if (nb_hearts > 0) {
            if (heartImage != null) {
                for (int i = 0; i < nb_hearts; i++) {
                    g.drawImage(heartImage, 400 + (i * 35), 5, 30, 30, null); // Position hearts horizontally
                }
            }
        } else {
            gameOver = true;
        }

        // Call method to draw the sprint bar
        drawSprintBar(g);
    }

    // Method to render the sprint bar with a dynamic width and percentage
    private void drawSprintBar(Graphics g) {
        if (hero.isRunning()) {
            // Initialize sprint start time if it's not already set
            if (hero.getSprintStartTime() == -1) {
                hero.setSprintStartTime(System.currentTimeMillis());
            }

            // Calculate how much of the sprint time has elapsed
            long elapsedTime = System.currentTimeMillis() - hero.getSprintStartTime();
            int sprintWidth = (int) (sprintBarWidth * (1 - (double) elapsedTime / hero.getSprintDuration()));

            // Stop sprinting if the duration is complete
            if (elapsedTime >= hero.getSprintDuration()) {
                sprintWidth = 0;
                hero.setRunning(false);
            }

            // Draw a green sprint bar with rounded corners
            Graphics2D g2d = (Graphics2D) g;
            g2d.setColor(new Color(34, 139, 34));
            g2d.fillRoundRect(150, 10, sprintWidth, sprintBarHeight, 10, 10);

            // Draw a dark green border around the sprint bar
            g2d.setColor(new Color(0, 100, 0));
            g2d.setStroke(new BasicStroke(2));
            g2d.drawRoundRect(150, 10, sprintWidth, sprintBarHeight, 10, 10);

            // Display the remaining sprint percentage above the bar
            g.setColor(Color.WHITE);
            g.setFont(new Font("Arial", Font.BOLD, 14));
            String sprintPercentage = String.format("%.0f%%",
                    (1 - (double) elapsedTime / hero.getSprintDuration()) * 100);
            g.drawString(sprintPercentage,
                    150 + sprintWidth / 2 - g.getFontMetrics().stringWidth(sprintPercentage) / 2,
                    10 + sprintBarHeight / 2 + 5);
        }
    }

    @Override
    public void update() {
        if (gameOver) {

            // Draw the game-over screen
            GameOverScreen gameOverScreen = new GameOverScreen();
            gameOverScreen.draw(getGraphics());
        } else {
            // Normal game update logic (e.g., rendering, physics)

            frameCount++; // Increment frame count every frame
            long currentTime = System.nanoTime();
            // Update FPS every second
            if (currentTime - lastTime >= 1_000_000_000L) {
                fpsText = "FPS: " + frameCount;
                frameCount = 0;
                lastTime = currentTime;
            }
            this.repaint(); // Trigger a repaint to refresh the screen
        }
    }
}
