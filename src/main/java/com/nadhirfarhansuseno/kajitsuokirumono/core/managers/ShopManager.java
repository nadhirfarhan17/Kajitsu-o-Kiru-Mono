
package com.nadhirfarhansuseno.kajitsuokirumono.core.managers;

import com.nadhirfarhansuseno.kajitsuokirumono.shared.enums.PotionType;
import com.nadhirfarhansuseno.kajitsuokirumono.shared.models.PlayerStats;

public class ShopManager {

    private static final int BASE_UPGRADE_PRICE = 100;
    private static final int PRICE_INCREMENT = 50;

    private static final int FREEZE_PRICE = 150;
    private static final int FRENZY_PRICE = 200;
    private static final int DOUBLE_POINTS_PRICE = 250;

    private final CoinManager coinManager;
    private final PlayerStats playerStats;
    private final InventoryManager inventoryManager;

    public ShopManager(
            CoinManager coinManager,
            PlayerStats playerStats
    ) {
        this(coinManager, playerStats, null);
    }

    public ShopManager(
            CoinManager coinManager,
            PlayerStats playerStats,
            InventoryManager inventoryManager
    ) {
        this.coinManager = coinManager;
        this.playerStats = playerStats;
        this.inventoryManager = inventoryManager;
    }

    public boolean buyDurabilityUpgrade() {
        if (playerStats.getDurability() >= PlayerStats.MAX_LEVEL) {
            return false;
        }

        int price = getDurabilityUpgradePrice();

        if (!coinManager.spendTotalCoins(price)) {
            return false;
        }

        return playerStats.upgradeDurability();
    }

    public boolean buySpeedUpgrade() {
        if (playerStats.getSpeed() >= PlayerStats.MAX_LEVEL) {
            return false;
        }

        int price = getSpeedUpgradePrice();

        if (!coinManager.spendTotalCoins(price)) {
            return false;
        }

        return playerStats.upgradeSpeed();
    }

    public boolean buyPotion(PotionType type) {
        if (inventoryManager == null || type == null) {
            return false;
        }

        int price = getPotionPrice(type);

        if (inventoryManager.getPotionCount(type) >= 5) {
            return false;
        }

        if (!coinManager.spendTotalCoins(price)) {
            return false;
        }

        return inventoryManager.addPotion(type);
    }

    public int getPotionPrice(PotionType type) {
        if (type == null) {
            return 0;
        }

        return switch (type) {
            case FREEZE -> FREEZE_PRICE;
            case FRENZY -> FRENZY_PRICE;
            case DOUBLE_POINTS -> DOUBLE_POINTS_PRICE;
        };
    }

    public int getDurabilityUpgradePrice() {
        int upgrades = playerStats.getDurability()
                - PlayerStats.INITIAL_LEVEL;

        return BASE_UPGRADE_PRICE
                + Math.max(0, upgrades) * PRICE_INCREMENT;
    }

    public int getSpeedUpgradePrice() {
        int upgrades = playerStats.getSpeed()
                - PlayerStats.INITIAL_LEVEL;

        return BASE_UPGRADE_PRICE
                + Math.max(0, upgrades) * PRICE_INCREMENT;
    }
}
