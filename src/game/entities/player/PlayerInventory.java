package game.entities.player;

import game.ui.Hotbar;

public class PlayerInventory
{
    private final Hotbar hotbarUI = new Hotbar((800 - (10 * 50 + 9 * 5)) / 2, 0, 800, 50, true);
    private Item[] items;
    private Item currentItem;
    public PlayerInventory(Item[] items)
    {
        this.items = items;
        int slot = 0;
        for (Item item : items)
        {
            hotbarUI.setItemSprite(slot, item.getSprite(), item.getMaxUsages() - item.getUses());
            slot++;
        }
        currentItem = items[0];
    }

    public void selectItem(int index)
    {
        hotbarUI.selectItem(index);
        if (items.length > index)
        {
            currentItem = items[index];
        }

    }

    public void useItem()
    {
        int slot = 0;
        for (Item item : items)
        {
            if (item.getUses() < item.getMaxUsages() && item == getCurrentItem() && System.currentTimeMillis() - item.lastUsedTime >= item.cooldownTime) { // Check if there are any uses left
                item.use();
                hotbarUI.setItemSprite(slot, item.getSprite(), item.getMaxUsages() - item.getUses());
            }
            slot++;
        }
    }

    public Item getCurrentItem() {
        return currentItem;
    }

    public Hotbar getHotbarUI() {
        return hotbarUI;
    }
}
