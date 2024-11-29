import java.awt.*;

public class SprintBar {

    private final int sprintBarWidth = 200;
    private final int sprintBarHeight = 20;

    public void draw(Graphics g, DynamicSprite hero) {
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
}
