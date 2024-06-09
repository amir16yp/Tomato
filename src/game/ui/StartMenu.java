package game.ui;

import game.Game;
import game.Logger;
import game.Screen;

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

        Button playButton = new Button(startX, startY, buttonWidth, buttonHeight, "Resume");
        Button optionsButton = new Button(startX, startY + spacing, buttonWidth, buttonHeight, "Options");
        Button exitButton = new Button(startX, startY + spacing * 2, buttonWidth, buttonHeight, "Exit");
        Button viewLogButton = new Button(startX, startY + spacing * 3, buttonWidth, buttonHeight, "View log");

        playButton.setOnSelectedAction(() -> {
            Screen.setPaused(false);
        });


        optionsButton.setOnSelectedAction(() -> {
            Screen.setCurrentMenu(1);
        });


        exitButton.setOnSelectedAction(() -> {
            Game.instance.dispose();
        });

        viewLogButton.setOnSelectedAction(() -> {
            Logger.logWindow.setVisible(true);
        });

        buttons.add(playButton);
        buttons.add(optionsButton);
        buttons.add(exitButton);
        buttons.add(viewLogButton);

        buttons.get(selectedButtonIndex).setSelected(true); // Highlight the first button
    }
}