
package com.nadhirfarhansuseno.kajitsuokirumono.frontend.components;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;

public class Button {

    private final Rectangle bounds;
    private final String text;
    private final Runnable action;

    private Color backgroundColor;
    private Color hoverColor;
    private Color textColor;

    private boolean hovered;
    private boolean enabled;

    public Button(
            int x,
            int y,
            int width,
            int height,
            String text,
            Runnable action
    ) {
        this.bounds = new Rectangle(x, y, width, height);
        this.text = text;
        this.action = action;

        this.backgroundColor = new Color(255, 190, 75);
        this.hoverColor = new Color(255, 210, 110);
        this.textColor = new Color(35, 35, 35);

        this.hovered = false;
        this.enabled = true;
    }

    public void render(Graphics2D g2d) {
        Graphics2D graphics = (Graphics2D) g2d.create();

        graphics.setRenderingHint(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON
        );

        // Draw button background
        if (!enabled) {
            graphics.setColor(Color.GRAY);
        } else if (hovered) {
            graphics.setColor(hoverColor);
        } else {
            graphics.setColor(backgroundColor);
        }

        graphics.fillRoundRect(
                bounds.x,
                bounds.y,
                bounds.width,
                bounds.height,
                20,
                20
        );

        // Draw button text
        graphics.setFont(new Font("SansSerif", Font.BOLD, 20));
        graphics.setColor(textColor);

        FontMetrics metrics = graphics.getFontMetrics();

        int textX = bounds.x +
                (bounds.width - metrics.stringWidth(text)) / 2;

        int textY = bounds.y +
                (bounds.height - metrics.getHeight()) / 2 +
                metrics.getAscent();

        graphics.drawString(text, textX, textY);

        graphics.dispose();
    }

    public void updateHover(int mouseX, int mouseY) {
        hovered = enabled && bounds.contains(mouseX, mouseY);
    }

    public void onClick(int mouseX, int mouseY) {
        if (enabled && bounds.contains(mouseX, mouseY) && action != null) {
            action.run();
        }
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;

        if (!enabled) {
            hovered = false;
        }
    }

    public boolean isHovered() {
        return hovered;
    }

    public Rectangle getBounds() {
        return new Rectangle(bounds);
    }
}
