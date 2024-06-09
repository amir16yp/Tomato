package game.ui;

import game.Utils;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class LogWindow extends JFrame {
    private static final int WIDTH = 800;
    private static final int HEIGHT = 600;
    private String currentContent = "";
    private JEditorPane logEditorPane;

    public LogWindow() {
        setTitle("Log Window");
        setSize(WIDTH, HEIGHT);
        setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        // Create the editor pane for logs
        logEditorPane = new JEditorPane();
        logEditorPane.setEditable(false); // Make editor pane non-editable
        logEditorPane.setContentType("text/html"); // Set content type to HTML
        logEditorPane.setBackground(Color.BLACK);
        // Add the editor pane to the frame
        getContentPane().add(new JScrollPane(logEditorPane), BorderLayout.CENTER);
    }

    // Method to append log messages to the editor pane
    public void appendLog(String originalMessage) {
        // Call ASCIIColorToHTML function to convert ASCII color codes to HTML
        String message = Utils.ConsoleColors.ASCIIColorToHTML(originalMessage);
        message = message.replace("\n", "<br>");
        // Append the message to the existing HTML content
        String newContent = currentContent + "<br>" + message;
        currentContent = newContent;
        logEditorPane.setText(newContent);

        // Scroll to the bottom
        //logEditorPane.setCaretPosition(logEditorPane.getDocument().getLength());
    }
}
