package game.entities.enemy;

import game.Direction;
import game.Screen;
import game.entities.player.PlayerEntity;

public class MeleeEnemyEntity extends EnemyEntity {

    private static final int ATTACK_COOLDOWN = 1000;
    public int walkAnimationSpeed = 100;
    public int attackAnimationSpeed = 200;
    private final double attackRange = 13.0; // Example attack range, adjust as needed
    private final double stopDistance = attackRange; // Stop and attack if within this distance
    private boolean isAttacking = false;
    private long lastAttackTime = 0;

    public MeleeEnemyEntity(String name) {
        super(name);
    }


    public boolean isAttacking() {
        return this.isAttacking;
    }

    public void setAttacking(boolean attacking) {
        this.isAttacking = attacking;
    }

    public void update() {
        super.update();
        if (!isAttacking()) {
            updateMoveSprite();
        }
        updateAI(Screen.getCurrentScene().playerEntity);
    }

    public void updateAI(PlayerEntity player) {
        super.updateAI(true);
        if (this.hp <= 0.00) {
            stopMoving();
            // Optionally, setCurrentSprite("death") here if you have a death sprite;
            return;
        }

        if (player == null) return; // Safety check

        if (!canSeePlayer()) {
            return;
        }

        int deltaX = player.getCurrentX() - this.getCurrentX();
        int deltaY = player.getCurrentY() - this.getCurrentY();
        double distance = Math.sqrt(deltaX * deltaX + deltaY * deltaY);


        if (distance > stopDistance) {
            // Normalize direction vectors for movement
            double normX = deltaX / distance;
            double normY = deltaY / distance;

            // Move directly towards the player if not within stopDistance
            this.velocityX = normX * this.moveSpeed;
            this.velocityY = normY * this.moveSpeed;
            isAttacking = false;

        } else {
            // Stop moving when within stopDistance

            stopMoving();

            // Attack if within attack range
            if (distance <= attackRange) {
                setAttacking(true);
                attack(player);
            } else {
                setAttacking(false);
            }
        }

        // Update sprite direction based on movement or attacking direction
        if (distance > stopDistance || (distance <= attackRange && distance <= stopDistance)) {
            updateSpriteDirection(deltaX / distance, deltaY / distance);
        }
    }

    private void attack(PlayerEntity player) {
        // Check if enough time has passed since the last attack
        long currentTime = System.currentTimeMillis();
        if (currentTime - lastAttackTime < ATTACK_COOLDOWN) {
            return; // Still on cooldown, can't attack yet
        }

        // player.takeDamage returns true if player died as a result of the damage
        /*
        if (player.takeDamage(getDamageAmount())) {
            player.die();
        }
         */
        player.takeDamage(getDamageAmount());
        lastAttackTime = currentTime;

        switch (getCurrentDirection()) {
            case Direction.UP -> setCurrentSprite("attack_north");
            case Direction.DOWN -> setCurrentSprite("attack_south");
            case Direction.LEFT -> setCurrentSprite("attack_west");
            case Direction.RIGHT -> setCurrentSprite("attack_east");
        }
    }


    private void updateSpriteDirection(double normX, double normY) {
        if (Math.abs(normX) > Math.abs(normY)) {
            if (normX > 0) {
                setCurrentDirection(Direction.RIGHT);
                //setCurrentSprite("east");
            } else {
                setCurrentDirection(Direction.LEFT);
                //setCurrentSprite("west");
            }
        } else {
            if (normY > 0) {
                setCurrentDirection(Direction.DOWN);
                //setCurrentSprite("south");
            } else {
                setCurrentDirection(Direction.UP);
                //setCurrentSprite("north");
            }
        }
    }

}
