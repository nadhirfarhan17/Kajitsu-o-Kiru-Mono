
package com.nadhirfarhansuseno.kajitsuokirumono.core.engine;

import com.nadhirfarhansuseno.kajitsuokirumono.frontend.screens.ScreenManager;
import com.nadhirfarhansuseno.kajitsuokirumono.frontend.window.GamePanel;
import com.nadhirfarhansuseno.kajitsuokirumono.shared.models.GameConfig;

import javax.swing.SwingUtilities;

public class GameEngine implements Runnable {

    private final GamePanel gamePanel;
    private final ScreenManager screenManager;

    private Thread gameThread;
    private volatile boolean running;

    public GameEngine(GamePanel gamePanel, ScreenManager screenManager) {
        this.gamePanel = gamePanel;
        this.screenManager = screenManager;
    }

    public void start() {
        if (running) {
            return;
        }

        running = true;
        gameThread = new Thread(this, "Game-Thread");
        gameThread.start();
    }

    public void stop() {
        running = false;

        if (gameThread != null) {
            gameThread.interrupt();
        }
    }

    @Override
    public void run() {
        final long frameTime = GameConfig.FRAME_TIME_NS;

        long previousTime = System.nanoTime();
        long accumulator = 0;

        while (running) {
            long currentTime = System.nanoTime();
            long elapsedTime = currentTime - previousTime;

            previousTime = currentTime;
            accumulator += elapsedTime;

            while (accumulator >= frameTime) {
                update(frameTime / 1_000_000_000.0);
                accumulator -= frameTime;
            }

            render();

            long sleepTime = frameTime - (System.nanoTime() - currentTime);

            if (sleepTime > 0) {
                try {
                    long sleepMillis = sleepTime / 1_000_000;
                    int sleepNanos = (int) (sleepTime % 1_000_000);

                    Thread.sleep(sleepMillis, sleepNanos);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        }

        running = false;
    }

    private void update(double deltaTime) {
        if (gamePanel.getKeyboardInput().isEscapePressed()) {
            screenManager.onEscapePressed();
            gamePanel.getKeyboardInput().resetEscapePressed();
        }

        screenManager.update(deltaTime);
    }

    private void render() {
        SwingUtilities.invokeLater(gamePanel::repaint);
    }

    public boolean isRunning() {
        return running;
    }
}
