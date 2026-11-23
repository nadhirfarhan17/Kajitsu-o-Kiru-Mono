
package com.nadhirfarhansuseno.kajitsuokirumono.frontend.window;

import com.nadhirfarhansuseno.kajitsuokirumono.shared.models.GameConfig;

import javax.swing.JFrame;
import javax.swing.WindowConstants;
import java.awt.Dimension;
import java.awt.Toolkit;

public class GameFrame extends JFrame {

    public GameFrame() {
        initializeWindow();
    }

    private void initializeWindow() {
        setTitle(GameConfig.WINDOW_TITLE);

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        setSize(
                GameConfig.VIRTUAL_WIDTH,
                GameConfig.VIRTUAL_HEIGHT
        );

        setMinimumSize(
                new Dimension(
                        GameConfig.MIN_WIDTH,
                        GameConfig.MIN_HEIGHT
                )
        );

        setResizable(GameConfig.RESIZABLE);

        setLocationRelativeTo(null);
    }
}
