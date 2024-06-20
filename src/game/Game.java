    package game;

    import game.registry.OptionsRegistry;

    import javax.swing.*;
    import java.awt.*;
    import java.awt.event.WindowAdapter;
    import java.awt.event.WindowEvent;
    import java.awt.event.WindowFocusListener;

    public class    Game extends JFrame {
        public static Screen screen = new Screen();
        public static Game instance = new Game();
            public static final int INTERNAL_WIDTH = 768;
        public static final int INTERNAL_HEIGHT = 576;
        public static int WIDTH = INTERNAL_WIDTH;
        public static int HEIGHT = INTERNAL_HEIGHT;
        public static final ResourceLoader defaultResourceLoader = new DefaultResourceLoader();
        public static final boolean FLAT = false    ; // FLAT MODE IS VERY EARLY STAGE. TOP DOWN GAMES ARE BETTER ANYWAY
        public void setResolution(int width, int height) {
            WIDTH = width;
            HEIGHT = height;

            setSize(WIDTH, HEIGHT);
            this.setPreferredSize(new Dimension(WIDTH, HEIGHT));
            // Adjust the actual window size and screen panel size
            screen.setSize(WIDTH, HEIGHT);
            screen.setPreferredSize(new Dimension(WIDTH, HEIGHT));
            pack(); // Adjust frame size to preferred size of its components
            setLocationRelativeTo(null); // Center the window on screen
        }

        public Game() {
            setDefaultCloseOperation(DISPOSE_ON_CLOSE);
            setTitle("Tomato");
            setResizable(false);
            add(screen);
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
            int width = OptionsRegistry.registry.getIntOption("WIDTH");
            int height = OptionsRegistry.registry.getIntOption("HEIGHT");
            instance.setResolution(width, height);
            ModLoader.loadMods();
            instance.setVisible(true);
        }
    }
