
package com.nadhirfarhansuseno.kajitsuokirumono.core.entities;

public abstract class Fruit {

    private static final double GRAVITY = 900.0;
    private static final double BOUNCE_FACTOR = 0.65;

    protected double x;
    protected double y;

    protected double velocityX;
    protected double velocityY;

    protected int radius;

    protected boolean sliced;

    private double movementSpeedMultiplier;

    public Fruit(
            double x,
            double y,
            double velocityX,
            double velocityY,
            int radius
    ) {
        this.x = x;
        this.y = y;
        this.velocityX = velocityX;
        this.velocityY = velocityY;
        this.radius = radius;

        this.sliced = false;
        this.movementSpeedMultiplier = 1.0;
    }

    public void setX(double x) {
        this.x = x;
    }

    public void setVelocityX(double velocityX) {
        this.velocityX = velocityX;
    }

    public void setVelocityY(double velocityY) {
        this.velocityY = velocityY;
    }

    public void setMovementSpeedMultiplier(double multiplier) {
        if (Double.isFinite(multiplier) && multiplier > 0) {
            this.movementSpeedMultiplier = multiplier;
        }
    }

    public double getMovementSpeedMultiplier() {
        return movementSpeedMultiplier;
    }

    public void update(double deltaTime) {
        if (sliced) {
            return;
        }

        x += velocityX * deltaTime * movementSpeedMultiplier;
        y += velocityY * deltaTime * movementSpeedMultiplier;

        velocityY += GRAVITY * deltaTime;
    }

    public void update(
            double deltaTime,
            int screenWidth,
            int screenHeight
    ) {
        if (sliced) {
            return;
        }

        x += velocityX * deltaTime * movementSpeedMultiplier;
        y += velocityY * deltaTime * movementSpeedMultiplier;

        velocityY += GRAVITY * deltaTime;

        // Batas kiri
        if (x - radius < 0) {
            x = radius;
            velocityX = Math.abs(velocityX) * BOUNCE_FACTOR;
        }

        // Batas kanan
        if (x + radius > screenWidth) {
            x = screenWidth - radius;
            velocityX = -Math.abs(velocityX) * BOUNCE_FACTOR;
        }

        // Batas atas
        if (y - radius < 0) {
            y = radius;

            if (velocityY < 0) {
                velocityY = 0;
            }
        }
    }

    public void slice() {
        sliced = true;
    }

    public boolean isSliced() {
        return sliced;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getVelocityX() {
        return velocityX;
    }

    public double getVelocityY() {
        return velocityY;
    }

    public int getRadius() {
        return radius;
    }
}
