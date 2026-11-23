
package com.nadhirfarhansuseno.kajitsuokirumono.core.systems;

import java.util.Random;

public class CoinDropSystem {

    private static final double NORMAL_DROP_CHANCE = 0.70;
    private static final double TOUGH_DROP_CHANCE = 0.85;

    private static final int NORMAL_MIN_COINS = 1;
    private static final int NORMAL_MAX_COINS = 3;

    private static final int TOUGH_MIN_COINS = 3;
    private static final int TOUGH_MAX_COINS = 5;

    private final Random random;

    public CoinDropSystem() {
        this.random = new Random();
    }

    public int calculateNormalFruitCoins() {
        return calculateCoins(
                NORMAL_DROP_CHANCE,
                NORMAL_MIN_COINS,
                NORMAL_MAX_COINS
        );
    }

    public int calculateToughFruitCoins() {
        return calculateCoins(
                TOUGH_DROP_CHANCE,
                TOUGH_MIN_COINS,
                TOUGH_MAX_COINS
        );
    }

    private int calculateCoins(
            double dropChance,
            int minCoins,
            int maxCoins
    ) {
        if (random.nextDouble() >= dropChance) {
            return 0;
        }

        return random.nextInt(maxCoins - minCoins + 1) + minCoins;
    }
}
