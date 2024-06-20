package game.entities.player;

import game.Direction;
import game.Game;
import game.Screen;
import game.Sprite;
import game.entities.Door;
import game.entities.Entity;
import game.entities.John;
import game.entities.NPC;
import game.entities.enemy.Zombie;
import game.registry.KeybindRegistry;
import game.items.*;
import game.ui.DialogueBox;
import org.w3c.dom.ranges.DocumentRange;

import java.awt.*;
import java.awt.event.KeyEvent;

public class PlayerEntity extends Entity {

    public Sprite projectileSprite = new Sprite("sprites/player/bullet.png", 8, 8, 100, Game.defaultResourceLoader);
    public static DialogueBox getActionDialogue() {
        return (DialogueBox) Screen.getCurrentScene().uiElements.get(0);
    }
    public static final Item[] starterItems = new Item[10];
    static {
        starterItems[0] = new Gun();
        starterItems[1] = new Sword();
        starterItems[2] = new HealPotion(5);
    }
    public static PlayerInventory inventory = new PlayerInventory(starterItems);
    public PlayerEntity() {
        super("Player");
        this.addSprite("north", new Sprite("sprites/player/walk_north.png", 32, 32, 50, Game.defaultResourceLoader));
        this.addSprite("east", new Sprite("sprites/player/walk_east.png", 32, 32, 50, Game.defaultResourceLoader));
        this.addSprite("west", new Sprite("sprites/player/walk_west.png", 32, 32, 50, Game.defaultResourceLoader));
        this.addSprite("south", new Sprite("sprites/player/walk_south.png", 32, 32, 50, Game.defaultResourceLoader));
        this.pauseAllAnimationByindex(0);
        this.setMoveSpeed(3);
    }

    public void update() {
        super.update();
        switch (getCurrentDirection()) {
            case Direction.UP -> setCurrentSprite("north");
            case Direction.DOWN -> setCurrentSprite("south");
            case Direction.LEFT -> setCurrentSprite("west");
            case Direction.RIGHT -> setCurrentSprite("east");
        }
    }


    public void updatePosition() {
        super.updatePosition();
    }

    public void shootGun()
    {
        shootProjectile(projectileSprite, 10, 25    );
    }

    public static void registerKeybinds() {
        if (!Game.FLAT)
        {
            KeybindRegistry.registry.registerKeyHeldAction(KeyEvent.VK_W, () -> moveWrap(Direction.UP, true));
            KeybindRegistry.registry.registerKeyHeldAction(KeyEvent.VK_S, () -> moveWrap(Direction.DOWN, true));
        }
        KeybindRegistry.registry.registerKeyHeldAction(KeyEvent.VK_D, () -> moveWrap(Direction.RIGHT, true));
        KeybindRegistry.registry.registerKeyHeldAction(KeyEvent.VK_A, () -> moveWrap(Direction.LEFT, true));
        KeybindRegistry.registry.registerKeyPressedAction(KeyEvent.VK_SPACE, () -> inventory.useItem());
        KeybindRegistry.registry.registerKeyPressedAction(KeyEvent.VK_1, () -> inventory.selectItem(0));
        KeybindRegistry.registry.registerKeyPressedAction(KeyEvent.VK_2, () -> inventory.selectItem(1));
        KeybindRegistry.registry.registerKeyPressedAction(KeyEvent.VK_3, () -> inventory.selectItem(2));
        KeybindRegistry.registry.registerKeyPressedAction(KeyEvent.VK_4, () -> inventory.selectItem(3));
        KeybindRegistry.registry.registerKeyPressedAction(KeyEvent.VK_5, () -> inventory.selectItem(4));
        KeybindRegistry.registry.registerKeyPressedAction(KeyEvent.VK_6, () -> inventory.selectItem(5));
        KeybindRegistry.registry.registerKeyPressedAction(KeyEvent.VK_7, () -> inventory.selectItem(6));
        KeybindRegistry.registry.registerKeyPressedAction(KeyEvent.VK_8, () -> inventory.selectItem(7));
        KeybindRegistry.registry.registerKeyPressedAction(KeyEvent.VK_9, () -> inventory.selectItem(8));
        KeybindRegistry.registry.registerKeyPressedAction(KeyEvent.VK_0, () -> inventory.selectItem(9));
        KeybindRegistry.registry.registerKeyPressedAction(KeyEvent.VK_R, () -> {
            Screen.getCurrentScene().reset();
            inventory = new PlayerInventory(starterItems);
        });
        KeybindRegistry.registry.registerKeyPressedAction(KeyEvent.VK_X, () -> Screen.getCurrentScene().spawnEntity(new Zombie(), 50, 50));
        KeybindRegistry.registry.registerKeyPressedAction(KeyEvent.VK_F, () -> {
            if (isFacingWallWrap()) {
                getActionDialogue().setMessage("It's a wall.");
            } else {
                for (Entity entity : Screen.getCurrentScene().entityList) {
                    if (entity instanceof NPC npc) {
                        double distance = distanceToWrap(entity);
                        if (distance < (npc.distanceToTalk)) {
                            getActionDialogue().setMessage(npc.interaction());
                            if (npc.hp != 0) {
                                break;
                            }
                        }
                    }
                }
            }
        });

        KeybindRegistry.registry.registerKeyPressedAction(KeyEvent.VK_Z, () -> {
            getPlayer().flash(2000, 50, Color.RED);
        });

        KeybindRegistry.registry.registerKeyPressedAction(KeyEvent.VK_C, () -> {
            System.out.println(getPlayer().getCurrentX() + "," + getPlayer().getCurrentY());
        });

        KeybindRegistry.registry.registerKeyReleasedAction(KeyEvent.VK_W, PlayerEntity::stopPlayerMovingWrap);
        KeybindRegistry.registry.registerKeyReleasedAction(KeyEvent.VK_S, PlayerEntity::stopPlayerMovingWrap);
        KeybindRegistry.registry.registerKeyReleasedAction(KeyEvent.VK_D, PlayerEntity::stopPlayerMovingWrap);
        KeybindRegistry.registry.registerKeyReleasedAction(KeyEvent.VK_A, PlayerEntity::stopPlayerMovingWrap);
    }

    public static PlayerEntity getPlayer() throws NullPointerException
    {
        return Screen.getCurrentScene().playerEntity;
    }

    private static boolean isFacingWallWrap()
    {
        return getPlayer().isFacingWall();
    }

    private static void stopPlayerMovingWrap()
    {
        getPlayer().stopPlayerMoving();
    }

    private static double distanceToWrap(Entity entity)
    {
        return getPlayer().distanceTo(entity);
    }

    private static void moveWrap(Direction direction, boolean unPause)
    {
        getPlayer().currentDirection = direction;
        getPlayer().move(direction, unPause);
    }

    public void move(Direction direction, boolean unPause) {
        super.move(direction, unPause);
    }
    public void stopPlayerMoving() {
        stopMoving();
        updateMoveSprite();
        pauseAllAnimationByindex(0);
    }


}
