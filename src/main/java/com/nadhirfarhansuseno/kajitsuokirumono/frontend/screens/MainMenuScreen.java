
package com.nadhirfarhansuseno.kajitsuokirumono.frontend.screens;

import com.nadhirfarhansuseno.kajitsuokirumono.frontend.components.Button;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.util.ArrayList;
import java.util.List;

public class MainMenuScreen implements Screen {

    private static final Color BACKGROUND_COLOR = new Color(25, 35, 55);
    private static final Color TITLE_COLOR = new Color(255, 190, 75);
    private static final Color TEXT_COLOR = Color.WHITE;

    private final ScreenManager screenManager;
    private final List<Button> buttons = new ArrayList<>();

    public MainMenuScreen(ScreenManager screenManager) {
        this.screenManager = screenManager;
        createButtons();
    }

    private void createButtons() {
        int buttonWidth = 280;
        int buttonHeight = 50;
        int centerX = (1280 - buttonWidth) / 2;

        buttons.add(new Button(
                centerX, 390, buttonWidth, buttonHeight,
                "PLAY",
                () -> screenManager.setScreen(
                        new GameScreen(
                                screenManager,
                                screenManager.getKeyboardInput()
                        )
                )
        ));

        buttons.add(new Button(
                centerX, 450, buttonWidth, buttonHeight,
                "SHOP",
                () -> screenManager.setScreen(new ShopScreen(screenManager))
        ));

        buttons.add(new Button(
                centerX, 510, buttonWidth, buttonHeight,
                "HIGH SCORE",
                () -> screenManager.setScreen(new HighScoreScreen(screenManager))
        ));

        buttons.add(new Button(
                centerX, 570, buttonWidth, buttonHeight,
                "HOW TO PLAY",
                () -> screenManager.setScreen(new HowToPlayScreen(screenManager))
        ));

        buttons.add(new Button(
                centerX, 630, buttonWidth, buttonHeight,
                "SETTINGS",
                () -> screenManager.setScreen(new SettingsScreen(screenManager))
        ));
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
        drawSubtitle(graphics);

        for (Button button : buttons) {
            button.render(graphics);
        }

        graphics.dispose();
    }

    private void drawBackground(Graphics2D g2d) {
        g2d.setColor(BACKGROUND_COLOR);
        g2d.fillRect(0, 0, 1280, 720);
    }

    private void drawTitle(Graphics2D g2d) {
        g2d.setFont(new Font("SansSerif", Font.BOLD, 64));
        g2d.setColor(TITLE_COLOR);

        String title = "Kajitsu o Kiru Mono";

        int textWidth = g2d.getFontMetrics().stringWidth(title);

        g2d.drawString(title, (1280 - textWidth) / 2, 270);
    }

    private void drawSubtitle(Graphics2D g2d) {
        g2d.setFont(new Font("SansSerif", Font.PLAIN, 22));
        g2d.setColor(TEXT_COLOR);

        String subtitle = "The Art of Fruit Slicing";

        int textWidth = g2d.getFontMetrics().stringWidth(subtitle);

        g2d.drawString(subtitle, (1280 - textWidth) / 2, 320);
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
