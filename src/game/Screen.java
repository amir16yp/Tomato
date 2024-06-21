package game;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

import game.entities.Door;
import game.entities.Entity;
import game.entities.TileEntity;
import game.entities.enemy.Zombie;
import game.entities.player.PlayerEntity;
import game.registry.KeybindRegistry;
import game.items.Item;
import game.registry.OptionsRegistry;
import game.registry.SceneRegistry;
import game.ui.*;
import game.ui.Menu;
import game.ui.SplashScreen;

import static game.Utils.runtime;

public class Screen extends JPanel {
    private static Scene currentScene;
    public static final int TARGET_FRAME_TIME = 30;
    public static Logger logger = new Logger(Screen.class.getName());
    public static boolean isPaused = true;
    public SplashScreen splashScreen = new SplashScreen(Game.INTERNAL_WIDTH, Game.INTERNAL_HEIGHT, Game.defaultResourceLoader, "/game/sprites/splash.png", SplashType.FADE_OUT );

    static {
        SceneRegistry.registry.registerScene(new Scene("tileid.csv", "level.csv", 64, 64, Game.defaultResourceLoader));
        SceneRegistry.registry.registerScene(new Scene("tileid.csv", "level2.csv", 320, 320, Game.defaultResourceLoader));
        Zombie defaultZombie = new Zombie();
        defaultZombie.setPosition(320, 320);
        SceneRegistry.registry.registerEntityInScene(0, defaultZombie);
        Door door = new Door(1);
        door.setX(320);
        door.setY(320);
        SceneRegistry.registry.registerTileEntitiesInScene(0, door);
    }

    public static void setPaused(boolean paused) {
        if (paused != isPaused) {
            logger.Log("Set paused " + paused);
            isPaused = paused;
        }
    }

    public static Menu[] menus = new Menu[]{
            new StartMenu(0, 0, Game.INTERNAL_WIDTH, Game.INTERNAL_HEIGHT),
            new OptionsMenu(0, 0, Game.INTERNAL_WIDTH, Game.INTERNAL_HEIGHT)
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
        showSplashScreen();
    }

    public void showSplashScreen() {
        splashScreen.setVisible(true);
    }

    public void showMainMenu() {
        splashScreen.setVisible(false);
        setCurrentMenu(menus[0]);
        startGameLoop();
        setCurrentScene(0);

    }

    public static void setCurrentMenu(Menu menu) {
        currentMenu.setVisible(false);
        currentMenu = menu;
        currentMenu.setVisible(true);
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
                KeybindRegistry.registry.handleKeyPress(e);
            }

            @Override
            public void keyReleased(KeyEvent e) {
                KeybindRegistry.registry.handleKeyRelease(e);
            }
        };
        addKeyListener(keyAdapter);
    }

    private void startGameLoop() {
        new Timer(TARGET_FRAME_TIME, e -> update()).start();
    }

    private void update() {
        if (splashScreen.splashOver) {
            if (!isPaused) {
                for (Integer keyCode : KeybindRegistry.registry.getKeysHeld())
                {
                    if (KeybindRegistry.registry.isKeyPressed(keyCode))
                    {
                        KeybindRegistry.registry.handleKeyHeld(keyCode);
                    }
                }
                if (currentScene != null)
                {
                    if (currentScene.initalized)
                    {
                        for (Item item : PlayerEntity.inventory.items)
                        {
                            if (item != null)
                            {
                                item.update();
                            }
                        }
                        currentScene.update();
                    }
                }
            }

            for (Mod mod : ModLoader.mods) {
                mod.update();
            }
        }
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        // Calculate scale factors for width and height
        double scaleX = (double) getWidth() / Game.INTERNAL_WIDTH;
        double scaleY = (double) getHeight() / Game.INTERNAL_HEIGHT;

        // Use the larger scale factor to cover the entire area
        //double scaleFactor = Math.max(scaleX, scaleY);

        g2d.scale(scaleX, scaleY); // Scale the graphics context

        if (splashScreen != null && splashScreen.isVisible()) {
            splashScreen.draw(g2d);
        } else {
            if (!isPaused) {
                if (currentScene != null)
                {
                    currentScene.draw(g2d);
                }
                else {
                    setCurrentScene(0);
                }
            } else {
                currentMenu.draw(g2d); // Draw the start menu
            }

            if (OptionsRegistry.registry.getBooleanOption("SHOW_STATS")) {
                long totalMemory = runtime.totalMemory();
                long freeMemory = runtime.freeMemory();
                long usedMemory = totalMemory - freeMemory;
                g2d.setColor(Color.YELLOW);
                g2d.setFont(new Font("Monospaced", Font.PLAIN, 12)); // Set monospaced font properties
                g2d.drawString("Used memory: " + Utils.humanReadableByteCount(usedMemory), 10, 20);
            }

            for (Mod mod : ModLoader.mods) {
                mod.draw(g2d);
            }
        }
    }

    // Method for scene management
    public static void setCurrentScene(int sceneId) {
        Scene scene = SceneRegistry.registry.getScene(sceneId);
        if (scene != null && !scene.initalized) {
            scene.init();
        }
        currentScene = scene;
    }
}