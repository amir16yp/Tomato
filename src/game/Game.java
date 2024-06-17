    package game;

    import javax.swing.*;
    import java.awt.*;
    import java.awt.event.WindowAdapter;
    import java.awt.event.WindowEvent;
    import java.awt.event.WindowFocusListener;

    public class Game extends JFrame {
        public static Screen screen = new Screen();
        public static Game instance = new Game();
        public static final int ORIGINAL_WIDTH = 800;
        public static final int ORIGINAL_HEIGHT = 600;
        public static int WIDTH = ORIGINAL_WIDTH;
        public static int HEIGHT = ORIGINAL_HEIGHT;
        public static int SCALE_FACTOR = 2;
        public static final ResourceLoader defaultResourceLoader = new DefaultResourceLoader();

        public void setResolution(int width, int height) {
            WIDTH = width;
            HEIGHT = height;

            // Calculate the scale factor based on the new resolution
            //SCALE_FACTOR = Math.min(WIDTH / ORIGINAL_WIDTH, HEIGHT / ORIGINAL_HEIGHT);

            setSize(WIDTH, HEIGHT);
            // Adjust the actual window size and screen panel size
            screen.setSize(WIDTH, HEIGHT);
            screen.setPreferredSize(new Dimension(WIDTH, HEIGHT));
            pack(); // Adjust frame size to preferred size of its components
            setLocationRelativeTo(null); // Center the window on screen
        }

        public Game() {
            setDefaultCloseOperation(DISPOSE_ON_CLOSE);
            //setResolution(800 * 2, 600 * 2);
            setSize(ORIGINAL_WIDTH, ORIGINAL_HEIGHT);
            setTitle("Tomato");
            setResizable(false);
            add(screen);
            setResolution(1280, 960); // Initial resolution setup
            setLocationRelativeTo(null);

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
                    // Handle window gained focus event
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
            // Load mods before making the game visible
            ModLoader.loadMods();
            instance.setVisible(true);
        }
    }
