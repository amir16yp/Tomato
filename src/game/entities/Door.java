package game.entities;

import game.Game;
import game.Scene;
import game.Screen;
import game.Sprite;
import game.entities.player.PlayerEntity;

public class Door extends TileEntity {

    private static final Sprite lowerSprite = new Sprite("/game/sprites/doorbottom.png", 16, 16, 1000, Game.defaultResourceLoader);
    private static final Sprite upperSprite = new Sprite("/game/sprites/doortop.png", 16, 16, 1000, Game.defaultResourceLoader);

    public Door(Scene doorTo) {
        super(new Sprite[][]{{lowerSprite}, {upperSprite}}, PlayerEntity.class);
        this.action = () -> {
            Screen.setCurrentScene(doorTo);
        };
    }
}
