package game;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Objects;

public class Sprite {

    private final int animationInterval;
    public BufferedImage[] tiles;
    private int currentFrame;
    private long lastFrameChange;
    private boolean isFlipped; // Flag to indicate if the image is flipped
    private boolean isPaused; // Flag to indicate if the animation is paused
    private int x;
    private int y;
    private final int width;
    private final int height;

    public Sprite(String path, int tileWidth, int tileHeight, int animationInterval) {
        this.animationInterval = animationInterval;
        BufferedImage spriteSheet = null;
        try {
            spriteSheet = ImageIO.read(Objects.requireNonNull(this.getClass().getResource(path)));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // Calculate the number of frames based on image width and desired tile width
        int numFrames = spriteSheet.getWidth() / tileWidth;

        // Check if the image width is divisible by the tile width
        if (spriteSheet.getWidth() % tileWidth != 0) {
            throw new IllegalArgumentException("Image width must be divisible by tile width for even splitting.");
        }

        tiles = new BufferedImage[numFrames];

        // Extract individual frames
        for (int i = 0; i < numFrames; i++) {
            int x = i * tileWidth; // Starting x-coordinate for each frame
            tiles[i] = spriteSheet.getSubimage(x, 0, tileWidth, tileHeight); // Extract a frame
        }

        currentFrame = 0;
        lastFrameChange = System.currentTimeMillis();
        isFlipped = false; // Initialize as not flipped
        isPaused = false; // Initialize as not paused
        width = tileWidth;
        height = tileHeight;

    }

    public void draw(Graphics g, int x, int y) {
        this.x = x;
        this.y = y;
        if (isFlipped) {
            // Draw the image flipped
            BufferedImage flippedTile = flipTile(tiles[currentFrame]);
            g.drawImage(flippedTile, x, y, null);
        } else {
            // Draw the image normally
            g.drawImage(tiles[currentFrame], x, y, null);
        }
    }

    public void update() {
        if (!isPaused && System.currentTimeMillis() - lastFrameChange >= animationInterval) {
            currentFrame++;
            if (currentFrame >= tiles.length) {
                currentFrame = 0; // Reset animation
            }
            lastFrameChange = System.currentTimeMillis();
        }
    }

    public void pauseAnimation() {
        isPaused = true;
    }

    public void unPauseAnimation() {
        isPaused = false;
    }

    public void pauseAnimationByFrameIndex(int frameIndex) {
        if (frameIndex >= 0 && frameIndex < tiles.length) {
            currentFrame = frameIndex;
            pauseAnimation();
        } else {
            throw new IllegalArgumentException("Invalid frame index");
        }
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }


    public void unPauseAnimationByFrameIndex() {
        unPauseAnimation();
    }

    public BufferedImage getTile(int index) {
        if (index >= 0 && index < tiles.length) {
            return tiles[index];
        } else {
            return null;
        }
    }

    public BufferedImage[] getTiles() {
        return tiles;
    }

    public boolean isFlipped() {
        return isFlipped;
    }

    public void setFlipped(boolean flipped) {
        isFlipped = flipped;
    }

    private BufferedImage flipTile(BufferedImage tile) {
        BufferedImage flippedImage = new BufferedImage(tile.getWidth(), tile.getHeight(), BufferedImage.TYPE_INT_ARGB);
        Graphics g = flippedImage.getGraphics();
        g.drawImage(tile, tile.getWidth(), 0, -tile.getWidth(), tile.getHeight(), null);
        g.dispose();
        return flippedImage;
    }

    public void dispose()
    {

    }
}
