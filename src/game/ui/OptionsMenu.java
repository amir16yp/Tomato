package game.ui;

import game.Game;
import game.Screen;

import java.util.List;

public class OptionsMenu extends Menu {

    public static boolean SHOW_FPS = false;
    public static boolean SHOW_STATS = true;

    // Define the list of 4:3 resolutions
    private final int[][] resolutions = {
            {640, 480},
            {800, 600},
            {1024, 768},
            {1152, 864},
            {1280, 960}
            // Add more resolutions as needed
    };

    private int currentResolutionIndex = 0;

    public OptionsMenu(int x, int y, int width, int height) {
        super(x, y, width, height);
    }

    @Override
    public void initializeButtons() {
        super.initializeButtons();

        int startX = startX();
        int startY = getY() + 50;

        List<Button> buttons = getButtons();

        Button toggleFps = new Button(startX, startY, buttonWidth, buttonHeight, "Toggle FPS " + SHOW_FPS);
        toggleFps.setOnSelectedAction(() -> {
            SHOW_FPS = !SHOW_FPS;
            toggleFps.setText("Toggle FPS " + SHOW_FPS);
        });

        Button toggleStats = new Button(startX, startY + spacing, buttonWidth, buttonHeight, "Toggle Stats " + SHOW_STATS);
        toggleStats.setOnSelectedAction(() -> {
            SHOW_STATS = !SHOW_STATS;
            toggleStats.setText("Toggle Stats " + SHOW_STATS);
        });

        Button backButton = new Button(startX, startY + spacing * 2, buttonWidth, buttonHeight, "Back");
        backButton.setOnSelectedAction(() -> {
            Screen.setCurrentMenu(Screen.menus[0]);
        });

        Button switchResolutionButton = new Button(startX, startY + spacing * 3, buttonWidth, buttonHeight, "Switch Resolution");
        switchResolutionButton.setOnSelectedAction(() -> {
            switchResolution();
            switchResolutionButton.setText(Game.WIDTH + "x" + Game.HEIGHT);
        });

        buttons.add(toggleFps);
        buttons.add(toggleStats);
        buttons.add(backButton);
        buttons.add(switchResolutionButton);

        buttons.get(selectedButtonIndex).setSelected(true); // Highlight the first button
    }

    private void switchResolution() {
        currentResolutionIndex = (currentResolutionIndex + 1) % resolutions.length;
        int[] resolution = resolutions[currentResolutionIndex];
        Game.instance.setResolution(resolution[0], resolution[1]);
    }
}
