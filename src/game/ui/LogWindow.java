package game.ui;


import javax.swing.*;
import java.awt.*;

public class LogWindow extends JFrame {
    private static JTextArea logArea = new JTextArea();

    public LogWindow() {
        // Set up the frame
        setTitle("Log Window");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);

        // Create the text area for logs
        logArea.setEditable(false); // Make text area non-editable
        logArea.setFont(new Font("Monospaced", Font.PLAIN, 12)); // Set a monospaced font

        // Add a scroll pane containing the text area to the frame
        JScrollPane scrollPane = new JScrollPane(logArea);

        // Adding the scrollPane to the CENTER of BorderLayout-managed content pane
        // This ensures that scrollPane will adjust to the frame's size automatically
        this.getContentPane().add(scrollPane, BorderLayout.CENTER);
    }

    // Method to append log messages to the text area
    public static void appendLog(String message) {
        SwingUtilities.invokeLater(() -> {
            logArea.append(message + "\n");
            logArea.setCaretPosition(logArea.getDocument().getLength()); // Auto-scroll to the bottom
        });
    }
}