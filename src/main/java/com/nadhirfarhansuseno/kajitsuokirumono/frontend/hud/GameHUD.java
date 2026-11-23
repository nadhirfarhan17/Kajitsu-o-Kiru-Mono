
package com.nadhirfarhansuseno.kajitsuokirumono.frontend.hud;

import com.nadhirfarhansuseno.kajitsuokirumono.core.engine.GameSession;
import com.nadhirfarhansuseno.kajitsuokirumono.shared.enums.PotionType;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;

public class GameHUD {

    private static final Color TEXT_COLOR = Color.WHITE;
    private static final Color ACCENT_COLOR = new Color(255, 190, 75);

    private final GameSession gameSession;

    public GameHUD(GameSession gameSession) {
        this.gameSession = gameSession;
    }

    public void render(Graphics2D g2d) {
        drawScore(g2d);
        drawLives(g2d);
        drawTime(g2d);
        drawCoins(g2d);
        drawPotionInventory(g2d);
        drawActivePotions(g2d);
    }

    private void drawScore(Graphics2D g2d) {
        g2d.setFont(new Font("SansSerif", Font.BOLD, 26));
        g2d.setColor(TEXT_COLOR);

        g2d.drawString(
                "SCORE: " + gameSession.getScoreManager().getScore(),
                40,
                50
        );
    }

    private void drawLives(Graphics2D g2d) {
        g2d.setFont(new Font("SansSerif", Font.BOLD, 22));
        g2d.setColor(TEXT_COLOR);

        g2d.drawString(
                "LIVES: " + gameSession.getLives(),
                40,
                85
        );
    }

    private void drawTime(Graphics2D g2d) {
        int totalSeconds = (int) gameSession.getElapsedTime();

        int minutes = totalSeconds / 60;
        int seconds = totalSeconds % 60;

        g2d.setFont(new Font("SansSerif", Font.BOLD, 22));
        g2d.setColor(TEXT_COLOR);

        g2d.drawString(
                String.format("TIME: %02d:%02d", minutes, seconds),
                40,
                120
        );
    }

    private void drawCoins(Graphics2D g2d) {
        g2d.setFont(new Font("SansSerif", Font.BOLD, 24));
        g2d.setColor(ACCENT_COLOR);

        g2d.drawString(
                "COINS: " + gameSession.getCoinManager().getCurrentCoins(),
                1030,
                50
        );
    }

    private void drawPotionInventory(Graphics2D g2d) {
        int startY = 150;

        g2d.setFont(new Font("SansSerif", Font.BOLD, 18));
        g2d.setColor(TEXT_COLOR);

        g2d.drawString("POTIONS", 1080, 120);

        PotionType[] types = PotionType.values();

        for (int i = 0; i < types.length; i++) {
            PotionType type = types[i];

            int count = gameSession
                    .getInventoryManager()
                    .getPotionCount(type);

            g2d.setColor(getPotionColor(type));

            g2d.fillOval(1080, startY + i * 45 - 15, 25, 25);

            g2d.setColor(TEXT_COLOR);

            g2d.drawString(
                    getPotionName(type) + ": " + count,
                    1115,
                    startY + i * 45 + 5
            );
        }
    }

    private void drawActivePotions(Graphics2D g2d) {
        int y = 600;

        for (PotionType type : PotionType.values()) {
            if (!gameSession.getPotionSystem().isActive(type)) {
                continue;
            }

            double remainingTime = gameSession
                    .getPotionSystem()
                    .getRemainingTime(type);

            g2d.setFont(new Font("SansSerif", Font.BOLD, 18));
            g2d.setColor(getPotionColor(type));

            g2d.drawString(
                    getPotionName(type) + ": " +
                            String.format("%.1f", remainingTime) + "s",
                    40,
                    y
            );

            y += 30;
        }
    }

    private Color getPotionColor(PotionType type) {
        return switch (type) {
            case FREEZE -> new Color(80, 190, 255);
            case FRENZY -> new Color(255, 90, 70);
            case DOUBLE_POINTS -> new Color(255, 210, 60);
        };
    }

    private String getPotionName(PotionType type) {
        return switch (type) {
            case FREEZE -> "FREEZE";
            case FRENZY -> "FRENZY";
            case DOUBLE_POINTS -> "DOUBLE POINTS";
        };
    }
}
