
package com.nadhirfarhansuseno.kajitsuokirumono.frontend.input;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class KeyboardInput extends KeyAdapter {

    private boolean escapePressed;

    private boolean freezePressed;
    private boolean frenzyPressed;
    private boolean doublePointsPressed;

    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_ESCAPE -> escapePressed = true;
            case KeyEvent.VK_F -> freezePressed = true;
            case KeyEvent.VK_G -> frenzyPressed = true;
            case KeyEvent.VK_H -> doublePointsPressed = true;
        }
    }

    public boolean isEscapePressed() {
        return escapePressed;
    }

    public boolean isFreezePressed() {
        return freezePressed;
    }

    public boolean isFrenzyPressed() {
        return frenzyPressed;
    }

    public boolean isDoublePointsPressed() {
        return doublePointsPressed;
    }

    public void resetEscapePressed() {
        escapePressed = false;
    }

    public void resetFreezePressed() {
        freezePressed = false;
    }

    public void resetFrenzyPressed() {
        frenzyPressed = false;
    }

    public void resetDoublePointsPressed() {
        doublePointsPressed = false;
    }
}
