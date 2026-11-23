
package com.nadhirfarhansuseno.kajitsuokirumono.core.entities;

import java.awt.Color;

public class ToughFruit extends Fruit {

    private final String name;
    private final Color color;

    private int durability;

    public ToughFruit(
            double x,
            double y,
            double velocityX,
            double velocityY,
            int radius,
            String name,
            Color color,
            int durability
    ) {
        super(x, y, velocityX, velocityY, radius);

        this.name = name;
        this.color = color;
        this.durability = durability;
    }

    public void takeDamage() {
        if (isSliced()) {
            return;
        }

        durability--;

        if (durability <= 0) {
            slice();
        }
    }

    public String getName() {
        return name;
    }

    public Color getColor() {
        return color;
    }

    public int getDurability() {
        return durability;
    }
}
