
package com.nadhirfarhansuseno.kajitsuokirumono.frontend.effects;

import com.nadhirfarhansuseno.kajitsuokirumono.core.systems.PotionSystem;
import com.nadhirfarhansuseno.kajitsuokirumono.shared.enums.PotionType;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.Polygon;

public class PotionEffectRenderer {

    private double animationTime;

    public PotionEffectRenderer() {
        animationTime = 0.0;
    }

    public void update(double deltaTime) {
        if (!Double.isFinite(deltaTime) || deltaTime <= 0) {
            return;
        }

        animationTime += deltaTime;
    }

    public void render(
            Graphics2D g2d,
            int screenWidth,
            int screenHeight,
            PotionSystem potionSystem
    ) {
        if (g2d == null || potionSystem == null) {
            return;
        }

        Graphics2D graphics = (Graphics2D) g2d.create();

        try {
            graphics.setComposite(AlphaComposite.SrcOver);

            double pulse = 0.75
                    + 0.25 * Math.sin(animationTime * 4.0);

            if (potionSystem.isActive(PotionType.FREEZE)) {
                renderFreeze(
                        graphics,
                        screenWidth,
                        screenHeight,
                        pulse
                );
            }

            if (potionSystem.isActive(PotionType.FRENZY)) {
                renderFrenzy(
                        graphics,
                        screenWidth,
                        screenHeight,
                        pulse
                );
            }

            if (potionSystem.isActive(PotionType.DOUBLE_POINTS)) {
                renderDoublePoints(
                        graphics,
                        screenWidth,
                        screenHeight,
                        pulse
                );
            }

        } finally {
            graphics.dispose();
        }
    }

    private void renderFreeze(
            Graphics2D g2d,
            int width,
            int height,
            double pulse
    ) {
        Color iceBlue = new Color(70, 190, 255);

        drawEdgeGlow(
                g2d,
                width,
                height,
                iceBlue,
                65,
                pulse
        );

        drawFreezeCrystals(
                g2d,
                width,
                height,
                pulse
        );
    }

    private void renderFrenzy(
            Graphics2D g2d,
            int width,
            int height,
            double pulse
    ) {
        Color goldenYellow = new Color(255, 190, 45);

        drawEdgeGlow(
                g2d,
                width,
                height,
                goldenYellow,
                75,
                pulse
        );
    }

    private void renderDoublePoints(
            Graphics2D g2d,
            int width,
            int height,
            double pulse
    ) {
        Color brightGreen = new Color(60, 255, 110);

        drawEdgeGlow(
                g2d,
                width,
                height,
                brightGreen,
                75,
                pulse
        );
    }

    private void drawEdgeGlow(
            Graphics2D g2d,
            int width,
            int height,
            Color color,
            int thickness,
            double pulse
    ) {
        int alpha = (int) (65 * pulse);
        int strongAlpha = (int) (95 * pulse);

        Color strongColor = withAlpha(color, strongAlpha);
        Color transparentColor = withAlpha(color, 0);

        // Top edge
        g2d.setPaint(new GradientPaint(
                0,
                0,
                strongColor,
                0,
                thickness,
                transparentColor
        ));

        g2d.fillRect(
                0,
                0,
                width,
                thickness
        );

        // Bottom edge
        g2d.setPaint(new GradientPaint(
                0,
                height - thickness,
                transparentColor,
                0,
                height,
                strongColor
        ));

        g2d.fillRect(
                0,
                height - thickness,
                width,
                thickness
        );

        // Left edge
        g2d.setPaint(new GradientPaint(
                0,
                0,
                strongColor,
                thickness,
                0,
                transparentColor
        ));

        g2d.fillRect(
                0,
                0,
                thickness,
                height
        );

        // Right edge
        g2d.setPaint(new GradientPaint(
                width - thickness,
                0,
                transparentColor,
                width,
                0,
                strongColor
        ));

        g2d.fillRect(
                width - thickness,
                0,
                thickness,
                height
        );

        // Soft inner glow
        g2d.setColor(withAlpha(color, alpha));

        g2d.setStroke(new BasicStroke(2.0f));

        g2d.drawRect(
                3,
                3,
                width - 6,
                height - 6
        );
    }

    private void drawFreezeCrystals(
            Graphics2D g2d,
            int width,
            int height,
            double pulse
    ) {
        int alpha = (int) (125 * pulse);

        g2d.setColor(
                new Color(180, 235, 255, alpha)
        );

        g2d.setStroke(new BasicStroke(2.5f));

        int size = 55;

        // Top-left crystal
        drawCrystal(g2d, 0, 0, size);

        // Top-right crystal
        drawCrystal(g2d, width, 0, size);

        // Bottom-left crystal
        drawCrystal(g2d, 0, height, size);

        // Bottom-right crystal
        drawCrystal(g2d, width, height, size);
    }

    private void drawCrystal(
            Graphics2D g2d,
            int x,
            int y,
            int size
    ) {
        Polygon crystal = new Polygon();

        crystal.addPoint(x, y);
        crystal.addPoint(x + size, y + size / 3);
        crystal.addPoint(x + size / 2, y + size);
        crystal.addPoint(x + size / 4, y + size / 2);

        g2d.drawPolygon(crystal);

        g2d.drawLine(
                x,
                y,
                x + size / 2,
                y + size
        );

        g2d.drawLine(
                x + size,
                y + size / 3,
                x + size / 4,
                y + size / 2
        );
    }

    private Color withAlpha(Color color, int alpha) {
        return new Color(
                color.getRed(),
                color.getGreen(),
                color.getBlue(),
                Math.max(0, Math.min(255, alpha))
        );
    }
}
