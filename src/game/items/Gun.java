package game.items;

import game.Screen;
import game.Sprite;

public class Gun extends PickupItem{
    public Gun() {
        super(new Sprite("/game/sprites/items/gun.png", 32, 32, 1000), 30, 100);
    }

    @Override
    public void use() {
        super.use();
        Screen.getCurrentScene().playerEntity.shootGun();
    }
}
