package game;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

import game.entities.player.PlayerEntity;
import game.input.KeybindRegistry;
import game.ui.*;
import game.ui.Menu;

import static game.Utils.runtime;

public class Screen extends JPanel {
    public static List<Scene> scenes = new ArrayList<>();
    private static Scene currentScene;
    private static final int TARGET_FRAME_TIME = 30;
    public static Logger logger = new Logger(Screen.class.getName());
    public static boolean isPaused = true;

    static {
        // Initial scenes
        scenes.add(new Scene("tileid.csv", "level.csv", 320, 320, Game.defaultResourceLoader));
        scenes.add(new Scene("tileid.csv", "level2.csv", 320, 320, Game.defaultResourceLoader));
        currentScene = scenes.getFirst();
    }

    public static void setPaused(boolean paused) {
        if (paused != isPaused) {
            logger.Log("Set paused " + paused);
            isPaused = paused;
        }
    }

    public static Menu[] menus = new Menu[]{
            new StartMenu(0, 0, Game.WIDTH, Game.HEIGHT),
            new OptionsMenu(0, 0, Game.WIDTH, Game.HEIGHT)
    };
    public static List<Menu> modMenus = new ArrayList<>();
    public static Menu currentMenu = menus[0];

    public Screen() {
        setFocusable(true);
        requestFocusInWindow();
        setupKeyListener();
        currentMenu = menus[0];
        for (Menu menu : menus) {
            this.addMouseListener(menu);
            this.addMouseMotionListener(menu);
        }
        startGameLoop();
    }

    public static void setCurrentMenu(Menu menu) {
        currentMenu.setVisible(false);
        currentMenu = menu;
        currentMenu.setVisible(true);
    }

    public void dispose() {
        for (Scene scene : scenes) {
            scene.dispose();
        }
    }

    public static Scene getCurrentScene() {
        return currentScene;
    }

    private void setupKeyListener() {
        KeybindRegistry.registry.registerKeyPressedAction(KeyEvent.VK_ESCAPE, () -> {
            setPaused(!isPaused);
            currentMenu.setVisible(isPaused);
        });

        PlayerEntity.registerKeybinds();
        Menu.registerKeybinds();
        DialogueBox.registerKeybinds();

        KeyAdapter keyAdapter = new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                KeybindRegistry.registry.handleKeyPress(e.getKeyCode());
            }

            @Override
            public void keyReleased(KeyEvent e) {
                KeybindRegistry.registry.handleKeyRelease(e.getKeyCode());
            }
        };
        addKeyListener(keyAdapter);
    }

    private void startGameLoop() {
        new Timer(TARGET_FRAME_TIME, e -> update()).start();
    }

    private void update() {
        if (!isPaused) {
            currentScene.update();
        }
        for (Mod mod : ModLoader.mods) {
            mod.update();
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
        if (OptionsMenu.SHOW_STATS) {
            long totalMemory = runtime.totalMemory();
            long freeMemory = runtime.freeMemory();
            long usedMemory = totalMemory - freeMemory;
            g.setColor(Color.YELLOW);
            g.setFont(new Font("Monospaced", Font.PLAIN, 12)); // Set monospaced font properties
            g.drawString("Used memory: " + Utils.humanReadableByteCount(usedMemory), 10, 20);
        }
        for (Mod mod : ModLoader.mods) {
            mod.draw(g);
        }
    }

    // Method for scene management
    public static void setCurrentScene(Scene scene) {
        currentScene = scene;
    }

    // Method to add a new scene dynamically
    public static void addScene(Scene scene) {
        scenes.add(scene);
        logger.Log("Added new scene: " + scene);
    }
}
