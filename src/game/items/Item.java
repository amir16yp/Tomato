package game.items;

import game.ResourceLoader;
import game.Sprite;

public class Item
{
    private Sprite sprite;
    private int maxUsages;
    private int uses = 0;
    public long lastUsedTime = 0; // Track the time when the item was last used
    public long cooldownTime; // Cooldown duration in milliseconds
    public Item(Sprite sprite, int maxUsages, long cooldownTime)
    {
        this.sprite = sprite;
        this.maxUsages = maxUsages;
        this.cooldownTime = cooldownTime;
    }

    public void use()
    {
        uses++;
        lastUsedTime = System.currentTimeMillis();

        if (uses == maxUsages)
        {
            onRunOut();
        }
    }

    private boolean hasRunOutPerformed = false;

    public void onRunOut()
    {
        if (!hasRunOutPerformed) {
            // Perform action when item runs out of uses
            // Example: System.out.println("Item " + this.getClass().getName() + " has run out of uses.");

            // Set the flag to true so that this action is performed only once
            hasRunOutPerformed = true;
        }
    }

    public void update()
    {
        sprite.update();
    }

    public Sprite getSprite() {
        return sprite;
    }

    public int getUses() {
        return uses;
    }

    public void setUses(int uses) {
        this.uses = uses;
    }

    public int getMaxUsages() {
        return maxUsages;
    }
}
