package game.ui;

import game.Game;
import game.Logger;
import game.Sprite;

import java.awt.*;

public class Hotbar extends UIElement {
    private static final int NUM_SLOTS = 10; // Number of slots in the hotbar
    private static final int SLOT_SIZE = 50; // Size of each slot (width and height)
    private static final int SLOT_PADDING = 5; // Padding between slots
    private Sprite[] itemSprites; // Array to store item sprites
    private int[] itemCounts; // Array to store item counts
    private int selectedItemIndex; // Index of the currently selected item
    private final Logger logger = new Logger(this.getClass().getName());

    public Hotbar(int x, int y, int width, int height, boolean visible) {
        super(x, y, width, height, visible);
        itemSprites = new Sprite[NUM_SLOTS];
        itemCounts = new int[NUM_SLOTS];
        selectedItemIndex = 0; // Initialize with the first slot selected
    }

    public void setItemSprite(int slot, Sprite sprite, int itemCount) {
        if (slot >= 0 && slot < NUM_SLOTS) {
            itemSprites[slot] = sprite;
            itemCounts[slot] = itemCount;
        } else {
            logger.Error(new IllegalArgumentException("Invalid slot index"));
        }
    }

    public void selectItem(int index) {
        if (index >= 0 && index < NUM_SLOTS) {
            selectedItemIndex = index;
        } else {
            logger.Error(new IllegalArgumentException("Invalid item index"));
        }
    }

    @Override
    public void draw(Graphics g) {
        if (!isVisible()) {
            return;
        }

        // Calculate scaled dimensions based on current screen size
        double scaleX = (double) Game.WIDTH / Game.ORIGINAL_WIDTH;
        double scaleY = (double) Game.HEIGHT / Game.ORIGINAL_HEIGHT;

        for (int i = 0; i < NUM_SLOTS; i++) {
            int slotX = (int) (getX() + i * (SLOT_SIZE * scaleX + SLOT_PADDING * scaleX));
            int slotY = (int) (getY());

            // Calculate the center of the slot
            int centerX = (int) (slotX + SLOT_SIZE * scaleX / 2);
            int centerY = (int) (slotY + SLOT_SIZE * scaleY / 2);

            // Draw slot background
            g.setColor(new Color(128, 128, 128, 128));
            g.fillRect(slotX, slotY, (int) (SLOT_SIZE * scaleX), (int) (SLOT_SIZE * scaleY));

            // Draw item sprite
            if (itemSprites[i] != null) {
                int scaledSpriteWidth = (int) (itemSprites[i].getWidth() * scaleX);
                int scaledSpriteHeight = (int) (itemSprites[i].getHeight() * scaleY);
                int spriteX = centerX - scaledSpriteWidth / 2;
                int spriteY = centerY - scaledSpriteHeight / 2;
                itemSprites[i].draw(g, spriteX, spriteY, scaledSpriteWidth, scaledSpriteHeight);
            }

            // Draw item count
            g.setColor(Color.WHITE);
            g.setFont(new Font("Arial", Font.PLAIN, (int) (12 * Math.min(scaleX, scaleY)))); // Scale font size
            g.drawString(String.valueOf(itemCounts[i]), slotX + (int) (SLOT_SIZE * scaleX) - 15, slotY + (int) (SLOT_SIZE * scaleY) - 10);

            // Highlight selected slot
            if (i == selectedItemIndex) {
                g.setColor(Color.YELLOW);
                g.drawRect(slotX, slotY, (int) (SLOT_SIZE * scaleX), (int) (SLOT_SIZE * scaleY));
            }
        }
    }

    public void clearSlot(int index)
    {
        itemSprites[index] = null;
        itemCounts[index] = 0;
    }

    @Override
    public void update() {
        for (Sprite sprite : itemSprites) {
            if (sprite != null) {
                sprite.update();
            }
        }
    }
}
