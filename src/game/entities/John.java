package game.entities;

import game.Game;
import game.Sprite;
import game.dialogue.DialogueLoader;

public class John extends Entity {
    public John() {
        super("John");
        // temporary
        this.addSprite("north", new Sprite("sprites/zombie/walk_north.png", 32, 32, 100, Game.defaultResourceLoader));
        this.addSprite("east", new Sprite("sprites/zombie/walk_east.png", 32, 32, 100, Game.defaultResourceLoader));
        this.addSprite("west", new Sprite("sprites/zombie/walk_west.png", 32, 32, 100, Game.defaultResourceLoader));
        this.addSprite("south", new Sprite("sprites/zombie/walk_south.png", 32, 32, 100, Game.defaultResourceLoader));
    }

}
