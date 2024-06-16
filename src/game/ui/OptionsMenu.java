package game.ui;

import game.Screen;

import java.util.List;

public class OptionsMenu extends Menu{

    public static boolean SHOW_FPS = false;
    public static boolean SHOW_STATS = true;

    public OptionsMenu(int x, int y, int width, int height) {
        super(x, y, width, height);
    }

    public void initializeButtons() {
        super.initializeButtons();

        int startX = startX();
        int startY = getY() + 50;

        List<Button> buttons = getButtons();

        Button toggleFps = new Button(startX, startY, buttonWidth, buttonHeight, "Toggle FPS " + SHOW_FPS);
        toggleFps.setOnSelectedAction(() ->
        {
            SHOW_FPS = !SHOW_FPS;
            toggleFps.setText("Toggle FPS " + SHOW_FPS);
        });

        Button toggleStats = new Button(startX, startY + spacing, buttonWidth, buttonHeight, "Toggle Stats " + SHOW_STATS);
        toggleStats.setOnSelectedAction(() ->
        {
            SHOW_STATS = !SHOW_STATS;
            toggleStats.setText("Toggle Stats " + SHOW_STATS);
        });

        Button backButton = new Button(startX, startY + spacing * 2, buttonWidth, buttonHeight, "Back");
        backButton.setOnSelectedAction(() -> {
            Screen.setCurrentMenu(Screen.menus[0]);
        });

        buttons.add(toggleFps);
        buttons.add(toggleStats);
        buttons.add(backButton);


        buttons.get(selectedButtonIndex).setSelected(true); // Highlight the first button
    }
}
