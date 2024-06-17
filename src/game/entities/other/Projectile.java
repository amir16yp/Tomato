package game.entities.other;

import game.Direction;
import game.Game;
import game.Screen;
import game.Sprite;
import game.entities.Entity;

import java.awt.*;

public class Projectile {
    private final Direction direction; // Direction of the projectile
    private final Sprite sprite; // The sprite for this projectile
    private final double damage;
    private final Entity shooter;
    private int speed; // Speed of the projectile
    private int x, y; // Current position of the projectile
    private boolean active = true; // State of the projectile (active/inactive)

    public Projectile(int startX, int startY, Direction direction, int speed, Sprite sprite, double damageAmount, Entity shooter) {
        this.x = startX;
        this.y = startY;
        this.direction = direction;
        setSpeed(speed);
        this.sprite = sprite;
        this.damage = damageAmount;
        this.shooter = shooter;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public Direction getDirection() {
        return this.direction;
    }


    public Entity getShooter() {
        return this.shooter;
    }


    // Move the projectile
    public void update() {
        if (!active) {
            return;
        }

        x += getDirection().getDeltaX() * speed;
        y += getDirection().getDeltaY() * speed;

        // Check for collisions or if the projectile goes out of bounds
        if (x < 0 || x > Game.WIDTH || y < 0 || y > Game.HEIGHT) {
            deactivate();
        }

        checkBoundaryCollision();
        checkEntityCollision();
    }

    private void checkEntityCollision() {
        Rectangle proposedHitbox = new Rectangle(x, y, this.sprite.getWidth() * Game.SCALE_FACTOR, this.sprite.getHeight() * Game.SCALE_FACTOR);

        for (Entity entity : Screen.getCurrentScene().entityList) {
            if (entity.hitbox.intersects(proposedHitbox) && entity != getShooter()) {
                entity.takeDamage(getDamageAmount());
                deactivate();
                return;
            }
        }
    }

    private void checkBoundaryCollision() {
        Rectangle proposedHitbox = new Rectangle(x, y, this.sprite.getWidth() * Game.SCALE_FACTOR, this.sprite.getHeight() * Game.SCALE_FACTOR);

        for (Rectangle rectangle : Screen.getCurrentScene().boundaries) {
            if (proposedHitbox.intersects(rectangle)) {
                deactivate();
                break;
            }
        }
    }

    // Draw the projectile
    public void draw(Graphics g) {
        if (!active) return;

        // Use the sprite to draw the projectile
        sprite.draw(g, getX(), getY());
    }

    // Check if the projectile is active
    public boolean isActive() {
        return active;
    }

    // Deactivate the projectile (e.g., after a collision)
    public void deactivate() {
        this.active = false;
    }

    // Getters for the projectile's current position
    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public double getDamageAmount() {
        return damage;
    }
}
