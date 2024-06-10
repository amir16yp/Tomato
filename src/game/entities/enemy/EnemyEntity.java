package game.entities.enemy;

import game.Direction;
import game.Screen;
import game.entities.Entity;

public class EnemyEntity extends Entity {
    private long timeSinceLastDirectionChange = 0;
    private boolean enableWandering = false;
    private final double wanderingSpeed = 2;
    private final double runToPlayerSpeed = 3;
    private double damageAmount = 10.0;
    public EnemyEntity(String name) {
        super(name);
        currentDirection = Direction.getRandomDirection();
    }

    public double getDamageAmount() {
        return damageAmount;
    }

    public void setDamageAmount(double damageAmount) {
        this.damageAmount = damageAmount;
    }

    public boolean canSeePlayer() {
        return canSee(Screen.getCurrentScene().playerEntity.getCurrentX(), Screen.getCurrentScene().playerEntity.getCurrentY());
    }

    public void onAllDirectionsBlocked()
    {

    }

    public void updateAI(boolean enableIdleWandering) {
        enableWandering = enableIdleWandering;
        if (!enableWandering) {
            setMoveSpeed(runToPlayerSpeed);
            return;  // Exit if idle wandering is not enabled
        }
        setMoveSpeed(wanderingSpeed);

        boolean allDirectionsBlocked = true;
        for (Direction dir : Direction.values()) {
            if (!isFacingWall(dir)) {
                allDirectionsBlocked = false;
                break;
            }
        }

        if (allDirectionsBlocked)
        {
            onAllDirectionsBlocked();
            return;
        }

        Direction randomDirection = Direction.getRandomDirection();
        while (isFacingWall(randomDirection)) {
            randomDirection = Direction.getRandomDirection();
        }

        long currentTime = System.currentTimeMillis();
        long timeElapsed = currentTime - timeSinceLastDirectionChange;
        long minTimeBetweenChanges = 1500;  // Adjust as needed (milliseconds)

        if (timeElapsed >= minTimeBetweenChanges) {
            move(randomDirection, false);
            timeSinceLastDirectionChange = currentTime; // Update time since last direction change
        }
    }

    public boolean canSee(int x, int y) {
        int tileWidth = Screen.getCurrentScene().currentTiles.getWidth();
        int tileHeight = Screen.getCurrentScene().currentTiles.getHeight();

        // Bresenham's line algorithm for line of sight check
        int x0 = this.getCurrentX() / tileWidth;
        int y0 = this.getCurrentY() / tileHeight;
        int x1 = x / tileWidth;
        int y1 = y / tileHeight;

        int dx = Math.abs(x1 - x0);
        int dy = Math.abs(y1 - y0);

        int sx = (x0 < x1) ? 1 : -1;
        int sy = (y0 < y1) ? 1 : -1;

        int err = dx - dy;

        while (true) {
            // Check if current tile is solid (blocking sight)
            if (Screen.getCurrentScene().currentTiles.getTile(x0, y0).isSolid) {
                return false;
            }

            if (x0 == x1 && y0 == y1) {
                return true; // Reached the player position with clear line of sight
            }

            int e2 = 2 * err;

            if (e2 > -dy) {
                err -= dy;
                x0 += sx;
            }

            if (e2 < dx) {
                err += dx;
                y0 += sy;
            }
        }
    }

}