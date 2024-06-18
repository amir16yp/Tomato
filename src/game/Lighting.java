package game;

import game.entities.player.PlayerEntity;

import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class Lighting {
    private int playerRadius = 32 * 5;
    private Color playerSourceColor = new Color(255, 255, 200, 128);
    private boolean drawPlayerSource = false;
    private float playerStrength = 1.0f; // default player light strength
    private final List<LightSource> lightSources;
    private float darkness; // value between 0.0 (no darkness) and 1.0 (completely dark)

    public Lighting() {
        this.lightSources = new ArrayList<>();
        this.darkness = 0.0f; // default darkness level
    }

    public void setDarkness(float darkness) {
        this.darkness = Math.max(0.0f, Math.min(1.0f, darkness)); // ensure darkness is within valid range
    }

    public float getDarkness() {
        return darkness;
    }

    public void addLightSource(int x, int y, int radius, Color color, float strength) {
        lightSources.add(new LightSource(x, y, radius, color, strength));
    }

    public void addLightSource(int x, int y, int radius) {
        lightSources.add(new LightSource(x, y, radius, playerSourceColor, 1.0f));
    }

    public void setPlayerSource(int radius, Color color, boolean draw, float strength) {
        this.playerRadius = radius;
        this.playerSourceColor = color;
        this.drawPlayerSource = draw;
        this.playerStrength = strength;
    }

    public void setPlayerSource(int radius, boolean draw, float strength) {
        this.playerRadius = radius;
        this.drawPlayerSource = draw;
        this.playerStrength = strength;
    }

    public void removeLightSource(LightSource lightSource) {
        lightSources.remove(lightSource);
    }

    public void clearLightSources() {
        lightSources.clear();
    }

    public void drawLighting(Graphics2D g2d, BufferedImage sceneImage) {
        PlayerEntity player = PlayerEntity.getPlayer(); // Get the player instance
        drawLightSources(g2d, player); // Draw all light sources
    }

    public void drawDarkness(Graphics2D g2d, BufferedImage sceneImage) {
        g2d.setColor(new Color(0, 0, 0, (int) (darkness * 255)));
        g2d.fillRect(0, 0, sceneImage.getWidth(), sceneImage.getHeight());
    }

    private void drawLightSources(Graphics2D g2d, PlayerEntity player) {
        for (LightSource lightSource : this.lightSources) {
            drawLightSource(g2d, lightSource);
        }

        // Draw player light source
        if (drawPlayerSource && player != null) {
            drawLightSource(g2d, new LightSource(
                    player.getCurrentX() + player.getCurrentSprite().getWidth() / 2,
                    player.getCurrentY() + player.getCurrentSprite().getHeight() / 2,
                    playerRadius,
                    playerSourceColor,
                    playerStrength
            ));
        }
    }

    private void drawLightSource(Graphics2D g2d, LightSource lightSource) {
        float effectiveStrength = lightSource.strength * calculateEffectiveStrength(lightSource.strength);
        RadialGradientPaint rgp = new RadialGradientPaint(
                new Point(lightSource.x, lightSource.y),
                lightSource.radius,
                new float[]{0.0f, 1.0f},
                new Color[]{adjustColorStrength(lightSource.color, effectiveStrength), new Color(0, 0, 0, 0)}
        );

        g2d.setPaint(rgp);
        g2d.fill(new Ellipse2D.Float(lightSource.x - lightSource.radius, lightSource.y - lightSource.radius,
                lightSource.radius * 2, lightSource.radius * 2));
    }

    private Color adjustColorStrength(Color color, float strength) {
        int alpha = Math.toIntExact(Math.round(color.getAlpha() * strength));
        alpha = Math.min(255, Math.max(0, alpha));
        return new Color(color.getRed(), color.getGreen(), color.getBlue(), alpha);
    }

    private float calculateEffectiveStrength(float strength) {
        return strength / (1.0f - darkness);
    }

    public static class LightSource {
        int x;
        int y;
        int radius;
        Color color;
        float strength;

        public LightSource(int x, int y, int radius, Color color, float strength) {
            this.x = x;
            this.y = y;
            this.radius = radius;
            this.color = color;
            this.strength = strength;
        }
    }
}
