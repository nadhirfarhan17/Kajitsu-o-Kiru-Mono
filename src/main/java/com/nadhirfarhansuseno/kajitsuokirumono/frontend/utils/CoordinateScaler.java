
package com.nadhirfarhansuseno.kajitsuokirumono.frontend.utils;

import com.nadhirfarhansuseno.kajitsuokirumono.shared.models.GameConfig;

import java.awt.Graphics2D;

public class CoordinateScaler {

    private double scale;
    private int offsetX;
    private int offsetY;

    public CoordinateScaler() {
        scale = 1.0;
        offsetX = 0;
        offsetY = 0;
    }

    public void update(int screenWidth, int screenHeight) {
        double scaleX = (double) screenWidth /
                GameConfig.VIRTUAL_WIDTH;

        double scaleY = (double) screenHeight /
                GameConfig.VIRTUAL_HEIGHT;

        scale = Math.min(scaleX, scaleY);

        int scaledWidth = (int) (
                GameConfig.VIRTUAL_WIDTH * scale
        );

        int scaledHeight = (int) (
                GameConfig.VIRTUAL_HEIGHT * scale
        );

        offsetX = (screenWidth - scaledWidth) / 2;
        offsetY = (screenHeight - scaledHeight) / 2;
    }

    public void apply(Graphics2D g2d) {
        g2d.translate(offsetX, offsetY);
        g2d.scale(scale, scale);
    }

    public int toVirtualX(int screenX) {
        return (int) ((screenX - offsetX) / scale);
    }

    public int toVirtualY(int screenY) {
        return (int) ((screenY - offsetY) / scale);
    }

    public double getScale() {
        return scale;
    }

    public int getOffsetX() {
        return offsetX;
    }

    public int getOffsetY() {
        return offsetY;
    }
}
