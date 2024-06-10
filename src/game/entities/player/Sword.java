package game.entities.player;

import game.Screen;
import game.Sprite;
import game.entities.Entity;
import game.entities.enemy.EnemyEntity;

public class Sword extends Item
{
    public Sword() {
        super(new Sprite("/game/sprites/items/sword.png", 32, 32    , 1000), 999, 750);
    }

    @Override
    public void use() {
        super.use();
        PlayerEntity player = Screen.getCurrentScene().playerEntity;
        for (Entity enemyEntity : Screen.getCurrentScene().entityList)
        {
            if (enemyEntity instanceof EnemyEntity)
            {
                if (enemyEntity.distanceTo(player) <= 32.00 * 2)
                {
                    enemyEntity.takeDamage(20);
                    break;
                }
            }
        }
    }
}
