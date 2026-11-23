
package com.nadhirfarhansuseno.kajitsuokirumono.core.systems;

import com.nadhirfarhansuseno.kajitsuokirumono.core.entities.Bomb;
import com.nadhirfarhansuseno.kajitsuokirumono.core.entities.NormalFruit;
import com.nadhirfarhansuseno.kajitsuokirumono.core.entities.PotionItem;
import com.nadhirfarhansuseno.kajitsuokirumono.core.entities.ToughFruit;
import com.nadhirfarhansuseno.kajitsuokirumono.shared.enums.PotionType;

import java.awt.Color;
import java.util.Random;

public class SpawnSystem {

    private final Random random = new Random();

    private static final String[] FRUIT_NAMES = {
            "Apple", "Orange", "Watermelon", "Banana",
            "Grape", "Strawberry", "Pineapple", "Peach"
    };

    private static final Color[] FRUIT_COLORS = {
            new Color(220, 50, 50),
            new Color(255, 150, 30),
            new Color(50, 180, 80),
            new Color(255, 225, 70),
            new Color(130, 70, 180),
            new Color(240, 70, 100),
            new Color(245, 210, 50),
            new Color(255, 170, 140)
    };

    private static final PotionType[] POTION_TYPES = {
            PotionType.FREEZE,
            PotionType.FRENZY,
            PotionType.DOUBLE_POINTS
    };

    public NormalFruit spawnNormalFruit(
            int screenWidth,
            int screenHeight,
            int durability,
            double speedMultiplier
    ) {
        int index = random.nextInt(FRUIT_NAMES.length);
        int radius = random.nextInt(11) + 30;

        double x = random.nextInt(screenWidth - radius * 2) + radius;
        double y = screenHeight + radius;

        double velocityX = (random.nextDouble() * 400 - 200)
                * speedMultiplier;
        double velocityY = -(random.nextDouble() * 200 + 700)
                * speedMultiplier;

        return new NormalFruit(
                x, y, velocityX, velocityY, radius,
                FRUIT_NAMES[index], FRUIT_COLORS[index], durability
        );
    }

    public ToughFruit spawnToughFruit(
            int screenWidth,
            int screenHeight,
            int durability,
            double speedMultiplier
    ) {
        int radius = 45;

        double x = random.nextInt(screenWidth - radius * 2) + radius;
        double y = screenHeight + radius;

        double velocityX = (random.nextDouble() * 300 - 150)
                * speedMultiplier;
        double velocityY = -(random.nextDouble() * 150 + 650)
                * speedMultiplier;

        return new ToughFruit(
                x, y, velocityX, velocityY, radius,
                "Tough Fruit", new Color(120, 100, 80), durability
        );
    }

    public Bomb spawnBomb(
            int screenWidth,
            int screenHeight,
            boolean instantGameOver,
            double speedMultiplier
    ) {
        int radius = instantGameOver ? 25 : 35;

        double x = random.nextInt(screenWidth - radius * 2) + radius;
        double y = screenHeight + radius;

        double velocityX = (random.nextDouble() * 300 - 150)
                * speedMultiplier;
        double velocityY = -(random.nextDouble() * 200 + 700)
                * speedMultiplier;

        return new Bomb(
                x, y, velocityX, velocityY, radius, instantGameOver
        );
    }

    public PotionItem spawnPotion(
            int screenWidth,
            int screenHeight,
            double speedMultiplier
    ) {
        int radius = 30;

        double x = random.nextInt(screenWidth - radius * 2) + radius;
        double y = screenHeight + radius;

        double velocityX = (random.nextDouble() * 300 - 150)
                * speedMultiplier;
        double velocityY = -(random.nextDouble() * 200 + 700)
                * speedMultiplier;

        PotionType type = POTION_TYPES[
                random.nextInt(POTION_TYPES.length)
                ];

        Color color = switch (type) {
            case FREEZE -> new Color(80, 190, 255);
            case FRENZY -> new Color(255, 100, 70);
            case DOUBLE_POINTS -> new Color(255, 210, 60);
        };

        return new PotionItem(
                x, y, velocityX, velocityY,
                radius, type, color
        );
    }
}
