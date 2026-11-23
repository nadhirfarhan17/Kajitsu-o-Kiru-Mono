
package com.nadhirfarhansuseno.kajitsuokirumono.shared.models;

public class PlayerStats {

    public static final int MIN_LEVEL = 0;
    public static final int MAX_LEVEL = 10;
    public static final int INITIAL_LEVEL = 5;

    private int durability;
    private int speed;

    public PlayerStats() {
        durability = INITIAL_LEVEL;
        speed = INITIAL_LEVEL;
    }

    public boolean upgradeDurability() {
        if (durability >= MAX_LEVEL) {
            return false;
        }

        durability++;
        return true;
    }

    public boolean upgradeSpeed() {
        if (speed >= MAX_LEVEL) {
            return false;
        }

        speed++;
        return true;
    }

    public void decreaseDurability(int amount) {
        if (amount <= 0) {
            return;
        }

        durability = Math.max(MIN_LEVEL, durability - amount);
    }

    public void decreaseSpeed(int amount) {
        if (amount <= 0) {
            return;
        }

        speed = Math.max(MIN_LEVEL, speed - amount);
    }

    public int getDurability() {
        return durability;
    }

    public int getSpeed() {
        return speed;
    }

    public void reset() {
        durability = INITIAL_LEVEL;
        speed = INITIAL_LEVEL;
    }
}
