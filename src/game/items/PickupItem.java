package game.items;

import game.Screen;
import game.Sprite;

import java.awt.*;

public class PickupItem extends Item
{
    private int x;
    private int y;

    public PickupItem(Sprite sprite, int maxUsages, long cooldownTime) {
        super(sprite, maxUsages, cooldownTime);
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void setX(int newX)
    {
        x = newX;
    }

    public void setY(int newY)
    {
        y = newY;
    }

    public void setCoordinate(int newX, int newY)
    {
        setX(newX);
        setY(newY);
    }

    public void draw(Graphics g)
    {
        getSprite().draw(g, x, y);
    }

    public void update()
    {
        getSprite().update();
    }
}
