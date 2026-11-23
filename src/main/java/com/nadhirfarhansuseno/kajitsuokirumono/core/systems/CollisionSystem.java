
package com.nadhirfarhansuseno.kajitsuokirumono.core.systems;

import com.nadhirfarhansuseno.kajitsuokirumono.core.entities.Blade;
import com.nadhirfarhansuseno.kajitsuokirumono.core.entities.Fruit;

public class CollisionSystem {

    public boolean checkCollision(Blade blade, Fruit fruit) {
        if (!blade.isActive() || fruit.isSliced()) {
            return false;
        }

        return checkSegmentCollision(
                fruit,
                blade.getPreviousX(),
                blade.getPreviousY(),
                blade.getX(),
                blade.getY()
        );
    }

    public boolean checkSegmentCollision(
            Fruit fruit,
            double startX,
            double startY,
            double endX,
            double endY
    ) {
        if (fruit == null || fruit.isSliced()) {
            return false;
        }

        double distance = distanceToSegment(
                fruit.getX(),
                fruit.getY(),
                startX,
                startY,
                endX,
                endY
        );

        return distance <= fruit.getRadius();
    }

    private double distanceToSegment(
            double pointX,
            double pointY,
            double startX,
            double startY,
            double endX,
            double endY
    ) {
        double segmentX = endX - startX;
        double segmentY = endY - startY;

        double lengthSquared =
                segmentX * segmentX + segmentY * segmentY;

        if (lengthSquared == 0) {
            return Math.hypot(
                    pointX - startX,
                    pointY - startY
            );
        }

        double t = (
                (pointX - startX) * segmentX
                        + (pointY - startY) * segmentY
        ) / lengthSquared;

        t = Math.max(0, Math.min(1, t));

        double closestX = startX + t * segmentX;
        double closestY = startY + t * segmentY;

        return Math.hypot(
                pointX - closestX,
                pointY - closestY
        );
    }
}
