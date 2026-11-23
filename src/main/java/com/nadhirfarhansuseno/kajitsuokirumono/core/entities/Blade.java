
package com.nadhirfarhansuseno.kajitsuokirumono.core.entities;

public class Blade {

    private double x;
    private double y;

    private double previousX;
    private double previousY;

    private boolean active;

    public Blade() {
        this.x = 0;
        this.y = 0;

        this.previousX = 0;
        this.previousY = 0;

        this.active = false;
    }

    public void update(double mouseX, double mouseY) {
        previousX = x;
        previousY = y;

        x = mouseX;
        y = mouseY;
    }

    public void startSlicing(double mouseX, double mouseY) {
        x = mouseX;
        y = mouseY;

        previousX = mouseX;
        previousY = mouseY;

        active = true;
    }

    public void stopSlicing() {
        active = false;
    }

    public boolean isActive() {
        return active;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getPreviousX() {
        return previousX;
    }

    public double getPreviousY() {
        return previousY;
    }
}
