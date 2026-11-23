
package com.nadhirfarhansuseno.kajitsuokirumono.frontend.input;

import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

public class MouseInput extends MouseAdapter {

    private int mouseX;
    private int mouseY;

    private boolean mousePressed;
    private boolean mouseClicked;

    private final List<Point> movementPoints =
            new ArrayList<>();

    private final List<Point> hoverMovementPoints =
            new ArrayList<>();

    private static final int MAX_HOVER_POINTS = 50;

    @Override
    public synchronized void mouseMoved(MouseEvent e) {
        updateMousePosition(e);

        if (!mousePressed) {
            addHoverPoint(e.getX(), e.getY());
        }
    }

    @Override
    public synchronized void mouseDragged(MouseEvent e) {
        updateMousePosition(e);

        if (mousePressed) {
            movementPoints.add(
                    new Point(e.getX(), e.getY())
            );
        }
    }

    @Override
    public synchronized void mousePressed(MouseEvent e) {
        updateMousePosition(e);

        if (e.getButton() == MouseEvent.BUTTON1) {
            mousePressed = true;
            mouseClicked = true;

            hoverMovementPoints.clear();

            // Initial slicing position.
            movementPoints.add(
                    new Point(mouseX, mouseY)
            );
        }
    }

    @Override
    public synchronized void mouseReleased(MouseEvent e) {
        updateMousePosition(e);

        if (e.getButton() == MouseEvent.BUTTON1) {
            mousePressed = false;
        }
    }

    public synchronized void setMousePosition(int x, int y) {
        boolean positionChanged =
                mouseX != x || mouseY != y;

        mouseX = x;
        mouseY = y;

        if (!positionChanged) {
            return;
        }

        if (mousePressed) {
            movementPoints.add(new Point(x, y));
        } else {
            addHoverPoint(x, y);
        }
    }

    private void updateMousePosition(MouseEvent e) {
        mouseX = e.getX();
        mouseY = e.getY();
    }

    private void addHoverPoint(int x, int y) {
        hoverMovementPoints.add(new Point(x, y));

        while (hoverMovementPoints.size() > MAX_HOVER_POINTS) {
            hoverMovementPoints.remove(0);
        }
    }

    public synchronized List<Point> consumeMovementPoints() {
        List<Point> points =
                new ArrayList<>(movementPoints);

        movementPoints.clear();

        return points;
    }

    public synchronized List<Point> consumeHoverMovementPoints() {
        List<Point> points =
                new ArrayList<>(hoverMovementPoints);

        hoverMovementPoints.clear();

        return points;
    }

    public synchronized int getMouseX() {
        return mouseX;
    }

    public synchronized int getMouseY() {
        return mouseY;
    }

    public synchronized boolean isMousePressed() {
        return mousePressed;
    }

    public synchronized boolean isMouseClicked() {
        return mouseClicked;
    }

    public synchronized void resetMouseClicked() {
        mouseClicked = false;
    }
}
