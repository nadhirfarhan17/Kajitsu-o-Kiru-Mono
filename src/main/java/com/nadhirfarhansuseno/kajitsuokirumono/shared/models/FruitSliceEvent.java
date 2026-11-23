
package com.nadhirfarhansuseno.kajitsuokirumono.shared.models;

import java.awt.Color;

public class FruitSliceEvent {

    private final double x;
    private final double y;

    private final int radius;

    private final Color color;

    private final double directionX;
    private final double directionY;

    public FruitSliceEvent(
            double x,
            double y,
            int radius,
            Color color,
            double directionX,
            double directionY
    ) {
        this.x = x;
        this.y = y;
        this.radius = radius;
        this.color = color;

        double length = Math.hypot(directionX, directionY);

        if (length > 0.0001) {
            this.directionX = directionX / length;
            this.directionY = directionY / length;
        } else {
            this.directionX = 1.0;
            this.directionY = 0.0;
        }
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public int getRadius() {
        return radius;
    }

    public Color getColor() {
        return color;
    }

    public double getDirectionX() {
        return directionX;
    }

    public double getDirectionY() {
        return directionY;
    }
}
