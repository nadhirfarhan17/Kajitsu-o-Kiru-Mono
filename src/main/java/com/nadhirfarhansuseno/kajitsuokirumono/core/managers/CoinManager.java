package com.nadhirfarhansuseno.kajitsuokirumono.core.managers;

public class CoinManager {

    private int currentCoins;
    private int totalCoins;

    public CoinManager() {
        currentCoins = 0;
        totalCoins = 0;
    }

    public void addCoins(int amount) {
        if (amount <= 0) {
            return;
        }

        currentCoins += amount;
    }

    public void bankCoins() {
        totalCoins += currentCoins;
        currentCoins = 0;
    }

    public boolean spendCoins(int amount) {
        if (amount <= 0 || currentCoins < amount) {
            return false;
        }

        currentCoins -= amount;
        return true;
    }

    public boolean spendTotalCoins(int amount) {
        if (amount <= 0 || totalCoins < amount) {
            return false;
        }

        totalCoins -= amount;
        return true;
    }

    public int getCurrentCoins() {
        return currentCoins;
    }

    public int getTotalCoins() {
        return totalCoins;
    }

    public void resetCurrentCoins() {
        currentCoins = 0;
    }

    public void resetAllCoins() {
        currentCoins = 0;
        totalCoins = 0;
    }
}