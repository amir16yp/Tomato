package game.items;

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
