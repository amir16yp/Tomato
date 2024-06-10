package game.ui;

import game.Game;
import game.Screen;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

public class    Button extends UIElement {
    private String text;
    private final Font font;
    private final Color textColor;
    private final Color backgroundColor;
    private final Color highlightColor;
    public boolean isSelected;

    public Button(int x, int y, int width, int height, String text) {
        super(x, y, width, height, true);
        this.text = text;
        this.font = new Font("Arial", Font.PLAIN, 20);
        this.textColor = Color.WHITE;
        this.backgroundColor = new Color(108, 117, 125); ;
        this.highlightColor = new Color(40, 167, 69);;
        this.isSelected = false;
    }

    private Runnable onSelectedAction;

    public void setOnSelectedAction(Runnable action) {
        this.onSelectedAction = action;
    }

    public void onOptionSelected() {
        if (onSelectedAction != null && this.isVisible() && Screen.isPaused) {
            onSelectedAction.run();
        }
    }

    public String getText()
    {
        return text;
    }
    public void setText(String text) {
        this.text = text;
    }
    public boolean containsPoint(int x, int y) {
        return x >= getX() && x <= getX() + getWidth() &&
                y >= getY() && y <= getY() + getHeight();
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    @Override
    public void draw(Graphics g) {

        // Draw background
        g.setColor(isSelected ? highlightColor : backgroundColor);
        g.fillRect(getX(), getY(), getWidth(), getHeight());

        // Draw text
        g.setFont(font);
        g.setColor(textColor);
        int stringWidth = g.getFontMetrics().stringWidth(text);
        int stringHeight = g.getFontMetrics().getHeight();
        // Center text horizontally and vertically within the button
        int x = getX() + (getWidth() - stringWidth) / 2;
        int y = getY() + (getHeight() - stringHeight) / 2 + g.getFontMetrics().getAscent();
        g.drawString(text, x, y);
    }
}
