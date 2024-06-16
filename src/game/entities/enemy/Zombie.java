package game.entities.enemy;

import game.Game;
import game.Sprite;

public class Zombie extends MeleeEnemyEntity
{
    private static int zombieCount =0;
    public Zombie() {
        //this.setMoveSpeed(1.5);
        super("zombie");
        this.logger.addPrefix(String.valueOf(zombieCount));
        zombieCount++;
        this.addSprite("north", new Sprite("sprites/zombie/walk_north.png", 32, 32, walkAnimationSpeed, Game.defaultResourceLoader));
        this.addSprite("east", new Sprite("sprites/zombie/walk_east.png", 32, 32, walkAnimationSpeed, Game.defaultResourceLoader));
        this.addSprite("west", new Sprite("sprites/zombie/walk_west.png", 32, 32, walkAnimationSpeed, Game.defaultResourceLoader));
        this.addSprite("south", new Sprite("sprites/zombie/walk_south.png", 32, 32, walkAnimationSpeed, Game.defaultResourceLoader));
        this.addSprite("attack_north", new Sprite("sprites/zombie/attack_north.png", 32, 32, attackAnimationSpeed, Game.defaultResourceLoader));
        this.addSprite("attack_east", new Sprite("sprites/zombie/attack_east.png", 32, 32, attackAnimationSpeed, Game.defaultResourceLoader));
        this.addSprite("attack_west", new Sprite("sprites/zombie/attack_west.png", 32, 32, attackAnimationSpeed, Game.defaultResourceLoader));
        this.addSprite("attack_south", new Sprite("sprites/zombie/attack_south.png", 32, 32, attackAnimationSpeed, Game.defaultResourceLoader));
        setDamageAmount(25);
    }

}
