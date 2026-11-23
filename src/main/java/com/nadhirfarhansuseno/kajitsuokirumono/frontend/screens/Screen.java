
package com.nadhirfarhansuseno.kajitsuokirumono.frontend.screens;

import java.awt.Graphics2D;

public interface Screen {

    void update(double deltaTime);

    void render(Graphics2D g2d);

    default void onEnter() {
    }

    default void onExit() {
    }

    default void onMouseMoved(int mouseX, int mouseY) {
    }

    default void onMouseClicked(int mouseX, int mouseY) {
    }

    default void onEscapePressed() {
    }
}
