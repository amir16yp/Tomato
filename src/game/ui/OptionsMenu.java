package game.ui;

import game.Game;
import game.Screen;
import game.registry.OptionsRegistry;

import java.util.List;

import static game.registry.OptionsRegistry.registry;

public class OptionsMenu extends Menu {

    private boolean SHOW_FPS;
    private boolean SHOW_STATS;

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
        // Load options from registry
        loadOptions();
    }

    // Method to load options from registry
    private void loadOptions() {
        SHOW_FPS = registry.getBooleanOption("SHOW_FPS");
        SHOW_STATS = registry.getBooleanOption("SHOW_STATS");
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
            registry.setOption("SHOW_FPS", SHOW_FPS); // Update registry
        });

        Button toggleStats = new Button(startX, startY + spacing, buttonWidth, buttonHeight, "Toggle Stats " + SHOW_STATS);
        toggleStats.setOnSelectedAction(() -> {
            SHOW_STATS = !SHOW_STATS;
            toggleStats.setText("Toggle Stats " + SHOW_STATS);
            registry.setOption("SHOW_STATS", SHOW_STATS); // Update registry
        });

        Button backButton = new Button(startX, startY + spacing * 2, buttonWidth, buttonHeight, "Back");
        backButton.setOnSelectedAction(() -> {
            Screen.setCurrentMenu(Screen.menus[0]);
        });

        Button switchResolutionButton = new Button(startX, startY + spacing * 3, buttonWidth, buttonHeight, "Switch Resolution");
        switchResolutionButton.setOnSelectedAction(() -> {
            switchResolution();
            switchResolutionButton.setText(Game.WIDTH + "x" + Game.HEIGHT);
            registry.setOption("WIDTH", Game.WIDTH);
            registry.setOption("HEIGHT", Game.HEIGHT);
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
