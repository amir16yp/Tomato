package game.entities.enemy;

import game.Direction;
import game.Game;
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

    public void onAllDirectionsBlocked() {
        // Logic when all directions are blocked
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

        if (allDirectionsBlocked) {
            onAllDirectionsBlocked();
            return;
        }

        if (Game.FLAT) {
            // Sidescroller mode: restrict movement to left or right
            Direction horizontalDirection = (currentDirection == Direction.LEFT || currentDirection == Direction.RIGHT) ? currentDirection : Direction.getRandomHorizontalDirection();
            while (isFacingWall(horizontalDirection)) {
                horizontalDirection = Direction.getRandomHorizontalDirection();
            }

            long currentTime = System.currentTimeMillis();
            long timeElapsed = currentTime - timeSinceLastDirectionChange;
            long minTimeBetweenChanges = 1500;  // Adjust as needed (milliseconds)

            if (timeElapsed >= minTimeBetweenChanges) {
                move(horizontalDirection, false);
                timeSinceLastDirectionChange = currentTime; // Update time since last direction change
            }
        } else {
            // Topdown mode: move freely in any direction
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
    }

    public boolean canSee(int x, int y) {
        if (Game.FLAT) {
            // Sidescroller mode line of sight check
            int tileWidth = Screen.getCurrentScene().currentTiles.getWidth();
            int x0 = this.getCurrentX() / tileWidth;
            int x1 = x / tileWidth;

            int dx = Math.abs(x1 - x0);
            int sx = x0 < x1 ? 1 : -1;
            int err = dx / 2;

            while (x0 != x1) {
                if (Screen.getCurrentScene().currentTiles.getTile(x0, 0).isSolid) {
                    return false;
                }
                x0 += sx;
            }
            return true;
        } else {
            // Topdown mode line of sight check
            int tileWidth = Screen.getCurrentScene().currentTiles.getWidth();
            int tileHeight = Screen.getCurrentScene().currentTiles.getHeight();

            int x0 = this.getCurrentX() / tileWidth;
            int y0 = this.getCurrentY() / tileHeight;
            int x1 = x / tileWidth;
            int y1 = y / tileHeight;

            int dx = Math.abs(x1 - x0);
            int dy = Math.abs(y1 - y0);

            int sx = x0 < x1 ? 1 : -1;
            int sy = y0 < y1 ? 1 : -1;

            int err = dx - dy;

            while (true) {
                if (Screen.getCurrentScene().currentTiles.getTile(x0, y0).isSolid) {
                    return false;
                }

                if (x0 == x1 && y0 == y1) {
                    return true;
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
}
