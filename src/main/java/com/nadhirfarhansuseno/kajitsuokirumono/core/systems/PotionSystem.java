
package com.nadhirfarhansuseno.kajitsuokirumono.core.systems;

import com.nadhirfarhansuseno.kajitsuokirumono.shared.enums.PotionType;

import java.util.EnumMap;
import java.util.Map;

public class PotionSystem {

    private final Map<PotionType, Double> activePotions;

    public PotionSystem() {
        activePotions = new EnumMap<>(PotionType.class);
    }

    public void activatePotion(PotionType potionType) {
        if (potionType == null) {
            return;
        }

        double duration = switch (potionType) {
            case FREEZE -> 5.0;
            case FRENZY -> 10.0;
            case DOUBLE_POINTS -> 15.0;
        };

        activePotions.put(potionType, duration);
    }

    public void update(double deltaTime) {
        if (!Double.isFinite(deltaTime) || deltaTime <= 0) {
            return;
        }

        activePotions.replaceAll(
                (type, remainingTime) -> remainingTime - deltaTime
        );

        activePotions.values().removeIf(
                remainingTime -> remainingTime <= 0
        );
    }

    public boolean isActive(PotionType potionType) {
        if (potionType == null) {
            return false;
        }

        return activePotions.containsKey(potionType);
    }

    public double getRemainingTime(PotionType potionType) {
        if (potionType == null) {
            return 0.0;
        }

        return activePotions.getOrDefault(potionType, 0.0);
    }

    public void reset() {
        activePotions.clear();
    }
}
