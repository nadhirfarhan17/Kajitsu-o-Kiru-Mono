
package com.nadhirfarhansuseno.kajitsuokirumono.frontend.screens;

import com.nadhirfarhansuseno.kajitsuokirumono.frontend.components.Button;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.util.ArrayList;
import java.util.List;

public class PauseScreen implements Screen {

    private static final Color BACKGROUND_COLOR =
            new Color(15, 20, 35, 210);

    private static final Color TITLE_COLOR =
            new Color(255, 190, 75);

    private static final Color TEXT_COLOR = Color.WHITE;

    private final ScreenManager screenManager;
    private final GameScreen gameScreen;

    private final List<Button> buttons = new ArrayList<>();

    public PauseScreen(
            ScreenManager screenManager,
            GameScreen gameScreen
    ) {
        this.screenManager = screenManager;
        this.gameScreen = gameScreen;

        createButtons();
    }

    private void createButtons() {
        int buttonWidth = 280;
        int buttonHeight = 50;
        int centerX = (1280 - buttonWidth) / 2;

        buttons.add(new Button(
                centerX, 350, buttonWidth, buttonHeight,
                "RESUME",
                () -> screenManager.setScreen(gameScreen)
        ));

        buttons.add(new Button(
                centerX, 410, buttonWidth, buttonHeight,
                "RESTART",
                () -> {
                    screenManager.getCoinManager()
                            .resetCurrentCoins();

                    screenManager.setScreen(
                            new GameScreen(
                                    screenManager,
                                    screenManager.getKeyboardInput()
                            )
                    );
                }
        ));

        buttons.add(new Button(
                centerX, 470, buttonWidth, buttonHeight,
                "MAIN MENU",
                () -> {
                    screenManager.getCoinManager()
                            .resetCurrentCoins();

                    screenManager.setScreen(
                            new MainMenuScreen(screenManager)
                    );
                }
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

        graphics.setColor(BACKGROUND_COLOR);
        graphics.fillRect(0, 0, 1280, 720);

        drawTitle(graphics);

        for (Button button : buttons) {
            button.render(graphics);
        }

        graphics.dispose();
    }

    private void drawTitle(Graphics2D g2d) {
        g2d.setFont(
                new Font("SansSerif", Font.BOLD, 56)
        );

        g2d.setColor(TITLE_COLOR);

        String title = "PAUSED";

        int textWidth =
                g2d.getFontMetrics().stringWidth(title);

        g2d.drawString(
                title,
                (1280 - textWidth) / 2,
                270
        );

        g2d.setFont(
                new Font("SansSerif", Font.PLAIN, 20)
        );

        g2d.setColor(TEXT_COLOR);

        String subtitle = "Game is currently paused";

        textWidth =
                g2d.getFontMetrics().stringWidth(subtitle);

        g2d.drawString(
                subtitle,
                (1280 - textWidth) / 2,
                310
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
