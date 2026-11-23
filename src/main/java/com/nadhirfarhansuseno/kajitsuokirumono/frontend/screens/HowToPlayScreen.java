
package com.nadhirfarhansuseno.kajitsuokirumono.frontend.screens;

import com.nadhirfarhansuseno.kajitsuokirumono.frontend.components.Button;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

public class HowToPlayScreen implements Screen {

    private static final Color BACKGROUND_COLOR = new Color(25, 35, 55);
    private static final Color TITLE_COLOR = new Color(255, 190, 75);
    private static final Color TEXT_COLOR = Color.WHITE;

    private final Button backButton;

    public HowToPlayScreen(ScreenManager screenManager) {
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
        drawInstructions(graphics);

        backButton.render(graphics);

        graphics.dispose();
    }

    private void drawBackground(Graphics2D g2d) {
        g2d.setColor(BACKGROUND_COLOR);
        g2d.fillRect(0, 0, 1280, 720);
    }

    private void drawTitle(Graphics2D g2d) {
        g2d.setFont(new Font("SansSerif", Font.BOLD, 52));
        g2d.setColor(TITLE_COLOR);

        String title = "HOW TO PLAY";

        int textWidth = g2d.getFontMetrics().stringWidth(title);

        g2d.drawString(title, (1280 - textWidth) / 2, 150);
    }

    private void drawInstructions(Graphics2D g2d) {
        String[] instructions = {
                "1. Move your mouse across the screen to control your blade.",
                "2. Hold the left mouse button and drag to slice fruits.",
                "3. Slice fruits to earn points and collect coins.",
                "4. Avoid black bombs because they will cost you one life.",
                "5. Slicing a red bomb will immediately end your game.",
                "6. Use potions to gain temporary advantages.",
                "7. Upgrade your abilities in the Shop."
        };

        g2d.setFont(new Font("SansSerif", Font.PLAIN, 21));
        g2d.setColor(TEXT_COLOR);

        int startY = 270;
        int lineSpacing = 55;

        for (int i = 0; i < instructions.length; i++) {
            g2d.drawString(
                    instructions[i],
                    250,
                    startY + (i * lineSpacing)
            );
        }
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
