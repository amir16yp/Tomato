package game.ui;

import game.Logger;

import java.awt.*;

public class UIElement
{
    private final int x, y, width, height;
    private boolean visible;
    private static int totalElementCount = 0;
    public Logger logger = new Logger(this.getClass().getName());
    public UIElement(int x, int y, int width, int height, boolean visible) {
        logger.addPrefix("E" + totalElementCount);
        totalElementCount++;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.visible = visible;
        logger.Log("Created UIElement with x=" + x + ", y=" + y + ", width=" + width + ", height=" + height + ", visible=" + visible);
    }

    public String getType()
    {
        return "UIElement";
    }
    public int getX()
    {
        return x;
    }

    public int getY()
    {
        return y;
    }

    public int getWidth()
    {
        return width;
    }

    public int getHeight()
    {
        return height;
    }

    public void setVisible(boolean visible)
    {
        logger.Log("Setting visible to " + visible);
        this.visible = visible;
    }

    public boolean isVisible()
    {
        return this.visible;
    }

    public void update()
    {

    }

    public void draw(Graphics g)
    {

    }

}
