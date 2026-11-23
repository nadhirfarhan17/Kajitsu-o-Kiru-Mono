
package com.nadhirfarhansuseno.kajitsuokirumono.core.managers;

import com.nadhirfarhansuseno.kajitsuokirumono.core.systems.PotionSystem;
import com.nadhirfarhansuseno.kajitsuokirumono.shared.enums.PotionType;

public class ScoreManager {

    private int score;
    private final PotionSystem potionSystem;

    public ScoreManager(PotionSystem potionSystem) {
        this.potionSystem = potionSystem;
        this.score = 0;
    }

    public int addScore(int points) {
        if (potionSystem.isActive(PotionType.DOUBLE_POINTS)) {
            points *= 2;
        }

        score += points;

        return points;
    }

    public int getScore() {
        return score;
    }

    public void reset() {
        score = 0;
    }
}
