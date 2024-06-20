package game.entities;

import java.awt.Graphics;
import java.awt.Polygon;
import game.Sprite;
import game.Logger;

public class TileEntity {
    private final Sprite[][] spriteMatrix;
    private final int rows;
    private final int columns;
    private final Logger logger;
    private int x;
    private int y;
    public double distanceToAction = 8.0;
    public Runnable action;
    private Entity entityInteractingWith;
    private Class<? extends Entity> targetType; // Type of entity this TileEntity targets

    public TileEntity(Sprite[][] spriteMatrix, Class<? extends Entity> targetType) {
        this.spriteMatrix = spriteMatrix;
        this.rows = spriteMatrix.length;
        this.columns = spriteMatrix[0].length;
        this.logger = new Logger(this.getClass().getName());
        this.targetType = targetType;
    }

    public void update() {
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < columns; col++) {
                if (spriteMatrix[row][col] != null) {
                    spriteMatrix[row][col].update();
                }
            }
        }
    }

    public void draw(Graphics g) {
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < columns; col++) {
                if (spriteMatrix[row][col] != null) {
                    spriteMatrix[row][col].draw(g, x + col * spriteMatrix[row][col].getWidth(), y + row * spriteMatrix[row][col].getHeight());
                }
            }
        }
    }

    public void dispose() {
        logger.Log("Disposing TileEntity resources");
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < columns; col++) {
                if (spriteMatrix[row][col] != null) {
                    spriteMatrix[row][col].dispose();
                }
            }
        }
    }

    public Entity getEntityInteractingWith() {
        return entityInteractingWith;
    }

    public void setEntityInteractingWith(Entity entityInteractingWith) {
        this.entityInteractingWith = entityInteractingWith;
    }

    public Polygon getBoundary() {
        Polygon boundary = new Polygon();

        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < columns; col++) {
                if (spriteMatrix[row][col] != null) {
                    int spriteWidth = spriteMatrix[row][col].getWidth();
                    int spriteHeight = spriteMatrix[row][col].getHeight();
                    int leftX = x + col * spriteWidth;
                    int topY = y + row * spriteHeight;
                    int rightX = leftX + spriteWidth;
                    int bottomY = topY + spriteHeight;

                    // Add the four corners of each sprite to the polygon
                    boundary.addPoint(leftX, topY);
                    boundary.addPoint(rightX, topY);
                    boundary.addPoint(rightX, bottomY);
                    boundary.addPoint(leftX, bottomY);
                }
            }
        }

        return boundary;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void setTargetType(Class<? extends Entity> targetType) {
        this.targetType = targetType;
    }

    public Class<? extends Entity> getTargetType() {
        return targetType;
    }

}
