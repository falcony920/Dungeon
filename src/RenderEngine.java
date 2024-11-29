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
    private long sprintStartTime = 0; // Timestamp when sprint started
    private final int sprintDuration = 3000; // 3 seconds for sprint
    private final int sprintBarWidth = 200; // Width of the sprint bar
    private final int sprintBarHeight = 20; // Height of the sprint bar

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
            if (sprintStartTime == 0) {
                sprintStartTime = System.currentTimeMillis(); // Start sprinting
            }
            long elapsedTime = System.currentTimeMillis() - sprintStartTime;
            int sprintWidth = (int) (sprintBarWidth * (1 - (double) elapsedTime / sprintDuration));
            if (elapsedTime >= sprintDuration) {
                sprintWidth = 0; // Sprint time is over
                hero.setRunning(false); // Stop the hero from running
            }
            g.setColor(Color.GREEN);
            g.fillRect(10, 40, sprintWidth, sprintBarHeight); // Draw the sprint bar
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
