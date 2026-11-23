package com.nadhirfarhansuseno.kajitsuokirumono.frontend.screens;

import com.nadhirfarhansuseno.kajitsuokirumono.core.managers.CoinManager;
import com.nadhirfarhansuseno.kajitsuokirumono.core.managers.InventoryManager;
import com.nadhirfarhansuseno.kajitsuokirumono.frontend.input.KeyboardInput;
import com.nadhirfarhansuseno.kajitsuokirumono.frontend.input.MouseInput;
import com.nadhirfarhansuseno.kajitsuokirumono.shared.models.PlayerStats;

import java.awt.Graphics2D;

public class ScreenManager {

    private Screen currentScreen;

    private KeyboardInput keyboardInput;
    private MouseInput mouseInput;

    private final PlayerStats playerStats;
    private final CoinManager coinManager;
    private final InventoryManager inventoryManager;

    public ScreenManager() {
        this.playerStats = new PlayerStats();
        this.coinManager = new CoinManager();
        this.inventoryManager = new InventoryManager();
    }

    public PlayerStats getPlayerStats() {
        return playerStats;
    }

    public CoinManager getCoinManager() {
        return coinManager;
    }

    public InventoryManager getInventoryManager() {
        return inventoryManager;
    }

    public void setKeyboardInput(KeyboardInput keyboardInput) {
        this.keyboardInput = keyboardInput;
    }

    public KeyboardInput getKeyboardInput() {
        return keyboardInput;
    }

    public void setMouseInput(MouseInput mouseInput) {
        this.mouseInput = mouseInput;
    }

    public MouseInput getMouseInput() {
        return mouseInput;
    }

    public void setScreen(Screen newScreen) {
        if (currentScreen != null) {
            currentScreen.onExit();
        }

        currentScreen = newScreen;

        if (currentScreen != null) {
            currentScreen.onEnter();
        }
    }

    public void update(double deltaTime) {
        if (currentScreen != null) {
            currentScreen.update(deltaTime);
        }
    }

    public void render(Graphics2D g2d) {
        if (currentScreen != null) {
            currentScreen.render(g2d);
        }
    }

    public void onMouseMoved(int mouseX, int mouseY) {
        if (currentScreen != null) {
            currentScreen.onMouseMoved(mouseX, mouseY);
        }
    }

    public void onMouseClicked(int mouseX, int mouseY) {
        if (currentScreen != null) {
            currentScreen.onMouseClicked(mouseX, mouseY);
        }
    }

    public void onEscapePressed() {
        if (currentScreen != null) {
            currentScreen.onEscapePressed();
        }
    }

    public Screen getCurrentScreen() {
        return currentScreen;
    }
}