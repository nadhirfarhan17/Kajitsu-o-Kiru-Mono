
package com.nadhirfarhansuseno.kajitsuokirumono.frontend.screens;

import com.nadhirfarhansuseno.kajitsuokirumono.frontend.components.Button;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.RoundRectangle2D;
import java.util.ArrayList;
import java.util.List;

public class GameOverScreen implements Screen {

    private static final Color BACKGROUND_TOP =
            new Color(15, 23, 42);

    private static final Color BACKGROUND_BOTTOM =
            new Color(30, 41, 59);

    private static final Color PANEL_COLOR =
            new Color(25, 36, 57);

    private static final Color ACCENT_RED =
            new Color(255, 91, 82);

    private static final Color ACCENT_ORANGE =
            new Color(255, 145, 77);

    private static final Color TEXT_PRIMARY =
            new Color(245, 247, 255);

    private static final Color TEXT_SECONDARY =
            new Color(155, 169, 190);

    private static final Color GOLD =
            new Color(255, 196, 77);

    private final ScreenManager screenManager;

    private final int finalScore;
    private final int coinsEarned;

    private final List<Button> buttons = new ArrayList<>();

    public GameOverScreen(
            ScreenManager screenManager,
            int finalScore,
            int coinsEarned
    ) {
        this.screenManager = screenManager;
        this.finalScore = finalScore;
        this.coinsEarned = coinsEarned;

        createButtons();
    }

    private void createButtons() {
        int buttonWidth = 280;
        int buttonHeight = 52;
        int centerX = (1280 - buttonWidth) / 2;

        buttons.add(new Button(
                centerX,
                490,
                buttonWidth,
                buttonHeight,
                "PLAY AGAIN",
                () -> screenManager.setScreen(
                        new GameScreen(
                                screenManager,
                                screenManager.getKeyboardInput()
                        )
                )
        ));

        buttons.add(new Button(
                centerX,
                555,
                buttonWidth,
                buttonHeight,
                "MAIN MENU",
                () -> screenManager.setScreen(
                        new MainMenuScreen(screenManager)
                )
        ));
    }

    @Override
    public void update(double deltaTime) {
    }

    @Override
    public void render(Graphics2D g2d) {
        Graphics2D graphics =
                (Graphics2D) g2d.create();

        graphics.setRenderingHint(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON
        );

        graphics.setRenderingHint(
                RenderingHints.KEY_TEXT_ANTIALIASING,
                RenderingHints.VALUE_TEXT_ANTIALIAS_ON
        );

        drawBackground(graphics);
        drawMainPanel(graphics);
        drawHeader(graphics);
        drawResultCards(graphics);
        drawButtons(graphics);

        graphics.dispose();
    }

    private void drawBackground(Graphics2D g2d) {
        GradientPaint gradient = new GradientPaint(
                0,
                0,
                BACKGROUND_TOP,
                0,
                720,
                BACKGROUND_BOTTOM
        );

        g2d.setPaint(gradient);
        g2d.fillRect(0, 0, 1280, 720);

        // Decorative background circles
        g2d.setColor(new Color(255, 91, 82, 12));
        g2d.fillOval(850, -180, 500, 500);

        g2d.setColor(new Color(70, 130, 220, 12));
        g2d.fillOval(-180, 430, 450, 450);

        // Subtle horizontal accent
        g2d.setColor(new Color(255, 255, 255, 15));
        g2d.fillRect(0, 0, 1280, 2);
    }

    private void drawMainPanel(Graphics2D g2d) {
        int panelX = 340;
        int panelY = 75;
        int panelWidth = 600;
        int panelHeight = 590;

        // Panel shadow
        g2d.setColor(new Color(0, 0, 0, 65));

        g2d.fillRoundRect(
                panelX + 5,
                panelY + 8,
                panelWidth,
                panelHeight,
                30,
                30
        );

        // Main panel
        g2d.setColor(PANEL_COLOR);

        g2d.fillRoundRect(
                panelX,
                panelY,
                panelWidth,
                panelHeight,
                30,
                30
        );

        // Panel border
        g2d.setColor(new Color(255, 255, 255, 25));
        g2d.setStroke(new BasicStroke(1.2f));

        g2d.drawRoundRect(
                panelX,
                panelY,
                panelWidth,
                panelHeight,
                30,
                30
        );

        // Top accent
        GradientPaint accent = new GradientPaint(
                panelX,
                panelY,
                ACCENT_RED,
                panelX + panelWidth,
                panelY,
                ACCENT_ORANGE
        );

        g2d.setPaint(accent);

        g2d.fillRoundRect(
                panelX + 1,
                panelY + 1,
                panelWidth - 2,
                5,
                5,
                5
        );
    }

    private void drawHeader(Graphics2D g2d) {
        drawCenteredText(
                g2d,
                "GAME OVER",
                new Font("SansSerif", Font.BOLD, 55),
                ACCENT_RED,
                235
        );

        drawCenteredText(
                g2d,
                "Better luck next time!",
                new Font("SansSerif", Font.PLAIN, 18),
                TEXT_SECONDARY,
                275
        );

        // Divider
        g2d.setColor(new Color(255, 255, 255, 30));
        g2d.setStroke(new BasicStroke(1));

        g2d.drawLine(
                405,
                305,
                875,
                305
        );

        drawCenteredText(
                g2d,
                "RUN SUMMARY",
                new Font("SansSerif", Font.BOLD, 13),
                TEXT_SECONDARY,
                340
        );
    }

    private void drawResultCards(Graphics2D g2d) {
        int cardY = 365;
        int cardWidth = 215;
        int cardHeight = 95;

        int scoreX = 405;
        int coinsX = 660;

        drawResultCard(
                g2d,
                scoreX,
                cardY,
                cardWidth,
                cardHeight,
                "FINAL SCORE",
                String.valueOf(finalScore),
                new Color(80, 145, 255)
        );

        drawResultCard(
                g2d,
                coinsX,
                cardY,
                cardWidth,
                cardHeight,
                "COINS EARNED",
                String.valueOf(coinsEarned),
                GOLD
        );
    }

    private void drawResultCard(
            Graphics2D g2d,
            int x,
            int y,
            int width,
            int height,
            String label,
            String value,
            Color accent
    ) {
        // Card background
        g2d.setColor(new Color(255, 255, 255, 12));

        g2d.fillRoundRect(
                x,
                y,
                width,
                height,
                18,
                18
        );

        // Card border
        g2d.setColor(new Color(255, 255, 255, 25));

        g2d.drawRoundRect(
                x,
                y,
                width,
                height,
                18,
                18
        );

        // Accent line
        g2d.setColor(accent);

        g2d.fillRoundRect(
                x + 15,
                y + 15,
                4,
                height - 30,
                4,
                4
        );

        // Label
        g2d.setFont(
                new Font("SansSerif", Font.BOLD, 12)
        );

        g2d.setColor(TEXT_SECONDARY);

        g2d.drawString(
                label,
                x + 32,
                y + 35
        );

        // Value
        g2d.setFont(
                new Font("SansSerif", Font.BOLD, 29)
        );

        g2d.setColor(accent);

        g2d.drawString(
                value,
                x + 32,
                y + 72
        );
    }

    private void drawButtons(Graphics2D g2d) {
        for (Button button : buttons) {
            button.render(g2d);
        }
    }

    private void drawCenteredText(
            Graphics2D g2d,
            String text,
            Font font,
            Color color,
            int y
    ) {
        g2d.setFont(font);
        g2d.setColor(color);

        int textWidth =
                g2d.getFontMetrics().stringWidth(text);

        g2d.drawString(
                text,
                (1280 - textWidth) / 2,
                y
        );
    }

    @Override
    public void onMouseMoved(int mouseX, int mouseY) {
        for (Button button : buttons) {
            button.updateHover(mouseX, mouseY);
        }
    }

    @Override
    public void onMouseClicked(int mouseX, int mouseY) {
        for (Button button : buttons) {
            button.onClick(mouseX, mouseY);
        }
    }
}
