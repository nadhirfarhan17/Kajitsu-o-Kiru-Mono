
package com.nadhirfarhansuseno.kajitsuokirumono.frontend.effects;

import com.nadhirfarhansuseno.kajitsuokirumono.shared.models.FruitSliceEvent;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class FruitSliceRenderer {

    private static final double DURATION = 0.55;

    private final List<SliceAnimation> animations = new ArrayList<>();

    public void addSlice(FruitSliceEvent event) {
        if (event == null) {
            return;
        }

        animations.add(new SliceAnimation(event));
    }

    public void update(double deltaTime) {
        Iterator<SliceAnimation> iterator = animations.iterator();

        while (iterator.hasNext()) {
            SliceAnimation animation = iterator.next();

            animation.elapsed += deltaTime;

            if (animation.elapsed >= DURATION) {
                iterator.remove();
            }
        }
    }

    public void render(Graphics2D g2d) {
        for (SliceAnimation animation : animations) {
            renderAnimation(g2d, animation);
        }
    }

    private void renderAnimation(
            Graphics2D g2d,
            SliceAnimation animation
    ) {
        double progress = Math.min(
                animation.elapsed / DURATION,
                1.0
        );

        double alpha = 1.0 - progress;

        double distance = animation.radius
                * 0.85
                * progress;

        double rotation = 0.65 * progress;

        double directionAngle = Math.atan2(
                animation.directionY,
                animation.directionX
        );

        drawHalf(
                g2d,
                animation,
                directionAngle,
                distance,
                rotation,
                alpha,
                1
        );

        drawHalf(
                g2d,
                animation,
                directionAngle,
                distance,
                rotation,
                alpha,
                -1
        );
    }

    private void drawHalf(
            Graphics2D original,
            SliceAnimation animation,
            double directionAngle,
            double distance,
            double rotation,
            double alpha,
            int side
    ) {
        Graphics2D g2d = (Graphics2D) original.create();

        try {
            g2d.setComposite(
                    AlphaComposite.getInstance(
                            AlphaComposite.SRC_OVER,
                            (float) Math.max(0, Math.min(1, alpha))
                    )
            );

            g2d.translate(animation.x, animation.y);

            g2d.rotate(directionAngle);

            g2d.translate(side * distance, 0);

            g2d.rotate(side * rotation);

            double radius = animation.radius;

            Ellipse2D circle = new Ellipse2D.Double(
                    -radius,
                    -radius,
                    radius * 2,
                    radius * 2
            );

            Rectangle2D halfRectangle;

            if (side > 0) {
                halfRectangle = new Rectangle2D.Double(
                        0,
                        -radius,
                        radius,
                        radius * 2
                );
            } else {
                halfRectangle = new Rectangle2D.Double(
                        -radius,
                        -radius,
                        radius,
                        radius * 2
                );
            }

            Area halfShape = new Area(circle);
            halfShape.intersect(new Area(halfRectangle));

            g2d.clip(halfShape);

            Color baseColor = animation.color;

            Color brighter = baseColor.brighter();
            Color darker = baseColor.darker();

            g2d.setPaint(
                    new GradientPaint(
                            0f,
                            (float) -radius,
                            brighter,
                            0f,
                            (float) radius,
                            darker
                    )
            );

            g2d.fill(circle);

            // Highlight pada permukaan buah
            g2d.setColor(new Color(
                    255,
                    255,
                    255,
                    65
            ));

            g2d.fillOval(
                    (int) (-radius * 0.55),
                    (int) (-radius * 0.65),
                    (int) (radius * 0.65),
                    (int) (radius * 0.55)
            );

            // Garis permukaan potongan
            g2d.setColor(new Color(
                    255,
                    245,
                    220,
                    180
            ));

            g2d.setStroke(new BasicStroke(3f));

            g2d.drawLine(
                    0,
                    (int) -radius,
                    0,
                    (int) radius
            );

            // Outline buah
            g2d.setColor(darker);
            g2d.setStroke(new BasicStroke(2f));
            g2d.draw(circle);

        } finally {
            g2d.dispose();
        }
    }

    public void clear() {
        animations.clear();
    }

    private static class SliceAnimation {

        private final double x;
        private final double y;

        private final int radius;

        private final Color color;

        private final double directionX;
        private final double directionY;

        private double elapsed;

        private SliceAnimation(FruitSliceEvent event) {
            this.x = event.getX();
            this.y = event.getY();

            this.radius = event.getRadius();
            this.color = event.getColor();

            this.directionX = event.getDirectionX();
            this.directionY = event.getDirectionY();

            this.elapsed = 0;
        }
    }
}
