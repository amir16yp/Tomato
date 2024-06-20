package game.items;

import game.Game;
import game.Sprite;
import game.entities.player.PlayerEntity;

import java.awt.*;

public class HealPotion extends PickupItem{
    public HealPotion(int maxUsages) {
        super(new Sprite("/game/sprites/items/healpotion.png", 32, 32, 1000, Game.defaultResourceLoader), maxUsages, 50);
    }

    @Override
    public void use() {
        super.use();
        PlayerEntity player = PlayerEntity.getPlayer();
        player.flash(100,30, Color.GREEN);
        player.heal(20);
    }
}
