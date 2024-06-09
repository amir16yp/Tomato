package game;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

import game.ui.DialogueBox;
import game.ui.Menu;
import game.ui.OptionsMenu;
import game.ui.StartMenu;

import static game.Utils.runtime;

public class Screen extends JPanel {
    private static final Scene[] SCENES = {
            new Scene("tileid.csv", "level.csv", 320, 320),
            new Scene("tileid.csv", "level2.csv", 320, 320)
    };
    private static Scene currentScene = SCENES[0];
    private static final int TARGET_FRAME_TIME = 30;
    public static Logger logger = new Logger("Screen");
    public static boolean isPaused = true;

    public static void setPaused(boolean paused)
    {
        logger.Log("Set paused " + paused);
        isPaused = paused;
    }

    public static Menu[] menus = new Menu[]
            {
                    new StartMenu(0, 0, Game.WIDTH, Game.HEIGHT),
                    new OptionsMenu(0, 0, Game.WIDTH, Game.HEIGHT)
            };
    public static Menu currentMenu;
    public Screen() {
        setFocusable(true);
        requestFocusInWindow();
        setupKeyListener();
        currentMenu = menus[0];
        for (Menu menu : menus)
        {
            this.addMouseListener(menu);
            this.addMouseMotionListener(menu);
        }
        startGameLoop();
    }

    public static void setCurrentMenu(int index)
    {
        currentMenu.setVisible(false);
        currentMenu = menus[index];
        currentMenu.setVisible(true);
    }



    public void dispose() {
        for (Scene scene : SCENES)
        {
            scene.dispose();
        }
    }

    public static Scene getCurrentScene()
    {
        return currentScene;
    }

    private void setupKeyListener() {
        KeyAdapter playerKeyAdapter = new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (!isPaused) {
                    currentScene.playerEntity.handleKeyPressed(e.getKeyCode());
                    ((DialogueBox) currentScene.uiElements.get(0)).hanndleKeyPressed(e.getKeyCode());
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
                if (!isPaused)
                {
                    currentScene.playerEntity.handleKeyReleased(e.getKeyCode());
                }
            }
        };
        addKeyListener(playerKeyAdapter);

        KeyAdapter menuKeyAdapter = new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                super.keyPressed(e);
                if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                    setPaused(!isPaused);
                    currentMenu.setVisible(isPaused); // Show/hide start menu with pause state
                } else{
                    if (isPaused)
                    {
                        currentMenu.keyPressed(e.getKeyCode());
                    }
                }
            }
        };
        addKeyListener(menuKeyAdapter);

    }

    private void startGameLoop() {
        new Timer(TARGET_FRAME_TIME, e -> update()).start();
    }

    private void update() {
        if (!isPaused)
        {
            currentScene.update();
        }
        repaint();

    }


    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (!isPaused) {
            currentScene.draw(g);
        } else {
            currentMenu.draw(g); // Draw the start menu
        }
        if (OptionsMenu.SHOW_STATS)
        {
            long totalMemory = runtime.totalMemory();
            long freeMemory = runtime.freeMemory();
            long usedMemory = totalMemory - freeMemory;
            g.setColor(Color.YELLOW);
            g.setFont(new Font("Monospaced", Font.PLAIN, 12)); // Set monospaced font properties
            g.drawString("Used memory: " +  Utils.humanReadableByteCount(usedMemory), 10, 20);
        }
    }

    // Example method for scene management (you could expand upon this)
    public static void setCurrentScene(int sceneIndex) {
        logger.Log("Setting scene " + sceneIndex);
        if (sceneIndex >= 0 && sceneIndex < SCENES.length) {
            currentScene = SCENES[sceneIndex];
        } else {
            logger.Error(String.format("Scene index out of bounds (%d)", sceneIndex));
        }
    }
}
