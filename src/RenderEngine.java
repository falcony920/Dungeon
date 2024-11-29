import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

public class RenderEngine extends JPanel implements Engine {
    private ArrayList<Displayable> renderList;
    private int frameCount = 0; // To count frames
    private long lastTime = System.nanoTime(); // Last time frame was rendered
    private String fpsText = "FPS: 0"; // To hold FPS value as string
    private Image heartImage; // Heart image
    private DynamicSprite hero;
    // private long sprintStartTime = 0; // Timestamp when sprint started
    // private final int sprintDuration = 3000; // 3 seconds for sprint
    private final int sprintBarWidth = 150; // Width of the sprint bar
    private final int sprintBarHeight = 15; // Height of the sprint bar

    public RenderEngine(JFrame jFrame, DynamicSprite hero) {
        renderList = new ArrayList<>();
        this.hero = hero;
        try {
            heartImage = ImageIO.read(new File("./img/heart.png")); // Load heart image
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addToRenderList(Displayable displayable) {
        if (!renderList.contains(displayable)) {
            renderList.add(displayable);
        }
    }

    public void addToRenderList(ArrayList<Displayable> displayable) {
        if (!renderList.contains(displayable)) {
            renderList.addAll(displayable);
        }
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);

        // Render game objects
        for (Displayable renderObject : renderList) {
            renderObject.draw(g);
        }

        // Draw FPS text in the top-left corner
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.PLAIN, 18));
        g.drawString(fpsText, 10, 20);

        // Draw hearts next to FPS
        if (heartImage != null) {
            for (int i = 0; i < 3; i++) {
                g.drawImage(heartImage, 400 + (i * 35), 5, 30, 30, null); // Adjust position based on heart size and
                                                                          // spacing
            }
        }

        // Draw sprint bar (below FPS and hearts)
        if (hero.isRunning()) {
            // Use the getter method to access sprintStartTime
            if (hero.getSprintStartTime() == -1) { // If sprintStartTime is -1, sprint has not started yet
                hero.setSprintStartTime(System.currentTimeMillis()); // Start sprinting
                System.out.println("Sprint started at: " + hero.getSprintStartTime()); // Debug: Sprint start time
            }

            long elapsedTime = System.currentTimeMillis() - hero.getSprintStartTime();
            System.out.println("Elapsed time: " + elapsedTime + "ms"); // Debug: Elapsed time since sprint started

            // Use the getter method to access sprintDuration
            int sprintWidth = (int) (sprintBarWidth * (1 - (double) elapsedTime / hero.getSprintDuration()));

            if (elapsedTime >= hero.getSprintDuration()) {
                sprintWidth = 0; // Sprint time is over
                hero.setRunning(false); // Stop the hero from running
                System.out.println("Sprint finished, width reset to: " + sprintWidth); // Debug: Sprint finished
            }

            // Draw the sprint bar
            g.setColor(Color.GREEN);
            g.fillRect(200, 15, sprintWidth, sprintBarHeight); // Draw the sprint bar

            // Debug: Print sprint bar width
            System.out.println("Sprint bar width: " + sprintWidth); // Debug: Current width of the sprint bar
        } else {
            // Debug: Print when hero is not running
            System.out.println("Hero is not running.");
        }

    }

    @Override
    public void update() {
        frameCount++;
        long currentTime = System.nanoTime();
        if (currentTime - lastTime >= 1_000_000_000L) {
            int fps = frameCount;
            fpsText = "FPS: " + fps;
            frameCount = 0;
            lastTime = currentTime;
        }
        this.repaint();
    }
}
