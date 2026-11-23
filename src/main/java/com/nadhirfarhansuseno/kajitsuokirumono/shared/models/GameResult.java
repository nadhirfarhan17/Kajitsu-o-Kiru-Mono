
package com.nadhirfarhansuseno.kajitsuokirumono.shared.models;

import java.time.LocalDateTime;

public class GameResult {

    private final int score;
    private final int coinsEarned;
    private final long duration;
    private final LocalDateTime date;

    public GameResult(
            int score,
            int coinsEarned,
            long duration
    ) {
        this.score = score;
        this.coinsEarned = coinsEarned;
        this.duration = duration;
        this.date = LocalDateTime.now();
    }

    public int getScore() {
        return score;
    }

    public int getCoinsEarned() {
        return coinsEarned;
    }

    public long getDuration() {
        return duration;
    }

    public LocalDateTime getDate() {
        return date;
    }
}
