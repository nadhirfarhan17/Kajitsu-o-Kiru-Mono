
package com.nadhirfarhansuseno.kajitsuokirumono.frontend.effects;

import java.awt.Graphics2D;
import java.util.Random;

public class ScreenShake {

    private final Random random;

    private double duration;
    private double remainingTime;

    private double intensity;

    private int offsetX;
    private int offsetY;

    public ScreenShake() {
        random = new Random();

        duration = 0;
        remainingTime = 0;
        intensity = 0;

        offsetX = 0;
        offsetY = 0;
    }

    public void start(double duration, double intensity) {
        this.duration = duration;
        this.remainingTime = duration;
        this.intensity = intensity;
    }

    public void update(double deltaTime) {
        if (remainingTime <= 0) {
            offsetX = 0;
            offsetY = 0;
            return;
        }

        remainingTime -= deltaTime;

        double progress = Math.max(
                0,
                remainingTime / duration
        );

        double currentIntensity = intensity * progress;

        offsetX = (int) (
                (random.nextDouble() * 2 - 1) * currentIntensity
        );

        offsetY = (int) (
                (random.nextDouble() * 2 - 1) * currentIntensity
        );

        if (remainingTime <= 0) {
            remainingTime = 0;
            offsetX = 0;
            offsetY = 0;
        }
    }

    public void apply(Graphics2D g2d) {
        g2d.translate(offsetX, offsetY);
    }

    public boolean isShaking() {
        return remainingTime > 0;
    }

    public void stop() {
        remainingTime = 0;
        offsetX = 0;
        offsetY = 0;
    }

    public int getOffsetX() {
        return offsetX;
    }

    public int getOffsetY() {
        return offsetY;
    }
}
