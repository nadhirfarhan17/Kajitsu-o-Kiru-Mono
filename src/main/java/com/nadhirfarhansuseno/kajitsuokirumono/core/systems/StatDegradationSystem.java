
package com.nadhirfarhansuseno.kajitsuokirumono.core.systems;

import com.nadhirfarhansuseno.kajitsuokirumono.shared.models.PlayerStats;

public class StatDegradationSystem {

    private static final double SPEED_DEGRADATION_INTERVAL = 30.0;
    private static final double DURABILITY_DEGRADATION_INTERVAL = 45.0;

    private final PlayerStats playerStats;

    private double speedTimer;
    private double durabilityTimer;

    private boolean active;

    public StatDegradationSystem(PlayerStats playerStats) {
        this.playerStats = playerStats;

        this.speedTimer = 0;
        this.durabilityTimer = 0;
        this.active = true;
    }

    public void update(double deltaTime) {
        if (!active || deltaTime <= 0) {
            return;
        }

        speedTimer += deltaTime;
        durabilityTimer += deltaTime;

        while (speedTimer >= SPEED_DEGRADATION_INTERVAL) {
            speedTimer -= SPEED_DEGRADATION_INTERVAL;

            if (playerStats.getSpeed() > 0) {
                decreaseSpeed(1);
            }
        }

        while (durabilityTimer >= DURABILITY_DEGRADATION_INTERVAL) {
            durabilityTimer -= DURABILITY_DEGRADATION_INTERVAL;

            if (playerStats.getDurability() > 0) {
                decreaseDurability(1);
            }
        }
    }

    public void stop() {
        active = false;
    }

    public boolean isActive() {
        return active;
    }

    public void decreaseDurability(int amount) {
        playerStats.decreaseDurability(amount);
    }

    public void decreaseSpeed(int amount) {
        playerStats.decreaseSpeed(amount);
    }

    public boolean isDurabilityWeak() {
        return playerStats.getDurability() <= 4;
    }

    public boolean isSpeedWeak() {
        return playerStats.getSpeed() <= 4;
    }

    public int getDurability() {
        return playerStats.getDurability();
    }

    public int getSpeed() {
        return playerStats.getSpeed();
    }
}
