package game.entities.other;

import java.awt.*;

public class HealthBar {
    private final int maxHealth;
    private final int width;
    private final int height;
    private final Color backgroundColor = Color.GRAY;
    private int currentHealth;
    private Color healthColor;

    public HealthBar(int maxHealth, int width, int height) {
        this.maxHealth = maxHealth;
        this.currentHealth = maxHealth; // Start with full health
        this.width = width;
        this.height = height;
        this.healthColor = Color.GREEN;
    }

    public void setCurrentHealth(int health) {
        this.currentHealth = Math.max(0, Math.min(health, this.maxHealth)); // Ensure health is within bounds
        updateHealthColor();
    }

    public void decreaseHealth(int amount) {
        setCurrentHealth(this.currentHealth - amount);
    }

    public void increaseHealth(int amount) {
        setCurrentHealth(this.currentHealth + amount);
    }

    private void updateHealthColor() {
        float healthPercentage = (float) currentHealth / maxHealth;
        if (healthPercentage > 0.5) {
            healthColor = Color.GREEN;
        } else if (healthPercentage > 0.25) {
            healthColor = Color.YELLOW;
        } else {
            healthColor = Color.RED;
        }
    }

    public void draw(Graphics g, int x, int y) {
        // Draw the background
        g.setColor(backgroundColor);
        g.fillRect(x, y, width, height);

        // Calculate the width of the current health
        int healthWidth = (int) (((double) currentHealth / maxHealth) * width);

        // Draw the current health
        g.setColor(healthColor);
        g.fillRect(x, y, healthWidth, height);
    }
}
