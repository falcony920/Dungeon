import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

public class RenderEngine extends JPanel implements Engine {
    private ArrayList<Displayable> renderList;
    private int frameCount = 0; // Used to calculate frames per second (FPS)
    private long lastTime = System.nanoTime(); // Last time a frame was recorded
    private String fpsText = "FPS: 0"; // Display string for the current FPS

    private Image heartImage;
    private DynamicSprite hero;
    private boolean gameOver = false;

    private SprintBar sprintBar; // Reference to the SprintBar

    public RenderEngine(JFrame jFrame, DynamicSprite hero) {
        renderList = new ArrayList<>();
        this.hero = hero;
        sprintBar = new SprintBar(); // Initialize the SprintBar

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
        if (nb_hearts > 0) {
            if (heartImage != null) {
                for (int i = 0; i < nb_hearts; i++) {
                    g.drawImage(heartImage, 400 + (i * 35), 5, 30, 30, null); // Position hearts horizontally
                }
            }
        } else {
            gameOver = true;
        }

        // Call method to draw the sprint bar from SprintBar class
        sprintBar.draw(g, hero); // Pass the Graphics object and hero instance
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
