package game.entities;

import game.*;
import game.entities.other.HealthBar;
import game.entities.other.Projectile;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Entity {
    private final Map<String, Sprite> sprites;
    private final List<Projectile> shotProjectiles = new ArrayList<>();
    private final boolean healthBarVisible = true;
    private final HealthBar healthBar;
    private final boolean canShoot = true;
    private final long shotDelay = 250;
    public double moveSpeed = 2; // Speed of movement
    private String name;
    public double velocityX; // Horizontal velocity of the entity
    public double velocityY; // Vertical velocity of the entity
    public double hp = 100.0;
    public Rectangle hitbox;
    public Tile currerntTile;
    private Sprite currentSprite;
    private int x; // Current x-coordinate of the entity
    private int y; // Current y-coordinate of the entity
    private long lastShotTime = 0;
    public Direction currentDirection;
    public Logger logger = new Logger(this.getClass().getName());
    public Entity(String name) {
        logger.addPrefix(name);
        this.name = name;
        sprites = new HashMap<>();
        hitbox = new Rectangle(getCurrentX(), getCurrentY(), 32, 32);
        healthBar = new HealthBar(100, 20, 5);
        currentDirection = Direction.getRandomDirection();
        logger.Log("Created entity");
    }


    // Add a sprite to the entity
    public void addSprite(String name, Sprite sprite) {
        sprites.put(name, sprite);
        if (currentSprite == null) {
            currentSprite = sprite;
        }
    }

    // Set the current sprite of the entity
    public void setCurrentSprite(String name) {
        //logger.Log("Setting sprite to " + name);
        currentSprite = sprites.get(name);
        if (currentSprite == null) {
            logger.Error("Sprite with name '" + name + "' does not exist.");
        }
    }

    public Direction getCurrentDirection() {
        return currentDirection;
    }

    public void setCurrentDirection(Direction direction)
    {
        this.currentDirection = direction;
    }

    // Draw the entity at its current position
    public void draw(Graphics g) {
        if (currentSprite != null) {
            currentSprite.draw(g, x, y);
        }
        drawProjectiles(g);
        if (this.healthBarVisible) {
            healthBar.draw(g, x, y - 10);
        }
    }

    // Update the current sprite
    public void update() {
        hitbox.y = y;
        hitbox.x = x;
        if (currentSprite != null) {
            currentSprite.update();
        }
        updatePosition();
        updateProjectiles();
        healthBar.setCurrentHealth((int) hp);
    }

    public void updateProjectiles() {
        List<Projectile> inactiveProjectiles = new ArrayList<>();

        // Update projectiles
        for (Projectile projectile : shotProjectiles) {
            projectile.update();
            if (!projectile.isActive()) {
                inactiveProjectiles.add(projectile);
            }
        }
        /*
        if (!inactiveProjectiles.isEmpty())
        {
            logger.Log("removing " + inactiveProjectiles.size() + " projectiles");
        }
         */
        // Remove inactive projectiles
        shotProjectiles.removeAll(inactiveProjectiles);
    }


    public void drawProjectiles(Graphics g) {
        for (Projectile projectile : shotProjectiles) {
            if (projectile.isActive()) {
                projectile.draw(g);
            }
        }
    }

    public void shootProjectile(Sprite projectileSprite, int speed, int damage) {
        // Ensure we have a direction before shooting
        if (currentDirection == null || !canShoot || !isTimeToShoot()) return;

        // Create a new projectile
        Projectile newProjectile = new Projectile(x, y, currentDirection, speed, projectileSprite, damage, this);

        // Add the new projectile to the list of projectiles
        shotProjectiles.add(newProjectile);
        lastShotTime = System.currentTimeMillis();
    }

    private boolean isTimeToShoot() {
        long currentTime = System.currentTimeMillis();
        return (currentTime - lastShotTime) >= shotDelay;
    }

    public void pauseAllAnimationByindex(int index) {
        for (Sprite sprite : sprites.values()) {
            sprite.pauseAnimationByFrameIndex(index);
        }
    }

    public void unPauseAnimation() {
        for (Sprite sprite : sprites.values()) {
            sprite.unPauseAnimation();
        }
    }

    public void updateMoveSprite() {
        switch (currentDirection) {
            case Direction.UP -> setCurrentSprite("north");
            case Direction.DOWN -> setCurrentSprite("south");
            case Direction.LEFT -> setCurrentSprite("west");
            case Direction.RIGHT -> setCurrentSprite("east");
        }
    }


    public void move(Direction direction, boolean unPause) {
        this.currentDirection = direction;
        if (unPause) {
            unPauseAnimation();
        }
        // Calculate the displacement based on the move speed and direction
        double displacementX = direction.getDeltaX() * moveSpeed;
        double displacementY = direction.getDeltaY() * moveSpeed;
        // Update the entity's velocity
        velocityX = displacementX;
        velocityY = displacementY;

    }

    private boolean checkCollision(int newX, int newY) {
        Rectangle proposedHitbox = new Rectangle(newX, newY, hitbox.width, hitbox.height);
        for (Rectangle boundary : Screen.getCurrentScene().boundaries) {
            if (proposedHitbox.intersects(boundary)) {
                return true; // Collision detected with a boundary
            }
        }
        return false; // No collision with any boundaries
    }

    public void stopMoving() {
        velocityX = 0;
        velocityY = 0;
    }

    // Update the entity's position based on its velocity
    public void updatePosition() {


        int proposedX = (int) (x + velocityX);
        int proposedY = (int) (y + velocityY);
        if (!checkCollision(proposedX, proposedY)) {
            setPosition(proposedX, proposedY);
        }
        int width = Screen.getCurrentScene().currentTiles.getWidth();
        int height = Screen.getCurrentScene().currentTiles.getHeight();

        currerntTile = Screen.getCurrentScene().currentTiles.getTile(x / width, y / height);
    }
    public boolean isFacingWall(Direction direction) {
        // Calculate the next position of the hitbox based on the current direction
        // without actually moving the entity.
        int offsetX = 0, offsetY = 0;
        int checkDistance = 4; // You can adjust this value based on how close you want to check against the wall.

        switch (direction) {
            case UP:
                offsetY = -checkDistance;
                break;
            case DOWN:
                offsetY = checkDistance;
                break;
            case LEFT:
                offsetX = -checkDistance;
                break;
            case RIGHT:
                offsetX = checkDistance;
                break;
        }

        // Create a proposed hitbox that represents where the entity's hitbox would be if it moved in the current direction.
        Rectangle proposedHitbox = new Rectangle(x + offsetX, y + offsetY, hitbox.width, hitbox.height);

        // Check for collision with the scene boundaries or walls.
        for (Rectangle boundary : Screen.getCurrentScene().boundaries) {
            if (proposedHitbox.intersects(boundary)) {
                return true; // The entity is directly facing a wall or boundary.
            }
        }
        return false; // No wall directly in the facing direction.
    }
    public boolean isFacingWall() {
        return isFacingWall(getCurrentDirection());
    }
    public void setPosition(int x, int y) {
        this.x = x;
        this.y = y;

    }

    public String getName() {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    // Get the current x-coordinate of the entity
    public int getCurrentX() {
        return x;
    }

    // Get the current y-coordinate of the entity
    public int getCurrentY() {
        return y;
    }

    // Set the move speed of the entity
    public void setMoveSpeed(double moveSpeed)
    {
        this.moveSpeed = moveSpeed;
    }

    public double distanceTo(Entity otherEntity) {
        double dx = otherEntity.getCurrentX() - this.getCurrentX();
        double dy = otherEntity.getCurrentY() - this.getCurrentY();
        return Math.sqrt(dx * dx + dy * dy);
    }


    public void die()
    {

    }

    public boolean takeDamage(double amount){
        this.hp -= amount;
        if (this.hp < 0.0) {
            this.hp = 0;
            die();
            return true;
        }
        return false;
    }

    public void dispose()
    {
        logger.Log("Disposing entity");
        shotProjectiles.clear();
    }
}
