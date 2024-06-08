package game.ui;

import java.awt.*;

public class Text extends UIElement{
    private String text;
    private Color color;

    public Text(int x, int y, int width, int height, boolean visible, String text, Color color) {
        super(x, y, width, height, visible);
        this.text = text;
        this.color = color;
    }

    public Text(int x, int y, int width, int height, boolean visible, String text) {
        super(x, y, width, height, visible);
        this.text = text;
        this.color = Color.YELLOW;
    }

    public void setColor(Color color)
    {
        this.color = color;
    }

    public Color getColor()
    {
        return this.color;
    }

    public String getText() {
        return text;
    }

    public void setText(String text)
    {
        this.text = text;
    }

    @Override
    public void draw(Graphics g)
    {
        super.draw(g);
        g.setColor(this.color);
        g.drawString(getText(), getX(), getY());
    }
}
