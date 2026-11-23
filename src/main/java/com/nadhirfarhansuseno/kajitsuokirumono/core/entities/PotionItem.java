
package com.nadhirfarhansuseno.kajitsuokirumono.core.entities;

import com.nadhirfarhansuseno.kajitsuokirumono.shared.enums.PotionType;

import java.awt.Color;

public class PotionItem extends Fruit {

    private final PotionType potionType;
    private final Color color;

    public PotionItem(
            double x,
            double y,
            double velocityX,
            double velocityY,
            int radius,
            PotionType potionType,
            Color color
    ) {
        super(x, y, velocityX, velocityY, radius);

        this.potionType = potionType;
        this.color = color;
    }

    public PotionType getPotionType() {
        return potionType;
    }

    public Color getColor() {
        return color;
    }
}
