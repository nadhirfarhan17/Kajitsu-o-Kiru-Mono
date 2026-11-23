
package com.nadhirfarhansuseno.kajitsuokirumono.frontend.effects;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

public class ParticleSystem {

    private final List<Particle> particles;
    private final Random random;

    public ParticleSystem() {
        particles = new ArrayList<>();
        random = new Random();
    }

    public synchronized void createEffect(
            double x,
            double y,
            Color color,
            int amount
    ) {
        if (color == null || amount <= 0) {
            return;
        }

        for (int i = 0; i < amount; i++) {

            double angle =
                    random.nextDouble() * Math.PI * 2;

            double speed =
                    100 + random.nextDouble() * 300;

            double velocityX =
                    Math.cos(angle) * speed;

            double velocityY =
                    Math.sin(angle) * speed - 80;

            double size =
                    random.nextDouble() * 5 + 3;

            double lifetime =
                    random.nextDouble() * 0.35 + 0.45;

            particles.add(
                    new Particle(
                            x,
                            y,
                            velocityX,
                            velocityY,
                            size,
                            lifetime,
                            color
                    )
            );
        }
    }

    public synchronized void update(double deltaTime) {
        Iterator<Particle> iterator =
                particles.iterator();

        while (iterator.hasNext()) {
            Particle particle = iterator.next();

            particle.update(deltaTime);

            if (particle.isExpired()) {
                iterator.remove();
            }
        }
    }

    public synchronized void render(Graphics2D g2d) {
        Graphics2D graphics =
                (Graphics2D) g2d.create();

        graphics.setRenderingHint(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON
        );

        for (Particle particle : particles) {
            particle.render(graphics);
        }

        graphics.dispose();
    }

    public synchronized void clear() {
        particles.clear();
    }

    private static class Particle {

        private static final double GRAVITY = 650.0;

        private double x;
        private double y;

        private final double velocityX;
        private double velocityY;

        private final double initialSize;
        private double size;

        private final double initialLifetime;
        private double lifetime;

        private final Color color;

        public Particle(
                double x,
                double y,
                double velocityX,
                double velocityY,
                double size,
                double lifetime,
                Color color
        ) {
            this.x = x;
            this.y = y;

            this.velocityX = velocityX;
            this.velocityY = velocityY;

            this.initialSize = size;
            this.size = size;

            this.initialLifetime = lifetime;
            this.lifetime = lifetime;

            this.color = color;
        }

        public void update(double deltaTime) {
            x += velocityX * deltaTime;
            y += velocityY * deltaTime;

            velocityY += GRAVITY * deltaTime;

            lifetime -= deltaTime;

            double progress = Math.max(
                    0,
                    lifetime / initialLifetime
            );

            size = initialSize * progress;
        }

        public void render(Graphics2D g2d) {
            if (lifetime <= 0 || size <= 0) {
                return;
            }

            float progress = (float) Math.max(
                    0,
                    Math.min(1, lifetime / initialLifetime)
            );

            int alpha = (int) (255 * progress);

            int particleSize = Math.max(
                    1,
                    (int) size
            );

            int drawX = (int) (x - particleSize / 2.0);
            int drawY = (int) (y - particleSize / 2.0);

            // Soft outer glow.
            g2d.setColor(
                    new Color(
                            color.getRed(),
                            color.getGreen(),
                            color.getBlue(),
                            Math.max(0, alpha / 4)
                    )
            );

            g2d.fillOval(
                    drawX - 3,
                    drawY - 3,
                    particleSize + 6,
                    particleSize + 6
            );

            // Main particle.
            g2d.setColor(
                    new Color(
                            color.getRed(),
                            color.getGreen(),
                            color.getBlue(),
                            alpha
                    )
            );

            g2d.fillOval(
                    drawX,
                    drawY,
                    particleSize,
                    particleSize
            );

            // Small highlight.
            g2d.setColor(
                    new Color(
                            255,
                            255,
                            255,
                            Math.max(0, alpha / 2)
                    )
            );

            g2d.setStroke(
                    new BasicStroke(1f)
            );

            g2d.drawOval(
                    drawX,
                    drawY,
                    particleSize,
                    particleSize
            );
        }

        public boolean isExpired() {
            return lifetime <= 0;
        }
    }
}
