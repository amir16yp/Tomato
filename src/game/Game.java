package game;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;

public class Game extends JFrame {
    public static Screen screen = new Screen();
    public static Game instance;

    public static final int WIDTH = 800;
    public static final int HEIGHT = 600;

    public Game() {
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(WIDTH, HEIGHT);
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

        addWindowFocusListener(new WindowFocusListener() {
            @Override
            public void windowGainedFocus(WindowEvent e) {

            }

            @Override
            public void windowLostFocus(WindowEvent e) {
                Screen.setPaused(true);
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
