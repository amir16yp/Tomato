package game.registry;

import game.Logger;

import java.awt.event.KeyEvent;
import java.util.*;

public class KeybindRegistry {

    private Map<Integer, List<Runnable>> keyActions = new HashMap<>();
    private Map<Integer, List<Runnable>> keyPressedActions = new HashMap<>();
    private Map<Integer, List<Runnable>> keyReleasedActions = new HashMap<>();
    private Map<Integer, Boolean> keyHeld = new HashMap<>(); // New map to track held keys
    private final Logger logger = new Logger(this.getClass().getName());
    public static KeybindRegistry registry = new KeybindRegistry(); // Singleton instance

    public void registerKeyPressedAction(int keyCode, Runnable action) {
        keyPressedActions.computeIfAbsent(keyCode, k -> new ArrayList<>()).add(action);
        logger.Log("Registered key pressed " + keyCode);
    }

    public void registerKeyReleasedAction(int keyCode, Runnable action) {
        keyReleasedActions.computeIfAbsent(keyCode, k -> new ArrayList<>()).add(action);
        logger.Log("Registered key released " + KeyEvent.getKeyText(keyCode));
    }

    public void registerKeyHeldAction(int keyCode, Runnable action) {
        // Add action to keyHeld map and existing key press action
        keyHeld.put(keyCode, false); // Initialize held state to false
        registerKeyPressedAction(keyCode, () -> {
            keyHeld.put(keyCode, true); // Set held to true on press
        });
        registerKeyReleasedAction(keyCode, () -> {
            keyHeld.put(keyCode, false);
        });
        keyActions.computeIfAbsent(keyCode, k -> new ArrayList<>()).add(action);
        logger.Log("Registered key held action " + keyCode);
    }

    public void handleKeyPress(KeyEvent keyEvent) {
        int keyCode = keyEvent.getKeyCode();
        handleKeyPress(keyCode);
    }

    public void handleKeyRelease(KeyEvent keyEvent) {
        int keyCode = keyEvent.getKeyCode();
        handleKeyRelease(keyCode);
    }

    private void handleKeyPress(int keyCode) {
        List<Runnable> actions = keyPressedActions.get(keyCode);
        if (actions != null) {
            for (Runnable action : actions) {
                if (action != null) {
                    action.run();
                }
            }
        }
    }

    public void handleKeyHeld(int keyCode)
    {
        if (!isKeyPressed(keyCode)) {
            return;
        }
        List<Runnable> actions = keyActions.get(keyCode);

        if (actions != null)
        {
            for (Runnable action : actions)
            {
                action.run();
            }
        }
    }

    private void handleKeyRelease(int keyCode) {
        List<Runnable> actions = keyReleasedActions.get(keyCode);
        if (actions != null) {
            for (Runnable action : actions) {
                if (action != null) {
                    action.run();
                }
            }
        }
        keyHeld.put(keyCode, false); // Reset held state on release
    }

    // Getter method to check if a key is currently held
    public boolean isKeyPressed(int keyCode) {
        return keyHeld.containsKey(keyCode) && keyHeld.get(keyCode);
    }

    public Set<Integer> getKeysHeld()
    {
        return keyHeld.keySet();
}
}
