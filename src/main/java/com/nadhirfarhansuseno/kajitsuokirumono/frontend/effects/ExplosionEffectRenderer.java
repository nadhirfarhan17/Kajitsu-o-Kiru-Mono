
package com.nadhirfarhansuseno.kajitsuokirumono.frontend.effects;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

public class ExplosionEffectRenderer {

    private static final Random RANDOM = new Random();

    private static final double PARTICLE_LIFETIME = 0.65;
    private static final double SHOCKWAVE_LIFETIME = 0.45;

    private final List<ExplosionParticle> particles;
    private final List<Shockwave> shockwaves;

    public ExplosionEffectRenderer() {
        particles = new ArrayList<>();
        shockwaves = new ArrayList<>();
    }

    public void addBlackBombExplosion(double x, double y) {
        createExplosion(
                x,
                y,
                35,
                180,
                new Color[]{
                        Color.BLACK,
                        new Color(55, 55, 55),
                        new Color(255, 125, 35),
                        new Color(255, 190, 70)
                }
        );
    }

    public void addRedBombExplosion(double x, double y) {
        createExplosion(
                x,
                y,
                55,
                260,
                new Color[]{
                        new Color(220, 25, 25),
                        new Color(255, 75, 25),
                        new Color(255, 145, 35),
                        Color.YELLOW
                }
        );
    }

    private void createExplosion(
            double x,
            double y,
            int particleCount,
            double maxSpeed,
            Color[] colors
    ) {
        shockwaves.add(new Shockwave(x, y));

        for (int i = 0; i < particleCount; i++) {
            double angle = RANDOM.nextDouble() * Math.PI * 2;
            double speed = 50 + RANDOM.nextDouble() * maxSpeed;

            double velocityX = Math.cos(angle) * speed;
            double velocityY = Math.sin(angle) * speed;

            double size = 3 + RANDOM.nextDouble() * 6;

            Color color = colors[
                    RANDOM.nextInt(colors.length)
                    ];

            particles.add(
                    new ExplosionParticle(
                            x,
                            y,
                            velocityX,
                            velocityY,
                            size,
                            color
                    )
            );
        }
    }

    public void update(double deltaTime) {
        if (!Double.isFinite(deltaTime) || deltaTime <= 0) {
            return;
        }

        Iterator<ExplosionParticle> particleIterator =
                particles.iterator();

        while (particleIterator.hasNext()) {
            ExplosionParticle particle = particleIterator.next();

            particle.update(deltaTime);

            if (particle.isExpired()) {
                particleIterator.remove();
            }
        }

        Iterator<Shockwave> shockwaveIterator =
                shockwaves.iterator();

        while (shockwaveIterator.hasNext()) {
            Shockwave shockwave = shockwaveIterator.next();

            shockwave.update(deltaTime);

            if (shockwave.isExpired()) {
                shockwaveIterator.remove();
            }
        }
    }

    public void render(Graphics2D g2d) {
        if (g2d == null) {
            return;
        }

        Graphics2D graphics =
                (Graphics2D) g2d.create();

        try {
            for (Shockwave shockwave : shockwaves) {
                renderShockwave(graphics, shockwave);
            }

            for (ExplosionParticle particle : particles) {
                renderParticle(graphics, particle);
            }
        } finally {
            graphics.dispose();
        }
    }

    private void renderShockwave(
            Graphics2D g2d,
            Shockwave shockwave
    ) {
        float progress = (float) (
                shockwave.getElapsedTime() / SHOCKWAVE_LIFETIME
        );

        float alpha = Math.max(0, 1 - progress);

        int radius = (int) (progress * 100);
        int diameter = radius * 2;

        Graphics2D graphics =
                (Graphics2D) g2d.create();

        try {
            graphics.setComposite(
                    AlphaComposite.getInstance(
                            AlphaComposite.SRC_OVER,
                            alpha
                    )
            );

            graphics.setColor(
                    new Color(255, 155, 65)
            );

            graphics.setStroke(
                    new BasicStroke(
                            Math.max(1, 7 * (1 - progress))
                    )
            );

            graphics.drawOval(
                    (int) shockwave.getX() - radius,
                    (int) shockwave.getY() - radius,
                    diameter,
                    diameter
            );
        } finally {
            graphics.dispose();
        }
    }

    private void renderParticle(
            Graphics2D g2d,
            ExplosionParticle particle
    ) {
        float alpha = (float) Math.max(
                0,
                1 - particle.getElapsedTime() / PARTICLE_LIFETIME
        );

        int size = (int) Math.max(1, particle.getSize());

        Graphics2D graphics =
                (Graphics2D) g2d.create();

        try {
            graphics.setComposite(
                    AlphaComposite.getInstance(
                            AlphaComposite.SRC_OVER,
                            alpha
                    )
            );

            graphics.setColor(particle.getColor());

            graphics.fillOval(
                    (int) particle.getX() - size / 2,
                    (int) particle.getY() - size / 2,
                    size,
                    size
            );
        } finally {
            graphics.dispose();
        }
    }

    private static class ExplosionParticle {

        private double x;
        private double y;

        private final double velocityX;
        private double velocityY;

        private final double size;
        private final Color color;

        private double elapsedTime;

        public ExplosionParticle(
                double x,
                double y,
                double velocityX,
                double velocityY,
                double size,
                Color color
        ) {
            this.x = x;
            this.y = y;
            this.velocityX = velocityX;
            this.velocityY = velocityY;
            this.size = size;
            this.color = color;
            this.elapsedTime = 0;
        }

        public void update(double deltaTime) {
            elapsedTime += deltaTime;

            x += velocityX * deltaTime;
            y += velocityY * deltaTime;

            velocityY += 180 * deltaTime;
        }

        public boolean isExpired() {
            return elapsedTime >= PARTICLE_LIFETIME;
        }

        public double getX() {
            return x;
        }

        public double getY() {
            return y;
        }

        public double getSize() {
            return size;
        }

        public Color getColor() {
            return color;
        }

        public double getElapsedTime() {
            return elapsedTime;
        }
    }

    private static class Shockwave {

        private final double x;
        private final double y;

        private double elapsedTime;

        public Shockwave(double x, double y) {
            this.x = x;
            this.y = y;
            this.elapsedTime = 0;
        }

        public void update(double deltaTime) {
            elapsedTime += deltaTime;
        }

        public boolean isExpired() {
            return elapsedTime >= SHOCKWAVE_LIFETIME;
        }

        public double getX() {
            return x;
        }

        public double getY() {
            return y;
        }

        public double getElapsedTime() {
            return elapsedTime;
        }
    }
}
