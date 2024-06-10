package game.ui;

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
    private Logger logger = new Logger(this.getClass().getName());

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

        for (int i = 0; i < NUM_SLOTS; i++) {
            int slotX = getX() + i * (SLOT_SIZE + SLOT_PADDING);
            int slotY = getY();

            // Calculate the center of the slot
            int centerX = slotX + SLOT_SIZE / 2;
            int centerY = slotY + SLOT_SIZE / 2;

            // Draw slot background
            g.setColor(new Color(128, 128, 128,128));
            g.fillRect(slotX, slotY, SLOT_SIZE, SLOT_SIZE);

            // Draw item sprite
            if (!(i > itemSprites.length))
            {
                if (itemSprites[i] != null) {
                    int spriteX = centerX - itemSprites[i].getWidth() / 2;
                    int spriteY = centerY - itemSprites[i].getHeight() / 2;
                    itemSprites[i].draw(g, spriteX, spriteY);
                }
            }


            // Draw item count
            g.setColor(Color.WHITE);
            g.drawString(String.valueOf(itemCounts[i]), slotX + SLOT_SIZE - 15, slotY + SLOT_SIZE - 10);

            // Highlight selected slot
            if (i == selectedItemIndex) {
                g.setColor(Color.YELLOW);
                g.drawRect(slotX, slotY, SLOT_SIZE, SLOT_SIZE);
            }
        }
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
