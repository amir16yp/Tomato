package game;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class Game extends JFrame {
    public static Screen screen = new Screen();
    public static Game instance;

    public Game() {
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(800, 600);
        setTitle("Tomato");
        setResizable(false);
        add(screen);
        setLocationRelativeTo(null);
        setVisible(true);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                super.windowClosing(e);
                dispose();
            }
        });
    }

    @Override
    public void dispose() {
        super.dispose();
        screen.dispose();
        System.exit(0);
    }

    public static void main(String[] args) {
        instance = new Game();
    }
}
