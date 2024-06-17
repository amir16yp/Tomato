package game.ui;

import game.DefaultResourceLoader;
import game.Logger;
import game.ResourceLoader;
import game.Game;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class SplashScreen extends UIElement {
    private BufferedImage image;
    private float opacity;
    private final Timer timer;
    public boolean splashOver = false;
    private Logger logger = new Logger(this.getClass().getName());

    public SplashScreen(int width, int height, ResourceLoader resourceLoader, String imagePath) {
        super(0, 0, width, height, true);
        if (resourceLoader == null) {
            resourceLoader = new DefaultResourceLoader();
        }
        try {
            this.image = resourceLoader.loadImage(imagePath);
        } catch (IOException e) {
            logger.Error(e);
        }
        this.opacity = 0.0f;

        // Set up a timer to handle the fade-in effect
        timer = new Timer(30, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (opacity < 1.0f) {
                    opacity += 0.02f;
                    if (opacity > 1.0f) {
                        opacity = 1.0f;
                    }
                    Game.screen.repaint();
                } else {
                    timer.stop();
                    logger.Log("timer over!");
                    splashOver = true;
                    Game.screen.showMainMenu();
                }
            }
        });
        timer.start();
    }

    @Override
    public void draw(Graphics g) {
        if (isVisible() && image != null) {
            Graphics2D g2d = (Graphics2D) g;
            Composite originalComposite = g2d.getComposite();
            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, opacity));
            int centerX = (getWidth() - image.getWidth()) / 2;
            int centerY = (getHeight() - image.getHeight()) / 2;
            g2d.drawImage(image, centerX, centerY, null);
            g2d.setComposite(originalComposite);
        }
    }

    public boolean isSplashOver() {
        return splashOver;
    }

    public void setSplashOver(boolean splashOver) {
        this.splashOver = splashOver;
    }
}
