package game.entities;

import game.*;
import game.entities.other.HealthBar;
import game.entities.other.Projectile;
import game.items.PickupItem;

import java.awt.*;
import java.util.*;
import java.util.List;

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
    public int maxHP = 100;
    public Tile currerntTile;
    private Sprite currentSprite;
    private int x; // Current x-coordinate of the entity
    private int y; // Current y-coordinate of the entity
    public Direction currentDirection;
    private boolean drawHitbox = false;
    private long flashDuration;
    private long flashSwitchDelay;
    private Color flashColor;
    private boolean flashing;
    private boolean showSprite = true; // Flag to toggle between sprite and hitbox fill
    private Timer flashTimer; // Timer for controlling the flashing effect
    private long startFlashTime;


    public final Logger logger = new Logger(this.getClass().getName());
    public Entity(String name) {
        logger.addPrefix(name);
        this.name = name;
        sprites = new HashMap<>();
        healthBar = new HealthBar(maxHP, 20, 5);
        currentDirection = Direction.getRandomDirection();
    }

    public boolean isDrawHitbox() {
        return drawHitbox;
    }

    public void setDrawHitbox(boolean drawHitbox) {
        this.drawHitbox = drawHitbox;
    }

    public Sprite getCurrentSprite() {
        return currentSprite;
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

    private void applyGravity() {
        velocityY += 0.5; // Example gravity effect, adjust as needed
    }

    public Direction getCurrentDirection() {
        return currentDirection;
    }

    public void setCurrentDirection(Direction direction)
    {
        this.currentDirection = direction;
    }

    // Draw the entity at its current position

    // Draw the entity at its current position
    public void draw(Graphics g) {
        if (currentSprite != null) {
            if (flashing) {
                if (showSprite) {
                    currentSprite.draw(g, x, y);
                } else {
                    g.setColor(flashColor);
                    g.drawPolygon(currentSprite.getHitbox());
                }
            } else {
                currentSprite.draw(g, x, y);
                if (drawHitbox) {
                    Polygon hitbox = currentSprite.getHitbox();
                    g.setColor(Color.RED); // Set color for the polyline outline
                    // Draw the polyline connecting all points
                    g.drawPolygon(hitbox);
                }
            }
        }
        drawProjectiles(g);
        if (this.healthBarVisible) {
            healthBar.draw(g, x, y - 10);
        }
    }

    public void flash(long durationMs, long flashSwitchDelay, Color color) {
        if (flashing) {
            // Flashing is already active, cancel previous timer
            flashTimer.cancel();
        }

        this.flashDuration = durationMs;
        this.flashSwitchDelay = flashSwitchDelay;
        this.flashColor = color;
        this.flashing = true;
        this.showSprite = true;
        this.startFlashTime = System.currentTimeMillis();

        flashTimer = new Timer();
        flashTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                showSprite = !showSprite; // Toggle between sprite and hitbox fill
                if (System.currentTimeMillis() - startFlashTime >= durationMs) {
                    flashing = false;
                    showSprite = true; // Ensure sprite is shown after flashing ends
                    flashTimer.cancel(); // Cancel the flashing timer
                }
            }
        }, 0, flashSwitchDelay);
    }

    // Cancel the flashing effect
    public void stopFlash() {
        if (flashing) {
            flashTimer.cancel();
            flashing = false;
            showSprite = true; // Ensure sprite is shown after flashing ends
        }
    }

    public void update() {
        if (currentSprite != null) {
            currentSprite.update();
        }
        updatePosition();
        updateProjectiles();
        healthBar.setCurrentHealth((int) hp);

        // Apply gravity if not flat
        if (Game.FLAT) {
            applyGravity();
        }
        doTileEntityActions();
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
        if (currentDirection == null || !canShoot) return;

        // Create a new projectile
        Projectile newProjectile = new Projectile(x, y, currentDirection, speed, projectileSprite, damage, this);

        // Add the new projectile to the list of projectiles
        shotProjectiles.add(newProjectile);
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
        if (Game.FLAT) {
            // If flat is true, restrict movement to left and right directions
            if (direction == Direction.LEFT || direction == Direction.RIGHT) {
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
        } else {
            // Default behavior for non-flat movement
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
    }

    public void doTileEntityActions() {
        for (TileEntity tileEntity : Screen.getCurrentScene().tileEntitiesList) {
            if (tileEntity.action != null && distanceTo(tileEntity) <= tileEntity.distanceToAction) {
                // Check if the tileEntity's targetType matches the current entity's class
                if (tileEntity.getTargetType().isInstance(this)) {
                    tileEntity.setEntityInteractingWith(this);
                    tileEntity.action.run();
                }
            }
        }
    }


    public boolean checkCollision(int newX, int newY) {
        // Get the current sprite's hitbox
        Polygon currentHitbox = currentSprite.getHitbox();

        // Calculate the proposed hitbox based on the new position
        int width = currentHitbox.getBounds().width;
        int height = currentHitbox.getBounds().height;
        int[] xPoints = {newX, newX + width, newX + width, newX};
        int[] yPoints = {newY, newY, newY + height, newY + height};
        Polygon proposedHitbox = new Polygon(xPoints, yPoints, 4);

        // Check for collision with the scene boundaries or walls.
        for (Rectangle boundary : Screen.getCurrentScene().boundaries) {
            if (proposedHitbox.intersects(boundary)) {
                return true; // Collision detected with a boundary
            }
        }
        return false; // No collision with any boundaries
    }


    public int getCenterX() {
        return x + currentSprite.getWidth() / 2;
    }

    // Get the center y-coordinate of the entity
    public int getCenterY() {
        return y + currentSprite.getHeight() / 2;
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
        //int width = Screen.getCurrentScene().currentTiles.getWidth();
        //int height = Screen.getCurrentScene().currentTiles.getHeight();

        currerntTile = Screen.getCurrentScene().getTileInCoordinate(this.getCenterX(), this.getCenterY());
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
        Polygon hitbox = currentSprite.getHitbox(); //new Rectangle(x + offsetX, y + offsetY, hitbox.width, hitbox.height);

        // Check for collision with the scene boundaries or walls.
        for (Rectangle boundary : Screen.getCurrentScene().boundaries) {
            if (hitbox.intersects(boundary)) {
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

    public boolean isCollidingWith(Entity otherEntity) {
        Rectangle playerBounds = new Rectangle(getCurrentX(), getCurrentY(), this.currentSprite.getWidth(), this.currentSprite.getHeight());
        Rectangle otherBounds = new Rectangle(otherEntity.getCurrentX(), otherEntity.getCurrentY(), otherEntity.currentSprite.getWidth(), otherEntity.currentSprite.getHeight());
        return playerBounds.intersects(otherBounds);
    }

    public boolean isCollidingWith(PickupItem pickupItem) {
        Rectangle playerBounds = new Rectangle(getCurrentX(), getCurrentY(), this.currentSprite.getWidth(), this.currentSprite.getHeight());
        Rectangle otherBounds = new Rectangle(pickupItem.getX(), pickupItem.getY(), pickupItem.getSprite().getWidth(), pickupItem.getSprite().getHeight());
        return playerBounds.intersects(otherBounds);
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

    public double distanceTo(TileEntity tileEntity) {
        // Get the bounding polygon of the TileEntity
        Polygon tilePolygon = tileEntity.getBoundary();

        // Calculate the center of the TileEntity
        Rectangle tileBounds = tilePolygon.getBounds();
        int tileCenterX = tileBounds.x + tileBounds.width / 2;
        int tileCenterY = tileBounds.y + tileBounds.height / 2;

        // Calculate the distance to the center of the TileEntity
        double dx = tileCenterX - this.getCenterX();
        double dy = tileCenterY - this.getCenterY();
        return Math.sqrt(dx * dx + dy * dy);
    }



    public void die()
    {
        logger.Log("Entity has died");
    }

    public void takeDamage(double amount){
        this.hp -= amount;
        if (this.hp <= 0.0) {
            this.hp = 0;
            die();
        } else {
            this.flash(100, 30, Color.RED);
        }
    }

    public void heal(int amount)
    {
        this.hp += amount;
        if (this.hp > maxHP)
        {
            this.hp = maxHP;
        }
    }

    public void setMaxHP(int amount)
    {
        this.maxHP = amount;
        this.healthBar.setMaxHealth(amount);
    }

    public void dispose()
    {
        logger.Log("Disposing entity");
        shotProjectiles.clear();
    }
}
