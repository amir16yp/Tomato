package game.ui;

import game.Game;
import game.Logger;
import game.Sprite;

import java.awt.*;

public class Hotbar extends UIElement {
    private static final int NUM_SLOTS = 10; // Number of slots in the hotbar
    private static final int SLOT_PADDING = 5; // Padding between slots
    private Sprite[] itemSprites; // Array to store item sprites
    private int[] itemCounts; // Array to store item counts
    private int selectedItemIndex; // Index of the currently selected item
    private final Logger logger = new Logger(Hotbar.class.getName());
    private static final int SLOT_SIZE = 40; // Height of each slot in pixels
    private int slotWidth;
    public Hotbar(boolean visible) {
        super(0, 0, 0, 0, visible); // Set initial width and height to 0
        itemSprites = new Sprite[NUM_SLOTS];
        itemCounts = new int[NUM_SLOTS];
        selectedItemIndex = 0;
    }

    public void setItemSprite(int slot, Sprite sprite, int itemCount) {
        if (isValidSlot(slot)) {
            itemSprites[slot] = sprite;
            itemCounts[slot] = itemCount;
        } else {
            logger.Error(new IllegalArgumentException("Invalid slot index"));
        }
    }

    public void selectItem(int index) {
        if (isValidSlot(index)) {
            selectedItemIndex = index;
        } else {
            logger.Error(new IllegalArgumentException("Invalid item index"));
        }
    }

    private boolean isValidSlot(int slot) {
        return slot >= 0 && slot < NUM_SLOTS;
    }

    public void updateDimensions(int screenWidth, int screenHeight)
    {

        //int newY = screenHeight - SLOT_SIZE - 2 * SLOT_PADDING; // Position at bottom of screen
        slotWidth = (screenWidth - (NUM_SLOTS - 1) * SLOT_PADDING) / NUM_SLOTS; // Calculate slot width based on screen width

        setX(0); // Set X to 0 to stretch across the screen
        setY(30);
        setWidth(screenWidth); // Set width to full screen width
        setHeight(SLOT_SIZE + 2 * SLOT_PADDING);
    }

    @Override
    public void draw(Graphics g) {
        if (!isVisible()) {
            return;
        }

        for (int i = 0; i < NUM_SLOTS; i++) {
            int slotX = i * (slotWidth + SLOT_PADDING);
            int slotY = getY();

            // Draw slot background
            g.setColor(new Color(128, 128, 128, 128));
            g.fillRect(slotX, slotY, slotWidth, SLOT_SIZE);

            // Draw item sprite
            Sprite sprite = itemSprites[i];
            if (sprite != null) {
                int spriteX = slotX + (slotWidth - sprite.getWidth()) / 2;
                int spriteY = slotY + SLOT_PADDING;
                sprite.draw(g, spriteX, spriteY);
            }

            // Draw item count
            g.setColor(Color.WHITE);
            g.setFont(new Font("Arial", Font.PLAIN, 12));
            g.drawString(String.valueOf(itemCounts[i]), slotX + slotWidth - 15, slotY + SLOT_SIZE - 10);

            // Highlight selected slot
            if (i == selectedItemIndex) {
                g.setColor(Color.YELLOW);
                g.drawRect(slotX, slotY, slotWidth, SLOT_SIZE);
            }
        }
    }

    public void clearSlot(int index) {
        if (isValidSlot(index)) {
            itemSprites[index] = null;
            itemCounts[index] = 0;
        }
    }

    @Override
    public void update() {
        updateDimensions(Game.INTERNAL_WIDTH, Game.INTERNAL_HEIGHT);
        for (Sprite sprite : itemSprites) {
            if (sprite != null) {
                sprite.update();
            }
        }
    }
}
