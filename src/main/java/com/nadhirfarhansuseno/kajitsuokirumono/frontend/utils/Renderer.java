
package com.nadhirfarhansuseno.kajitsuokirumono.frontend.utils;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

public final class Renderer {

    private Renderer() {
    }

    public static void drawCenteredText(
            Graphics2D g2d,
            String text,
            int centerX,
            int y,
            Font font,
            Color color
    ) {
        Graphics2D graphics = (Graphics2D) g2d.create();

        graphics.setFont(font);
        graphics.setColor(color);

        FontMetrics metrics = graphics.getFontMetrics();

        int x = centerX - metrics.stringWidth(text) / 2;

        graphics.drawString(text, x, y);

        graphics.dispose();
    }

    public static void drawCircle(
            Graphics2D g2d,
            double x,
            double y,
            int radius,
            Color color
    ) {
        Graphics2D graphics = (Graphics2D) g2d.create();

        graphics.setColor(color);

        graphics.fillOval(
                (int) (x - radius),
                (int) (y - radius),
                radius * 2,
                radius * 2
        );

        graphics.dispose();
    }

    public static void drawRoundedRectangle(
            Graphics2D g2d,
            int x,
            int y,
            int width,
            int height,
            int arc,
            Color color
    ) {
        Graphics2D graphics = (Graphics2D) g2d.create();

        graphics.setRenderingHint(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON
        );

        graphics.setColor(color);

        graphics.fillRoundRect(
                x,
                y,
                width,
                height,
                arc,
                arc
        );

        graphics.dispose();
    }

    public static void drawProgressBar(
            Graphics2D g2d,
            int x,
            int y,
            int width,
            int height,
            double progress,
            Color backgroundColor,
            Color progressColor
    ) {
        Graphics2D graphics = (Graphics2D) g2d.create();

        double safeProgress = Math.max(
                0.0,
                Math.min(1.0, progress)
        );

        graphics.setColor(backgroundColor);
        graphics.fillRoundRect(x, y, width, height, 10, 10);

        graphics.setColor(progressColor);
        graphics.fillRoundRect(
                x,
                y,
                (int) (width * safeProgress),
                height,
                10,
                10
        );

        graphics.dispose();
    }
}
