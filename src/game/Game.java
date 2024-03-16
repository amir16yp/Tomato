package game;

import javax.swing.*;

public class Game extends JFrame {
    public static Screen screen = new Screen();
    public static Game instance;

    public Game() {
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(800, 600);
        setTitle("Tomato");
        setResizable(false);
        add(screen);
        setLocationRelativeTo(null);
        setVisible(true);

    }

    public static void main(String[] args) {
        instance = new Game();
    }
}