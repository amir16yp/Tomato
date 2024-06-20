package game.ui;

import game.dialogue.DialogueNode;
import game.dialogue.DialogueOption;
import game.entities.player.PlayerEntity;
import game.registry.KeybindRegistry;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.util.List;

public class DialogueBox extends UIElement {
    private final Color boxColor;
    private Color textColor;
    private final Font font;
    private String message;
    private int currentLength;
    private long lastCharTime;
    private final long charDelay; // Milliseconds delay between characters
    private boolean stayVisibleAfterTyping; // Whether the message stays visible after typing
    private long visibilityDelay; // Delay before the message goes invisible after typing
    private long typingFinishedTime = 0; // Time when typing finished
    public List<DialogueOption> options;
    public int selectedOptionIndex = 0;
    public boolean showOptions = false;
    public static DialogueOption quitOption = new DialogueOption("Goodbye.", new DialogueNode("Goodbye!"));


    public DialogueBox(Color boxColor, Color textColor, Font font, long charDelay, boolean stayVisibleAfterTyping, long visibilityDelay) {
        super(20, 330, 760, 300, false); // Adjust the height to 300
        this.boxColor = boxColor;
        this.textColor = textColor;
        this.font = font;
        this.charDelay = charDelay;
        this.message = "";
        this.currentLength = 0;
        this.lastCharTime = System.currentTimeMillis();
        this.stayVisibleAfterTyping = stayVisibleAfterTyping;
        this.visibilityDelay = visibilityDelay;
    }

    public void setMessage(String message) {
        logger.Log("Set message: " + message);
        this.message = message;
        this.currentLength = 0;
        this.lastCharTime = System.currentTimeMillis();
        this.typingFinishedTime = 0; // Reset typing finished time
        this.showOptions = false;
        this.stayVisibleAfterTyping = false;
        setVisible(true); // Make sure the box is visible when setting a new message
    }

    public void setMessage(DialogueNode node) {
        this.message = node.getMessage();
        this.options = node.getOptions();
        logger.Log(String.format("set message \"%s\" with options:\n%s", message, String.join("\nOption: ", node.getOptionsStrings())));
        this.selectedOptionIndex = 0;
        this.currentLength = 0;
        this.lastCharTime = System.currentTimeMillis();
        this.showOptions = true;
        this.stayVisibleAfterTyping = true;
        if (!options.contains(quitOption))
        {
            options.add(quitOption);
        }
        setVisible(true);
    }

    public void update() {
        super.update();
        long currentTime = System.currentTimeMillis();

        if (currentLength < message.length()) {
            if (currentTime - lastCharTime >= charDelay) {
                currentLength++;
                lastCharTime = currentTime;
            }
        } else if (typingFinishedTime == 0) { // Typing just finished
            typingFinishedTime = currentTime; // Record the time typing finished
        }
        currentLength = Math.min(currentLength, message.length());

        // Handle visibility after typing is complete and delay is specified
        if (!stayVisibleAfterTyping && typingFinishedTime > 0 && currentTime - typingFinishedTime > visibilityDelay) {
            setVisible(false); // Make the dialogue box invisible
        }


    }

    @Override
    public void draw(Graphics g) {
        super.draw(g);
        // Draw the dialogue box
        g.setColor(boxColor);
        g.fillRect(getX(), getY(), getWidth(), getHeight());

        // Prepare to draw text
        g.setFont(font);
        g.setColor(textColor);

        // Split the message to draw up to currentLength, accounting for newline characters
        String textToDraw = message.substring(0, currentLength);
        String[] lines = textToDraw.split("\n", -1); // Split by newline, keeping trailing empty strings

        // Initial text positioning
        int startX = getX() + 10;
        int startY = getY() + 30; // Start a bit lower for visual appeal
        int lineHeight = g.getFontMetrics().getHeight(); // Get the line height to space lines evenly

        // Draw each line
        for (String line : lines) {
            g.drawString(line, startX, startY);
            startY += lineHeight; // Move to the next line's position
        }

        // Draw options if showOptions is true
        if (showOptions) {
            int optionX = startX;
            int optionY = startY + 10; // Spacing between message and options
            for (int i = 0; i < options.size(); i++) {
                DialogueOption option = options.get(i);
                if (i == selectedOptionIndex) {
                    g.setColor(Color.YELLOW); // Highlight selected option
                } else {
                    g.setColor(textColor);
                }
                g.drawString(option.getText(), optionX, optionY);
                optionY += lineHeight; // Spacing between options
            }
        }
    }


    public void setTextColor(Color textColor) {
        this.textColor = textColor;
    }

    public static void registerKeybinds()
    {
        KeybindRegistry.registry.registerKeyPressedAction(KeyEvent.VK_UP, () -> {
            DialogueBox dialogueBox = PlayerEntity.getActionDialogue();
            if (dialogueBox.showOptions) {
                dialogueBox.selectedOptionIndex = Math.max(0, dialogueBox.selectedOptionIndex - 1);
            }
        });
        KeybindRegistry.registry.registerKeyPressedAction(KeyEvent.VK_DOWN, () -> {
            DialogueBox dialogueBox = PlayerEntity.getActionDialogue();
            if (dialogueBox.showOptions)
            {
                dialogueBox.selectedOptionIndex = Math.min(dialogueBox.options.size() - 1,dialogueBox.selectedOptionIndex + 1);
                DialogueOption selectedOption = dialogueBox.options.get(dialogueBox.selectedOptionIndex);
                dialogueBox.setMessage(selectedOption.getNextNode());
                if (selectedOption == quitOption)
                {
                    dialogueBox.setVisible(false);
                }
            }
        });
    }
}
