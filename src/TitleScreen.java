import javax.swing.*;
import java.awt.*;
import javax.imageio.ImageIO;
import java.io.File;

public class TitleScreen extends JPanel {

    JButton startButton;
    Image backgroundImage;

    public TitleScreen() {
        setLayout(new BorderLayout());

        // Load and set the background image
        try {
            backgroundImage = ImageIO.read(new File("./img/titleBackground.png")); // Replace with your image path
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Main panel to center everything vertically
        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setOpaque(false); // Make the panel transparent
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = GridBagConstraints.RELATIVE;
        gbc.insets = new Insets(20, 0, 20, 0); // Add spacing between components
        gbc.anchor = GridBagConstraints.CENTER;

        // Title label
        JLabel titleLabel = new JLabel("Dungeon Crawler", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 36));
        titleLabel.setForeground(Color.WHITE); // Change text color to make it stand out
        mainPanel.add(titleLabel, gbc);

        // Start button
        startButton = new JButton("Start Game");
        startButton.setFont(new Font("Arial", Font.PLAIN, 24));
        mainPanel.add(startButton, gbc);

        add(mainPanel, BorderLayout.NORTH); // Add the main panel to the center
    }

    // Override paintComponent to draw the background image
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (backgroundImage != null) {
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        }
    }
}
