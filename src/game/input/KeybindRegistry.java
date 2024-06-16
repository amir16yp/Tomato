package game.input;

import game.Logger;

import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class KeybindRegistry {

    private Map<Integer, List<Runnable>> keyActions = new HashMap<>();
    private Map<Integer, List<Runnable>> keyReleasedActions = new HashMap<>();
    private final Logger logger = new Logger(this.getClass().getName());
    public static KeybindRegistry registry = new KeybindRegistry();

    public void registerKeyPressedAction(int keyCode, Runnable action) {
        keyActions.computeIfAbsent(keyCode, k -> new ArrayList<>()).add(action);
        logger.Log("Registered key pressed " + keyCode);
    }

    public void registerKeyReleasedAction(int keyCode, Runnable action) {
        keyReleasedActions.computeIfAbsent(keyCode, k -> new ArrayList<>()).add(action);
        logger.Log("Registered key released " + KeyEvent.getKeyText(keyCode));
    }

    public void registerKeyPressedAction(KeyEvent keyEvent, Runnable action) {
        this.registerKeyPressedAction(keyEvent.getKeyCode(), action);
    }

    public void registerKeyReleasedAction(KeyEvent keyEvent, Runnable action) {
        this.registerKeyReleasedAction(keyEvent.getKeyCode(), action);
    }

    public void handleKeyPress(int keyCode) {
        List<Runnable> actions = keyActions.get(keyCode);
        if (actions != null) {
            for (Runnable action : actions) {
                if (action != null)
                {
                    action.run();
                }
            }
        }
    }

    public void handleKeyRelease(int keyCode) {
        List<Runnable> actions = keyReleasedActions.get(keyCode);
        if (actions != null) {
            for (Runnable action : actions) {
                if (action != null)
                {
                    action.run();
                }
            }
        }
    }

}
