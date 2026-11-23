
package com.nadhirfarhansuseno.kajitsuokirumono.frontend.screens;

import com.nadhirfarhansuseno.kajitsuokirumono.frontend.components.Button;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

public class HighScoreScreen implements Screen {

    private static final Color BACKGROUND_COLOR = new Color(25, 35, 55);
    private static final Color TITLE_COLOR = new Color(255, 190, 75);
    private static final Color TEXT_COLOR = Color.WHITE;

    private final Button backButton;

    public HighScoreScreen(ScreenManager screenManager) {
        backButton = new Button(
                40, 40, 150, 50,
                "BACK",
                () -> screenManager.setScreen(new MainMenuScreen(screenManager))
        );
    }

    @Override
    public void update(double deltaTime) {
    }

    @Override
    public void render(Graphics2D g2d) {
        Graphics2D graphics = (Graphics2D) g2d.create();

        graphics.setRenderingHint(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON
        );

        drawBackground(graphics);
        drawTitle(graphics);
        drawHighScore(graphics);

        backButton.render(graphics);

        graphics.dispose();
    }

    private void drawBackground(Graphics2D g2d) {
        g2d.setColor(BACKGROUND_COLOR);
        g2d.fillRect(0, 0, 1280, 720);
    }

    private void drawTitle(Graphics2D g2d) {
        g2d.setFont(new Font("SansSerif", Font.BOLD, 56));
        g2d.setColor(TITLE_COLOR);

        String title = "HIGH SCORE";

        int textWidth = g2d.getFontMetrics().stringWidth(title);

        g2d.drawString(title, (1280 - textWidth) / 2, 180);
    }

    private void drawHighScore(Graphics2D g2d) {
        g2d.setFont(new Font("SansSerif", Font.PLAIN, 28));
        g2d.setColor(TEXT_COLOR);

        String scoreText = "BEST SCORE: 0";

        int textWidth = g2d.getFontMetrics().stringWidth(scoreText);

        g2d.drawString(scoreText, (1280 - textWidth) / 2, 350);
    }

    @Override
    public void onMouseMoved(int mouseX, int mouseY) {
        backButton.updateHover(mouseX, mouseY);
    }

    @Override
    public void onMouseClicked(int mouseX, int mouseY) {
        backButton.onClick(mouseX, mouseY);
    }
}
