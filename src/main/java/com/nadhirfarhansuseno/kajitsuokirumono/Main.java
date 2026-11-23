
package com.nadhirfarhansuseno.kajitsuokirumono;

import com.nadhirfarhansuseno.kajitsuokirumono.core.engine.GameEngine;
import com.nadhirfarhansuseno.kajitsuokirumono.frontend.screens.MainMenuScreen;
import com.nadhirfarhansuseno.kajitsuokirumono.frontend.screens.ScreenManager;
import com.nadhirfarhansuseno.kajitsuokirumono.frontend.window.GameFrame;
import com.nadhirfarhansuseno.kajitsuokirumono.frontend.window.GamePanel;

import javax.swing.SwingUtilities;

public class Main {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Main::initializeGame);
    }

    private static void initializeGame() {
        ScreenManager screenManager = new ScreenManager();

        screenManager.setScreen(new MainMenuScreen(screenManager));

        GamePanel gamePanel = new GamePanel(screenManager);
        GameFrame gameFrame = new GameFrame();

        gameFrame.setContentPane(gamePanel);
        gameFrame.pack();
        gameFrame.setLocationRelativeTo(null);
        gameFrame.setVisible(true);

        GameEngine gameEngine = new GameEngine(gamePanel, screenManager);
        gameEngine.start();
    }
}
