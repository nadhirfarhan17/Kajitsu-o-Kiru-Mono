
package com.nadhirfarhansuseno.kajitsuokirumono.frontend.components;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;

public class Notification {

    private String message;

    private Color color;

    private double duration;
    private double remainingTime;

    private boolean active;

    public Notification() {
        message = "";
        color = Color.WHITE;

        duration = 0;
        remainingTime = 0;

        active = false;
    }

    public void show(
            String message,
            Color color,
            double duration
    ) {
        this.message = message;
        this.color = color;

        this.duration = duration;
        this.remainingTime = duration;

        this.active = true;
    }

    public void update(double deltaTime) {
        if (!active) {
            return;
        }

        remainingTime -= deltaTime;

        if (remainingTime <= 0) {
            remainingTime = 0;
            active = false;
        }
    }

    public void render(Graphics2D g2d) {
        if (!active) {
            return;
        }

        Graphics2D graphics = (Graphics2D) g2d.create();

        float alpha = (float) Math.max(
                0,
                Math.min(1, remainingTime / duration)
        );

        graphics.setFont(
                new Font("SansSerif", Font.BOLD, 24)
        );

        graphics.setColor(
                new Color(
                        color.getRed(),
                        color.getGreen(),
                        color.getBlue(),
                        (int) (alpha * 255)
                )
        );

        int textWidth = graphics.getFontMetrics()
                .stringWidth(message);

        int x = (1280 - textWidth) / 2;

        graphics.drawString(message, x, 150);

        graphics.dispose();
    }

    public boolean isActive() {
        return active;
    }

    public void clear() {
        active = false;
        remainingTime = 0;
    }
}
