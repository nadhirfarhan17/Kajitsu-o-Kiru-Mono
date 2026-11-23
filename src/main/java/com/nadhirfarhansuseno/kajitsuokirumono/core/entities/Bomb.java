
package com.nadhirfarhansuseno.kajitsuokirumono.core.entities;

public class Bomb extends Fruit {

    private final boolean instantGameOver;
    private boolean exploded;

    public Bomb(
            double x,
            double y,
            double velocityX,
            double velocityY,
            int radius,
            boolean instantGameOver
    ) {
        super(x, y, velocityX, velocityY, radius);

        this.instantGameOver = instantGameOver;
        this.exploded = false;
    }

    public boolean isInstantGameOver() {
        return instantGameOver;
    }

    public boolean isExploded() {
        return exploded;
    }

    public void explode() {
        if (exploded) {
            return;
        }

        exploded = true;
        slice();
    }
}
