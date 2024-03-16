package game.entities;

import game.Sprite;

public class Zombie extends MeleeEnemyEntity
{
    private static int zombieCount =0;
    public Zombie() {
        //this.setMoveSpeed(1.5);
        super(String.format("zombie_%d", zombieCount));
        zombieCount++;
        this.addSprite("north", new Sprite("sprites/zombie/walk_north.png", 32, 32, walkAnimationSpeed));
        this.addSprite("east", new Sprite("sprites/zombie/walk_east.png", 32, 32, walkAnimationSpeed));
        this.addSprite("west", new Sprite("sprites/zombie/walk_west.png", 32, 32, walkAnimationSpeed));
        this.addSprite("south", new Sprite("sprites/zombie/walk_south.png", 32, 32, walkAnimationSpeed));
        this.addSprite("attack_north", new Sprite("sprites/zombie/attack_north.png", 32, 32, attackAnimationSpeed));
        this.addSprite("attack_east", new Sprite("sprites/zombie/attack_east.png", 32, 32, attackAnimationSpeed));
        this.addSprite("attack_west", new Sprite("sprites/zombie/attack_west.png", 32, 32, attackAnimationSpeed));
        this.addSprite("attack_south", new Sprite("sprites/zombie/attack_south.png", 32, 32, attackAnimationSpeed));
        setDamageAmount(25);
    }

}
