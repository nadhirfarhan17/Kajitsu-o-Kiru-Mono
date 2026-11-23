
package com.nadhirfarhansuseno.kajitsuokirumono.frontend.effects;

import com.nadhirfarhansuseno.kajitsuokirumono.shared.models.RewardEvent;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class FloatingRewardRenderer {

    private static final double DURATION = 1.1;
    private static final double FLOAT_SPEED = 65.0;

    private static final Font SCORE_FONT =
            new Font("Arial", Font.BOLD, 22);

    private static final Font COIN_FONT =
            new Font("Arial", Font.BOLD, 18);

    private final List<FloatingReward> rewards;

    public FloatingRewardRenderer() {
        this.rewards = new ArrayList<>();
    }

    public void addReward(RewardEvent event) {
        if (event == null) {
            return;
        }

        if (event.getScore() <= 0 && event.getCoins() <= 0) {
            return;
        }

        rewards.add(new FloatingReward(
                event.getX(),
                event.getY(),
                event.getScore(),
                event.getCoins()
        ));
    }

    public void update(double deltaTime) {
        if (!Double.isFinite(deltaTime) || deltaTime <= 0) {
            return;
        }

        Iterator<FloatingReward> iterator = rewards.iterator();

        while (iterator.hasNext()) {
            FloatingReward reward = iterator.next();

            reward.update(deltaTime);

            if (reward.isExpired()) {
                iterator.remove();
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
            for (FloatingReward reward : rewards) {
                float alpha = (float) Math.max(
                        0,
                        1.0 - reward.getElapsedTime() / DURATION
                );

                graphics.setComposite(
                        AlphaComposite.getInstance(
                                AlphaComposite.SRC_OVER,
                                alpha
                        )
                );

                drawReward(graphics, reward);
            }
        } finally {
            graphics.dispose();
        }
    }

    private void drawReward(
            Graphics2D g2d,
            FloatingReward reward
    ) {
        int x = (int) reward.getX();
        int y = (int) reward.getY();

        if (reward.getScore() > 0) {
            drawText(
                    g2d,
                    "+" + reward.getScore(),
                    x,
                    y,
                    SCORE_FONT,
                    new Color(255, 235, 120)
            );
        }

        if (reward.getCoins() > 0) {
            drawText(
                    g2d,
                    "+" + reward.getCoins() + " Coins",
                    x,
                    y + 24,
                    COIN_FONT,
                    new Color(255, 195, 55)
            );
        }
    }

    private void drawText(
            Graphics2D g2d,
            String text,
            int x,
            int y,
            Font font,
            Color color
    ) {
        g2d.setFont(font);

        FontMetrics metrics = g2d.getFontMetrics();

        int textX = x - metrics.stringWidth(text) / 2;

        g2d.setColor(new Color(0, 0, 0, 170));
        g2d.drawString(text, textX + 2, y + 2);

        g2d.setColor(color);
        g2d.drawString(text, textX, y);
    }

    private static class FloatingReward {

        private double x;
        private double y;

        private final int score;
        private final int coins;

        private double elapsedTime;

        public FloatingReward(
                double x,
                double y,
                int score,
                int coins
        ) {
            this.x = x;
            this.y = y;
            this.score = score;
            this.coins = coins;
            this.elapsedTime = 0;
        }

        public void update(double deltaTime) {
            elapsedTime += deltaTime;
            y -= FLOAT_SPEED * deltaTime;
        }

        public boolean isExpired() {
            return elapsedTime >= DURATION;
        }

        public double getX() {
            return x;
        }

        public double getY() {
            return y;
        }

        public int getScore() {
            return score;
        }

        public int getCoins() {
            return coins;
        }

        public double getElapsedTime() {
            return elapsedTime;
        }
    }
}
