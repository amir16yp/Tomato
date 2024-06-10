package game.entities.player;

import game.Screen;
import game.Sprite;

public class Gun extends Item{
    public Gun() {
        super(new Sprite("/game/sprites/items/gun.png", 32, 32, 1000), 999, 500);
    }

    @Override
    public void use() {
        super.use();
        Screen.getCurrentScene().playerEntity.shootGun();
    }
}
