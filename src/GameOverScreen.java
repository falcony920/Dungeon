import java.awt.*;

public class GameOverScreen {

    public void draw(Graphics g) {
        // Set background color for the Game Over screen
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, 500, 600); // Assuming screen dimensions are 500x600

        // Set text color
        g.setColor(Color.WHITE);

        // Draw "Game Over" message
        Font font = new Font("Arial", Font.BOLD, 48);
        g.setFont(font);
        String gameOverText = "Game Over!";
        FontMetrics metrics = g.getFontMetrics(font);
        int x = (500 - metrics.stringWidth(gameOverText)) / 2;
        int y = 250; // Vertically centered
        g.drawString(gameOverText, x, y);

        /**
         * // Optionally, add a retry or quit message
         * g.setFont(new Font("Arial", Font.PLAIN, 24));
         * String retryMessage = "Press R to Retry";
         * metrics = g.getFontMetrics();
         * x = (500 - metrics.stringWidth(retryMessage)) / 2;
         * g.drawString(retryMessage, x, y + 50);
         * 
         **/
    }
}
