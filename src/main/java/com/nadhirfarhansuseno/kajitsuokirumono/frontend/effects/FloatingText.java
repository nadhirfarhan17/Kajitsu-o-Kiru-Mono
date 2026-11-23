
package com.nadhirfarhansuseno.kajitsuokirumono.frontend.effects;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class FloatingText {

    private final List<TextParticle> texts;

    public FloatingText() {
        texts = new ArrayList<>();
    }

    public void addText(
            String text,
            double x,
            double y,
            Color color
    ) {
        texts.add(
                new TextParticle(
                        text,
                        x,
                        y,
                        color
                )
        );
    }

    public void update(double deltaTime) {
        Iterator<TextParticle> iterator = texts.iterator();

        while (iterator.hasNext()) {
            TextParticle particle = iterator.next();

            particle.update(deltaTime);

            if (particle.isExpired()) {
                iterator.remove();
            }
        }
    }

    public void render(Graphics2D g2d) {
        for (TextParticle particle : texts) {
            particle.render(g2d);
        }
    }

    public void clear() {
        texts.clear();
    }

    private static class TextParticle {

        private final String text;

        private double x;
        private double y;

        private final Color color;

        private double lifetime;

        private static final double MAX_LIFETIME = 1.0;
        private static final double FLOAT_SPEED = 60.0;

        public TextParticle(
                String text,
                double x,
                double y,
                Color color
        ) {
            this.text = text;
            this.x = x;
            this.y = y;
            this.color = color;

            lifetime = MAX_LIFETIME;
        }

        public void update(double deltaTime) {
            y -= FLOAT_SPEED * deltaTime;
            lifetime -= deltaTime;
        }

        public void render(Graphics2D g2d) {
            int alpha = (int) (
                    255 * Math.max(
                            0,
                            Math.min(1, lifetime / MAX_LIFETIME)
                    )
            );

            g2d.setFont(
                    new Font("SansSerif", Font.BOLD, 24)
            );

            g2d.setColor(
                    new Color(
                            color.getRed(),
                            color.getGreen(),
                            color.getBlue(),
                            alpha
                    )
            );

            g2d.drawString(
                    text,
                    (int) x,
                    (int) y
            );
        }

        public boolean isExpired() {
            return lifetime <= 0;
        }
    }
}
