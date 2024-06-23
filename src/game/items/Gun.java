package game.items;

import game.Game;
import game.Screen;
import game.Sprite;
import game.registry.SoundRegistry;

public class Gun extends PickupItem{
    public Gun() {
        super(new Sprite("/game/sprites/items/gun.png", 32, 32, 1000, Game.defaultResourceLoader), 30, 1020);
    }

    @Override
    public void use() {
        super.use();
        Screen.getCurrentScene().playerEntity.shootGun();
    }
}
