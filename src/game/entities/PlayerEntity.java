package game.entities;

import game.Direction;
import game.Screen;
import game.Sprite;
import game.ui.DialogueBox;

import java.awt.event.KeyEvent;

public class PlayerEntity extends Entity {
    public Sprite projectileSprite = new Sprite("sprites/player/bullet.png", 8, 8, 100);

    public PlayerEntity() {
        super("Player");
        this.addSprite("north", new Sprite("sprites/player/walk_north.png", 32, 32, 50));
        this.addSprite("east", new Sprite("sprites/player/walk_east.png", 32, 32, 50));
        this.addSprite("west", new Sprite("sprites/player/walk_west.png", 32, 32, 50));
        this.addSprite("south", new Sprite("sprites/player/walk_south.png", 32, 32, 50));
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
        int levelID = currerntTile.doorToLevel;
        if (levelID >= 0) {
            Screen.setCurrentScene(levelID);
            this.setPosition(Screen.getCurrentScene().getSpawnX(), Screen.getCurrentScene().getSpawnY());
        }
    }

    public void handleKeyPressed(int keyCode) {
        //KeyEvent.getKeyText(keyCode);
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
                DialogueBox actionDialogue = (DialogueBox) Screen.getCurrentScene().uiElements.get(0);
                if (this.isFacingWall())
                {
                    actionDialogue.setMessage("It's a wall.");
                } else {
                    actionDialogue.setMessage("Nothing interesting here.");
                }
        }

    }

    public void stopPlayerMoving() {
        stopMoving();
        updateMoveSprite();
        pauseAllAnimationByindex(0);
    }

    public void handleKeyReleased(int keyCode) {
        switch (keyCode) {
            case KeyEvent.VK_W, KeyEvent.VK_S, KeyEvent.VK_D, KeyEvent.VK_A -> stopPlayerMoving();
        }
    }
}
