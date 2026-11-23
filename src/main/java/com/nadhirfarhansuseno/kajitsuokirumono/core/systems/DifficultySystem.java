
package com.nadhirfarhansuseno.kajitsuokirumono.core.systems;

public class DifficultySystem {

    public enum DifficultyLevel {
        EASY,
        NORMAL,
        HARD,
        EXPERT,
        EXTREME
    }

    private double elapsedTime;
    private double difficultyMultiplier;

    public DifficultySystem() {
        reset();
    }

    public void update(double deltaTime) {
        elapsedTime += deltaTime;

        difficultyMultiplier = Math.min(
                1.0 + (elapsedTime / 30.0) * 0.1,
                2.5
        );
    }

    public DifficultyLevel getDifficultyLevel() {
        if (elapsedTime < 30) {
            return DifficultyLevel.EASY;
        } else if (elapsedTime < 60) {
            return DifficultyLevel.NORMAL;
        } else if (elapsedTime < 120) {
            return DifficultyLevel.HARD;
        } else if (elapsedTime < 180) {
            return DifficultyLevel.EXPERT;
        } else {
            return DifficultyLevel.EXTREME;
        }
    }

    public double getSpawnInterval() {
        switch (getDifficultyLevel()) {
            case EASY:
                return 1.5;
            case NORMAL:
                return 1.3;
            case HARD:
                return 1.1;
            case EXPERT:
                return 0.9;
            case EXTREME:
                return 0.7;
            default:
                return 1.5;
        }
    }

    public int getMinSpawnAmount() {
        switch (getDifficultyLevel()) {
            case EASY:
                return 1;
            case NORMAL:
                return 2;
            case HARD:
                return 3;
            case EXPERT:
                return 4;
            case EXTREME:
                return 4;
            default:
                return 1;
        }
    }

    public int getMaxSpawnAmount() {
        switch (getDifficultyLevel()) {
            case EASY:
                return 2;
            case NORMAL:
                return 3;
            case HARD:
                return 4;
            case EXPERT:
                return 5;
            case EXTREME:
                return 6;
            default:
                return 2;
        }
    }

    public double getToughFruitChance() {
        switch (getDifficultyLevel()) {
            case EASY:
                return 0.15;
            case NORMAL:
                return 0.20;
            case HARD:
                return 0.25;
            case EXPERT:
                return 0.30;
            case EXTREME:
                return 0.35;
            default:
                return 0.15;
        }
    }

    public double getBombChance() {
        switch (getDifficultyLevel()) {
            case EASY:
                return 0.03;
            case NORMAL:
                return 0.05;
            case HARD:
                return 0.07;
            case EXPERT:
                return 0.09;
            case EXTREME:
                return 0.12;
            default:
                return 0.03;
        }
    }

    public double getDifficultyMultiplier() {
        return difficultyMultiplier;
    }

    public double getElapsedTime() {
        return elapsedTime;
    }

    public void reset() {
        elapsedTime = 0;
        difficultyMultiplier = 1.0;
    }
}
