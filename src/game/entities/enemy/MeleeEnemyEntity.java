package game.entities.enemy;

import game.Direction;
import game.Game;
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
        updateAI(PlayerEntity.getPlayer());
    }

    public void updateAI(PlayerEntity player) {
        super.updateAI(true); // Always enable wandering for melee enemies
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

        if (Game.FLAT) {
            // Sidescroller mode: restrict movement to left or right
            if (Math.abs(deltaX) > Math.abs(deltaY)) {
                if (deltaX > 0) {
                    // Move right
                    move(Direction.RIGHT, false);
                } else {
                    // Move left
                    move(Direction.LEFT, false);
                }
            } else {
                // No vertical movement in sidescroller mode
                stopMoving();
            }
        } else {
            // Topdown mode: move freely towards the player
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
        }

        // Update sprite direction based on movement or attacking direction
        if (!Game.FLAT || Math.abs(deltaX) > Math.abs(deltaY)) {
            updateSpriteDirection(deltaX, deltaY);
        } else {
            updateSpriteDirection(0, 0); // No vertical sprite direction update in sidescroller mode
        }
    }

    private void attack(PlayerEntity player) {
        // Check if enough time has passed since the last attack
        long currentTime = System.currentTimeMillis();
        if (currentTime - lastAttackTime < ATTACK_COOLDOWN) {
            return; // Still on cooldown, can't attack yet
        }
        player.takeDamage(getDamageAmount());
        lastAttackTime = currentTime;

        Direction currentDirection = getCurrentDirection();

        // Determine the attack sprite based on the current direction
        String attackSprite = null;
        switch (currentDirection) {
            case UP:
                attackSprite = "attack_north";
                break;
            case DOWN:
                attackSprite = "attack_south";
                break;
            case LEFT:
                attackSprite = "attack_west";
                break;
            case RIGHT:
                attackSprite = "attack_east";
                break;
        }

        // Only set the attack sprite if it's different from the current sprite
        if (!getCurrentSprite().getName().equals(attackSprite)) {
            setCurrentSprite(attackSprite);
        }
    }

    private void updateSpriteDirection(int deltaX, int deltaY) {
        if (Math.abs(deltaX) > Math.abs(deltaY)) {
            if (deltaX > 0) {
                setCurrentDirection(Direction.RIGHT);
            } else {
                setCurrentDirection(Direction.LEFT);
            }
        } else {
            if (deltaY > 0) {
                setCurrentDirection(Direction.DOWN);
            } else {
                setCurrentDirection(Direction.UP);
            }
        }
    }

}
