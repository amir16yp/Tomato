package game.entities.player;

import game.Game;
import game.Logger;
import game.items.Item;
import game.ui.Hotbar;

import java.util.ArrayList;
import java.util.List;

public class PlayerInventory
{
    public static final Hotbar hotbarUI = new Hotbar(true);
    public Item[] items;
    private Item currentItem;
    private final Logger logger = new Logger(this.getClass().getName());
    private static List<Item> itemHistory = new ArrayList<>();
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
                itemHistory.add(item);
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

    public void clearSlot(int index)
    {
        items[index] = null;
        hotbarUI.clearSlot(index);
    }

    public void clearSlots()
    {
        for (int i = 0; items.length > i; i++)
        {
            clearSlot(i);
        }
    }

    public void removeItem(Item item)
    {
        int slot = 0;
        for (Item slotItem : items)
        {
            if (slotItem != null)
            {
                if (slotItem == item)
                {
                    clearSlot(slot);
                }
            }

            slot++;
        }
    }

    public void useItem()
    {
        int slot = 0;
        for (Item item : items)
        {
            if (item != null && item == getCurrentItem() && System.currentTimeMillis() - item.lastUsedTime >= item.cooldownTime) {
                if (item.getUses() < item.getMaxUsages()) {
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

    public void addItem(Item newItem) {
        for (int i = 0; i < items.length; i++) {
            Item existingItem = items[i];
            if (existingItem != null && existingItem.getClass() == newItem.getClass() && existingItem.getUses() < existingItem.getMaxUsages()) {
                // Merge the uses and maxUsages of newItem into the existing item
                existingItem.setMaxUsages(existingItem.getMaxUsages() + newItem.getMaxUsages());
                int remainingCapacity = existingItem.getMaxUsages() - existingItem.getUses();
                int transferAmount = Math.min(remainingCapacity, newItem.getUses());

                existingItem.setUses(existingItem.getUses() + transferAmount);
                newItem.setUses(newItem.getUses() - transferAmount);

                hotbarUI.setItemSprite(i, existingItem.getSprite(), existingItem.getMaxUsages() - existingItem.getUses()); 
                // If newItem uses have been transferred completely, exit the method
                if (newItem.getUses() == 0) {
                    return;
                }
            }
        }

        // If stacking wasn't possible or newItem still has remaining uses, add it to an empty slot
        for (int i = 0; i < items.length; i++) {
            if (items[i] == null) {
                items[i] = newItem;
                hotbarUI.setItemSprite(i, newItem.getSprite(), newItem.getMaxUsages() - newItem.getUses());
                logger.Log("Added " + newItem.getClass().getName() + " to inventory at slot " + i);
                return;
            }
        }

        // If the inventory is full and newItem couldn't be stacked completely, log an error
        if (newItem.getUses() > 0) {
            logger.Log("Inventory is full. Cannot add item: " + newItem.getClass().getName() + " with " + newItem.getUses() + " remaining uses.");
        }
    }


    public Hotbar getHotbarUI() {
        return hotbarUI;
    }

    public static void clearItemHistory()
    {
        itemHistory.clear();
    }

    public void resetUses()
    {
        for (Item item : this.items)
        {
            if (item != null)
            {
                item.setUses(0);
            }
        }
    }

    public static List<Item> getItemHistory() {
        return itemHistory;
    }
}
