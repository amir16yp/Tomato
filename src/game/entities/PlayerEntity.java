package game.entities;

import game.Direction;
import game.Screen;
import game.Sprite;
import game.ui.DialogueBox;

import java.awt.event.KeyEvent;

public class PlayerEntity extends Entity {

    public static Direction playerDirection;
    public Sprite projectileSprite = new Sprite("sprites/player/bullet.png", 8, 8, 100);
    public static DialogueBox actionDialogue;
    private boolean isInDialogue = false;
    private final double interactionRange = 5.0;
    public PlayerEntity() {
        super("Player");
        this.addSprite("north", new Sprite("sprites/player/walk_north.png", 32, 32, 50));
        this.addSprite("east", new Sprite("sprites/player/walk_east.png", 32, 32, 50));
        this.addSprite("west", new Sprite("sprites/player/walk_west.png", 32, 32, 50));
        this.addSprite("south", new Sprite("sprites/player/walk_south.png", 32, 32, 50));
        this.pauseAllAnimationByindex(0);
        this.setMoveSpeed(3);
    }

    public Direction getCurrentDirection()
    {
        if (playerDirection == null)
        {
            playerDirection = Direction.getRandomDirection();
        }
        return playerDirection;
    }

    public void update() {
        super.update();
        switch (getCurrentDirection()) {
            case Direction.UP -> setCurrentSprite("north");
            case Direction.DOWN -> setCurrentSprite("south");
            case Direction.LEFT -> setCurrentSprite("west");
            case Direction.RIGHT -> setCurrentSprite("east");
        }
        if (actionDialogue != null)
        {
            updateDialogueState();
        }
    }


    public void updatePosition() {
        if (isInDialogue) { return ;}
        super.updatePosition();
        int levelID = currerntTile.doorToLevel;
        if (levelID >= 0) {
            Screen.setCurrentScene(levelID);

            this.setPosition(Screen.getCurrentScene().getSpawnX(), Screen.getCurrentScene().getSpawnY());
        }
    }

    public void handleKeyPressed(int keyCode) {
        //KeyEvent.getKeyText(keyCode);
        if (isInDialogue) { return ;}
        actionDialogue = (DialogueBox) Screen.getCurrentScene().uiElements.get(0);
        switch (keyCode) {
            case KeyEvent.VK_W:
                move(Direction.UP, true);
                break;
            case KeyEvent.VK_S:
                move(Direction.DOWN, true);
                break;
            case KeyEvent.VK_D:
                move(Direction.RIGHT, true);
                break;
            case KeyEvent.VK_A:
                move(Direction.LEFT, true);
                break;
            case KeyEvent.VK_SPACE:
                shootProjectile(projectileSprite, 10, 100);
                break;
            case KeyEvent.VK_R:
                Screen.getCurrentScene().reset();
                break;
            case KeyEvent.VK_X:
                Screen.getCurrentScene().spawnEntity(new Zombie(), 50, 50);
                break;
            case KeyEvent.VK_F:
                if (this.isFacingWall()) {
                    actionDialogue.setMessage("It's a wall.");
                } else {
                    for (Entity entity : Screen.getCurrentScene().entityList)
                    {
                        if (entity instanceof NPC npc)
                        {
                            double distance = distanceTo(entity);
                            if (distance < (npc.distanceToTalk))
                            {
                                actionDialogue.setMessage(npc.interaction());
                                if (npc.hp != 0)
                                {
                                    break;
                                }
                            }
                        }
                    }
                    ///actionDialogue.setMessage("Nothing interesting here.");
                }
                break;
            case KeyEvent.VK_Z:
                Screen.getCurrentScene().spawnEntity(new John(), this.getCurrentX(), this.getCurrentY());
                break;
        }

    }

    public void move(Direction direction, boolean unPause) {
        if (isInDialogue) { return;}
        super.move(direction, unPause);
        playerDirection = direction;
    }
    public void stopPlayerMoving() {
        stopMoving();
        updateMoveSprite();
        pauseAllAnimationByindex(0);
    }

    public void setInDialogue(boolean inDialogue)
    {
        this.isInDialogue = inDialogue;
    }

    public void updateDialogueState()
    {
        setInDialogue(actionDialogue.isVisible());
    }

    public void handleKeyReleased(int keyCode) {
        switch (keyCode) {
            case KeyEvent.VK_W, KeyEvent.VK_S, KeyEvent.VK_D, KeyEvent.VK_A -> stopPlayerMoving();
        }
    }
}
