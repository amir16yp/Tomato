package game.ui;

import java.util.List;

public class StartMenu extends Menu
{
    public StartMenu(int x, int y, int width, int height)
    {
        super(x, y, width, height);
    }

    public void initializeButtons() {
        super.initializeButtons();

        int startX = getX() + (getWidth() - buttonWidth) / 2;
        int startY = getY() + 50;

        List<Button> buttons = getButtons();

        buttons.add(new Button(startX, startY, buttonWidth, buttonHeight, "Resume"));
        buttons.add(new Button(startX, startY + spacing, buttonWidth, buttonHeight, "Options"));
        buttons.add(new Button(startX, startY + spacing * 2, buttonWidth, buttonHeight, "Exit"));
        buttons.add(new Button(startX, startY + spacing * 3, buttonWidth, buttonHeight, "View log"));

        buttons.get(selectedButtonIndex).setSelected(true); // Highlight the first button
    }
}