package game.dialogue;

import java.util.ArrayList;
import java.util.List;

public class DialogueNode {
    private String message;
    private List<DialogueOption> options;
    private Runnable action; // New field to hold a Runnable action

    public DialogueNode(String message) {
        this.message = message;
        this.options = new ArrayList<>();
        this.action = null; // Initialize action as null
    }

    public DialogueNode(String message, Runnable action) {
        this.message = message;
        this.options = new ArrayList<>();
        this.action = action; // Initialize action with provided Runnable
    }

    public String getMessage() {
        return message;
    }

    public List<DialogueOption> getOptions() {
        return options;
    }

    public List<String> getOptionsStrings()
    {
        List<String> optionsStr = new ArrayList<>();
        for (DialogueOption option : options)
        {
            optionsStr.add(option.getText());
        }
        return optionsStr;
    }

    public void addOption(DialogueOption option) {
        options.add(option);
    }

    public Runnable getAction() {
        return action;
    }

    public void setAction(Runnable action) {
        this.action = action;
    }

    public void setOptions(List<DialogueOption> options) {
        this.options = options;
    }
}
