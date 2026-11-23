
package com.nadhirfarhansuseno.kajitsuokirumono.core.managers;

import com.nadhirfarhansuseno.kajitsuokirumono.core.entities.Blade;

import java.awt.Point;
import java.util.List;

public class BladeManager {

    private final Blade blade;

    private double cooldown;
    private double cooldownTimer;

    public BladeManager() {
        blade = new Blade();
        cooldown = 0.15;
        cooldownTimer = 0;
    }

    public void update(
            double deltaTime,
            double mouseX,
            double mouseY,
            boolean mousePressed
    ) {
        cooldownTimer = Math.max(0, cooldownTimer - deltaTime);

        if (mousePressed) {
            if (!blade.isActive()) {
                blade.startSlicing(mouseX, mouseY);
            }
        } else {
            blade.stopSlicing();
            blade.update(mouseX, mouseY);
        }
    }

    public void processMovementPoint(Point point) {
        if (point == null || !blade.isActive()) {
            return;
        }

        blade.update(point.x, point.y);
    }

    public void processMovementPoints(List<Point> points) {
        if (points == null) {
            return;
        }

        for (Point point : points) {
            processMovementPoint(point);
        }
    }

    public boolean canSlice() {
        if (cooldownTimer > 0) {
            return false;
        }

        cooldownTimer = cooldown;
        return true;
    }

    public void setSpeedLevel(int speedLevel) {
        int safeLevel = Math.max(0, Math.min(10, speedLevel));
        cooldown = 0.30 - (safeLevel * 0.02);
    }

    public Blade getBlade() {
        return blade;
    }

    public double getCooldown() {
        return cooldown;
    }

    public double getCooldownTimer() {
        return cooldownTimer;
    }

    public void reset() {
        blade.stopSlicing();
        cooldownTimer = 0;
    }
}
