package game;

import game.entities.player.PlayerEntity;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class Lighting {
    private int playerRadius = 32 * 5;
    private Color playerSourceColor = new Color(255, 255, 200, 128);
    private boolean drawPlayerSource = false;
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

    public void addLightSource(int x, int y, int radius, Color color) {
        lightSources.add(new LightSource(x, y, radius, color));
    }

    public void addLightSource(int x, int y, int radius) {
        lightSources.add(new LightSource(x, y, radius, playerSourceColor));
    }

    public void setPlayerSource(int radius, Color color, boolean draw)
    {
        this.playerRadius = radius;
        this.playerSourceColor = color;
        this.drawPlayerSource = draw;
    }

    public void setPlayerSource(int radius, boolean draw)
    {
        this.playerRadius = radius;
        ///this.playerSourceColor = color;
        this.drawPlayerSource = draw;
    }

    public void removeLightSource(LightSource lightSource) {
        lightSources.remove(lightSource);
    }

    public void clearLightSources() {
        lightSources.clear();
    }

    public void drawLighting(Graphics2D g2d, PlayerEntity player, BufferedImage sceneImage) {
        BufferedImage lightMap = new BufferedImage(Game.INTERNAL_WIDTH, Game.INTERNAL_HEIGHT, BufferedImage.TYPE_INT_ARGB);
        Graphics2D lg2d = lightMap.createGraphics();

        lg2d.setComposite(AlphaComposite.Clear);
        lg2d.fillRect(0, 0, Game.INTERNAL_WIDTH, Game.INTERNAL_HEIGHT);
        lg2d.setComposite(AlphaComposite.SrcOver);

        // Draw all light sources
        for (LightSource lightSource : lightSources) {
            drawLightSource(lg2d, lightSource);
        }

        // Draw player light source
        if (drawPlayerSource) {
            drawLightSource(lg2d, new LightSource(player.getCurrentX() + player.getCurrentSprite().getWidth() / 2, player.getCurrentY() + player.getCurrentSprite().getHeight() / 2, playerRadius, playerSourceColor));
        }
        // Apply the light map to the scene
        g2d.drawImage(lightMap, 0, 0, null);

        // Apply darkness overlay
        Color darknessColor = new Color(0, 0, 0, (int) (darkness * 255));
        g2d.setColor(darknessColor);
        g2d.fillRect(0, 0, Game.INTERNAL_WIDTH, Game.INTERNAL_HEIGHT);

        lg2d.dispose();
    }

    private void drawLightSource(Graphics2D g2d, LightSource lightSource) {
        RadialGradientPaint rgp = new RadialGradientPaint(
                new Point(lightSource.x, lightSource.y),
                lightSource.radius,
                new float[]{0.0f, 1.0f},
                new Color[]{lightSource.color, new Color(0, 0, 0, 0)}
        );

        g2d.setPaint(rgp);
        g2d.fillOval(lightSource.x - lightSource.radius, lightSource.y - lightSource.radius, lightSource.radius * 2, lightSource.radius * 2);
    }

    public void drawLightSources(Graphics2D graphics2D)
    {
        for (LightSource lightSource : this.lightSources)
        {
            drawLightSource(graphics2D, lightSource);
        }
    }

    public static class LightSource {
        int x;
        int y;
        int radius;
        Color color;

        public LightSource(int x, int y, int radius, Color color) {
            this.x = x;
            this.y = y;
            this.radius = radius;
            this.color = color;
        }
    }
}
