
package com.nadhirfarhansuseno.kajitsuokirumono.core.managers;

import com.nadhirfarhansuseno.kajitsuokirumono.shared.models.PlayerStats;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

public class SaveManager {

    private static final String SAVE_PATH = "data/save.properties";

    private final Properties properties;

    public SaveManager() {
        properties = new Properties();
        load();
    }

    public void save(
            CoinManager coinManager,
            PlayerStats playerStats,
            int highScore
    ) {
        properties.setProperty(
                "totalCoins",
                String.valueOf(coinManager.getTotalCoins())
        );

        properties.setProperty(
                "durability",
                String.valueOf(playerStats.getDurability())
        );

        properties.setProperty(
                "speed",
                String.valueOf(playerStats.getSpeed())
        );

        properties.setProperty(
                "highScore",
                String.valueOf(highScore)
        );

        try (FileOutputStream output =
                     new FileOutputStream(SAVE_PATH)) {

            properties.store(output, "Kajitsu o Kiru Mono Save Data");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void load() {
        try (FileInputStream input =
                     new FileInputStream(SAVE_PATH)) {

            properties.load(input);

        } catch (IOException e) {
            // Save file does not exist yet.
        }
    }

    public int getSavedValue(String key, int defaultValue) {
        try {
            return Integer.parseInt(
                    properties.getProperty(
                            key,
                            String.valueOf(defaultValue)
                    )
            );
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    public void applySavedData(
            CoinManager coinManager,
            PlayerStats playerStats
    ) {
        // Restore permanent coins.
        int coins = Math.max(0, getSavedValue("totalCoins", 0));

        coinManager.resetAllCoins();

        coinManager.addCoins(coins);
        coinManager.bankCoins();

        // Restore player upgrades.
        int durability = Math.max(
                PlayerStats.MIN_LEVEL,
                Math.min(
                        PlayerStats.MAX_LEVEL,
                        getSavedValue(
                                "durability",
                                PlayerStats.INITIAL_LEVEL
                        )
                )
        );

        int speed = Math.max(
                PlayerStats.MIN_LEVEL,
                Math.min(
                        PlayerStats.MAX_LEVEL,
                        getSavedValue(
                                "speed",
                                PlayerStats.INITIAL_LEVEL
                        )
                )
        );

        playerStats.reset();

        for (int i = PlayerStats.INITIAL_LEVEL; i < durability; i++) {
            playerStats.upgradeDurability();
        }

        for (int i = PlayerStats.INITIAL_LEVEL; i < speed; i++) {
            playerStats.upgradeSpeed();
        }
    }
}
