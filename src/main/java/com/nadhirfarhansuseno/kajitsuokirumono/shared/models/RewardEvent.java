package com.nadhirfarhansuseno.kajitsuokirumono.shared.models;

public class RewardEvent {

    private final double x;
    private final double y;
    private final int score;
    private final int coins;

    public RewardEvent(
            double x,
            double y,
            int score,
            int coins
    ) {
        this.x = x;
        this.y = y;
        this.score = score;
        this.coins = coins;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public int getScore() {
        return score;
    }

    public int getCoins() {
        return coins;
    }
}