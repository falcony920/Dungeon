import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;

import static javax.swing.WindowConstants.EXIT_ON_CLOSE;

public class Main {

    JFrame displayZoneFrame;
    RenderEngine renderEngine;
    GameEngine gameEngine;
    PhysicEngine physicEngine;

    public Main() throws Exception {
        displayZoneFrame = new JFrame("Dungeon Crawler");
        displayZoneFrame.setSize(500, 600);
        displayZoneFrame.setDefaultCloseOperation(EXIT_ON_CLOSE);

        // Create and show the title screen initially
        TitleScreen titleScreen = new TitleScreen();
        displayZoneFrame.getContentPane().add(titleScreen);
        displayZoneFrame.setVisible(true);

        // Add ActionListener to start game when the button is pressed
        titleScreen.startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    startGame();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
    }

    private void startGame() throws Exception {
        // Remove title screen and initialize the game
        displayZoneFrame.getContentPane().removeAll();

        // Initialize the hero character (DynamicSprite) with its position and
        // spritesheet
        DynamicSprite hero = new DynamicSprite(200, 300,
                ImageIO.read(new File("./img/heroTileSheetLowRes.png")), 48, 50);

        // Initialize the different engines for rendering, physics, and game logic
        renderEngine = new RenderEngine(displayZoneFrame);
        physicEngine = new PhysicEngine();
        gameEngine = new GameEngine(hero);

        // Create timers to update each engine every 50 milliseconds (20 FPS)
        Timer renderTimer = new Timer(50, (time) -> renderEngine.update());
        Timer gameTimer = new Timer(50, (time) -> gameEngine.update());
        Timer physicTimer = new Timer(50, (time) -> physicEngine.update());

        // Start all the timers to keep the game running
        renderTimer.start();
        gameTimer.start();
        physicTimer.start();

        // Add the render engine to the content pane so it can draw everything in the
        // game window
        displayZoneFrame.getContentPane().add(renderEngine);
        displayZoneFrame.revalidate();
        displayZoneFrame.repaint();

        // Load the game level from a text file and create all the sprites (e.g., walls,
        // obstacles)
        Playground level = new Playground("./data/level1.txt");

        // Add all sprites from the level (including traps) to the render engine
        renderEngine.addToRenderList(level.getSpriteList());

        // Add the hero to the list of sprites to be rendered
        renderEngine.addToRenderList(hero);

        // Add the hero to the list of moving sprites managed by the physic engine
        physicEngine.addToMovingSpriteList(hero);

        // Set up the environment (solid objects like walls, traps) and add them for
        // collision detection
        physicEngine.setEnvironment(level.getSolidSpriteList());
        ArrayList<Trap> traps = level.getTrapList();
        // Loop through each trap and add it to the environment
        for (Trap trap : traps) {
            physicEngine.addToEnvironmentList(trap);
        }
        // `getTrapList()` returns a list of traps

        // Add a key listener to capture user input and control the hero character
        renderEngine.addKeyListener(gameEngine);
        renderEngine.setFocusable(true); // Ensure the game panel can receive key events
        renderEngine.requestFocusInWindow(); // Request focus to receive key events
    }

    public static void main(String[] args) throws Exception {
        Main main = new Main();
    }
}
