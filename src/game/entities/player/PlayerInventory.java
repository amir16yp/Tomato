package game.entities.player;

import game.Game;
import game.Logger;
import game.ui.Hotbar;

public class PlayerInventory
{
    private static final Hotbar hotbarUI = new Hotbar((Game.WIDTH - (10 * 50 + 9 * 5)) / 2, 0, Game.WIDTH, 50, true);
    private Item[] items;
    private Item currentItem;
    private Logger logger = new Logger(this.getClass().getName());

    public PlayerInventory(Item[] items)
    {
        logger.Log("Init player inventory");
        if (items.length != 10)
        {
            logger.Error("Items list must have correct length");
        }
        this.items = items;
        StringBuilder logItemsString = new StringBuilder();
        logItemsString.append("init inventory with items:\n");
        int slot = 0;
        for (Item item : items)
        {
            logItemsString.append(slot);
            logItemsString.append(" >");
            if (item != null)
            {
                logItemsString.append(item.getClass().getName());
                hotbarUI.setItemSprite(slot, item.getSprite(), item.getMaxUsages() - item.getUses());
            } else {
                logItemsString.append("Empty");
            }
            logItemsString.append("\n");
            slot++;
        }
        logger.Log(logItemsString.toString());
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
            if (item != null)
            {
                if (item.getUses() < item.getMaxUsages() && item == getCurrentItem() && System.currentTimeMillis() - item.lastUsedTime >= item.cooldownTime) { // Check if there are any uses left
                    item.use();
                    hotbarUI.setItemSprite(slot, item.getSprite(), item.getMaxUsages() - item.getUses());
                }
            }
            slot++;
        }
    }

    public Item getCurrentItem() {
        return currentItem;
    }

    public void setItemAtIndex(Item item, int index)
    {
        try {
            this.items[index] = item;
        } catch (Exception e)
        {
            logger.Error(e);
        }
    }

    public Item getItemAtIndex(int index)
    {
        try {
            return items[index];
        } catch (Exception e)
        {
            logger.Error(e);
        }
        return null;
    }
    public Hotbar getHotbarUI() {
        return hotbarUI;
    }
}
