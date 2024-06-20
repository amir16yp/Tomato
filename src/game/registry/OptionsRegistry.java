package game.registry;

import game.Logger;
import game.Utils;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class OptionsRegistry {

    private transient Logger logger = new Logger(this.getClass().getName()); // Initialize logger instance

    private Map<String, String> options;

    private File optionsFile;
    public static OptionsRegistry registry = new OptionsRegistry();

    public OptionsRegistry() {
        this.options = new HashMap<>();
        this.optionsFile = new File(Utils.getCurrentDir(), "options.dat");

        logger.Log("Initializing options");
        loadOptionsFromFile(); // Load options from file on initialization
        saveOptionsIfNeeded(); // Save default options if file doesn't exist
    }

    // Getter for options map
    public Map<String, String> getOptions() {
        return options;
    }

    // Method to set an option
    public void setOption(String key, Object value) {
        logger.Log("Set key " + key + " to value " + value.toString());
        options.put(key, value.toString());
        saveOptions(); // Save options after each change
    }

    // Method to get an option
    public String getOption(String key) {
        return options.get(key);
    }

    // Method to get a boolean option
    public boolean getBooleanOption(String key) {
        return Boolean.parseBoolean(getOption(key));
    }

    // Method to get an integer option
    public int getIntOption(String key) {
        return Integer.parseInt(getOption(key));
    }

    // Save options to file
    public void saveOptions() {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(optionsFile))) {
            out.writeObject(options); // Write the options map directly
            logger.Log("Options saved to " + optionsFile.getPath());
        } catch (IOException e) {
            logger.Error(e);
        }
    }

    // Load options from file
    private void loadOptionsFromFile() {
        if (optionsFile.exists()) {
            try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(optionsFile))) {
                options = (Map<String, String>) in.readObject(); // Read the options map directly
                logger.Log("Options loaded from " + optionsFile.getPath());
            } catch (IOException | ClassNotFoundException e) {
                logger.Error(e);
            }
        } else {
            // Initialize default options if file doesn't exist
            initDefaultOptions();
        }
    }

    // Save default options if file doesn't exist
    private void saveOptionsIfNeeded() {
        if (!optionsFile.exists()) {
            initDefaultOptions();
            saveOptions();
        }
    }

    // Initialize default options
    private void initDefaultOptions() {
        // Set default options using setOption method
        this.setOption("SHOW_FPS", false);
        this.setOption("SHOW_STATS", true);
        this.setOption("WIDTH", 800);
        this.setOption("HEIGHT", 600);
        logger.Log("Default options initialized");
    }
}
