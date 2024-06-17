package game.ui;

import game.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class SplashScreen extends UIElement {
    private BufferedImage image;
    private float opacity;
    private final Timer fadeInTimer;
    private final Timer fadeOutTimer;
    public boolean splashOver = false;
    private Logger logger = new Logger(this.getClass().getName());

    public SplashScreen(int width, int height, ResourceLoader resourceLoader, String imagePath, SplashType splashType) {
        super(0, 0, width, height, true);
        if (resourceLoader == null) {
            resourceLoader = new DefaultResourceLoader();
        }
        try {
            this.image = resourceLoader.loadImage(imagePath);
        } catch (IOException e) {
            logger.Error(e);
        }

        // Set initial opacity based on splash type
        switch (splashType) {
            case FADE_IN:
                this.opacity = 0.0f;
                startFadeIn();
                break;
            case FADE_OUT:
                this.opacity = 1.0f;
                startFadeOut();
                break;
            case FADE_IN_OUT:
                this.opacity = 0.0f;
                startFadeIn();
                break;
            case FADE_OUT_IN:
                this.opacity = 1.0f;
                startFadeOut();
                break;
        }
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

    private void startFadeIn() {
        fadeInTimer.start();
    }

    private void startFadeOut() {
        fadeOutTimer.start();
    }

    public boolean isSplashOver() {
        return splashOver;
    }

    public void setSplashOver(boolean splashOver) {
        this.splashOver = splashOver;
    }

    // Initialize timers
    {
        // Set up timer for fade in effect
        fadeInTimer = new Timer(Screen.TARGET_FRAME_TIME, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (opacity < 1.0f) {
                    opacity += 0.02f;
                    if (opacity > 1.0f) {
                        opacity = 1.0f;
                    }
                    Game.screen.repaint();
                } else {
                    fadeInTimer.stop();
                    logger.Log("Fade in complete!");
                    if (splashOver) {
                        Game.screen.showMainMenu();
                    } else {
                        startFadeOut();
                    }
                }
            }
        });

        // Set up timer for fade out effect
        fadeOutTimer = new Timer(30, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (opacity > 0.0f) {
                    opacity -= 0.02f;
                    if (opacity < 0.0f) {
                        opacity = 0.0f;
                    }
                    Game.screen.repaint();
                } else {
                    fadeOutTimer.stop();
                    logger.Log("Fade out complete!");
                    splashOver = true;
                    startFadeIn();
                }
            }
        });
    }
}
