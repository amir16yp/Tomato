package game.registry;

import game.DefaultResourceLoader;
import game.Game;
import game.Logger;
import game.ResourceLoader;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

public class SoundRegistry {
    public static SoundRegistry registry = new SoundRegistry();
    private Map<String, Clip> soundMap;
    private Logger logger = new Logger(this.getClass().getName());

    // Private constructor to prevent instantiation
    private SoundRegistry() {
        soundMap = new HashMap<>();
        logger.Log("Initialized sound registry");
        registerSounds();
    }

    private void registerSounds() {
        logger.Log("registering sounds");
        registerSound("gun1", "/game/sounds/gun1.wav", Game.defaultResourceLoader);
    }

    public void registerSound(String keyword, String filePath, ResourceLoader resourceLoader) {
        try {
            if (resourceLoader == null) {
                resourceLoader = new DefaultResourceLoader();
            }
            Clip clip = resourceLoader.loadSound(filePath);
            soundMap.put(keyword, clip);
            logger.Log("Registered sound " + keyword + " from " + filePath);
        } catch (IOException e) {
            logger.Error(e);
        }
    }

    // Method to play a sound by keyword in a new thread
    public void playSound(String keyword) {
        Clip clip = soundMap.get(keyword);
        if (clip != null) {
            new Thread(() -> {
                clip.setFramePosition(0); // Rewind to the beginning
                clip.start();
            }).start();
        } else {
            logger.Error("Sound not found: " + keyword);
        }
    }
}
