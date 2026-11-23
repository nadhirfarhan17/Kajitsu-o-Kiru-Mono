
package com.nadhirfarhansuseno.kajitsuokirumono.frontend.window;

import com.nadhirfarhansuseno.kajitsuokirumono.frontend.input.KeyboardInput;
import com.nadhirfarhansuseno.kajitsuokirumono.frontend.input.MouseInput;
import com.nadhirfarhansuseno.kajitsuokirumono.frontend.screens.ScreenManager;
import com.nadhirfarhansuseno.kajitsuokirumono.frontend.utils.CoordinateScaler;
import com.nadhirfarhansuseno.kajitsuokirumono.shared.models.GameConfig;

import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

public class GamePanel extends JPanel {

    private final ScreenManager screenManager;
    private final MouseInput mouseInput;
    private final KeyboardInput keyboardInput;
    private final CoordinateScaler coordinateScaler;

    public GamePanel(ScreenManager screenManager) {
        this.screenManager = screenManager;
        this.mouseInput = new MouseInput();
        this.keyboardInput = new KeyboardInput();
        this.coordinateScaler = new CoordinateScaler();

        this.screenManager.setKeyboardInput(keyboardInput);
        this.screenManager.setMouseInput(mouseInput);

        initializePanel();
        registerMouseEvents();
        registerKeyboardEvents();
    }

    private void initializePanel() {
        setPreferredSize(
                new Dimension(
                        GameConfig.VIRTUAL_WIDTH,
                        GameConfig.VIRTUAL_HEIGHT
                )
        );

        setBackground(Color.BLACK);

        setFocusable(true);
        setDoubleBuffered(true);
    }

    private void updateCoordinates() {
        coordinateScaler.update(
                getWidth(),
                getHeight()
        );
    }

    private int toVirtualX(int x) {
        return coordinateScaler.toVirtualX(x);
    }

    private int toVirtualY(int y) {
        return coordinateScaler.toVirtualY(y);
    }

    private void updateMousePosition(MouseEvent e) {
        updateCoordinates();

        mouseInput.setMousePosition(
                toVirtualX(e.getX()),
                toVirtualY(e.getY())
        );
    }

    private void registerMouseEvents() {
        addMouseListener(new MouseListener() {

            @Override
            public void mouseClicked(MouseEvent e) {
                updateMousePosition(e);

                screenManager.onMouseClicked(
                        toVirtualX(e.getX()),
                        toVirtualY(e.getY())
                );
            }

            @Override
            public void mousePressed(MouseEvent e) {
                mouseInput.mousePressed(e);
                updateMousePosition(e);
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                mouseInput.mouseReleased(e);
                updateMousePosition(e);
            }

            @Override
            public void mouseEntered(MouseEvent e) {
            }

            @Override
            public void mouseExited(MouseEvent e) {
            }
        });

        addMouseMotionListener(new MouseMotionListener() {

            @Override
            public void mouseMoved(MouseEvent e) {
                mouseInput.mouseMoved(e);
                updateMousePosition(e);

                screenManager.onMouseMoved(
                        toVirtualX(e.getX()),
                        toVirtualY(e.getY())
                );

                repaint();
            }

            @Override
            public void mouseDragged(MouseEvent e) {
                mouseInput.mouseDragged(e);
                updateMousePosition(e);
            }
        });
    }

    private void registerKeyboardEvents() {
        addKeyListener(keyboardInput);
    }

    @Override
    protected void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);

        updateCoordinates();

        Graphics2D g2d = (Graphics2D) graphics.create();

        g2d.setColor(Color.BLACK);
        g2d.fillRect(0, 0, getWidth(), getHeight());

        g2d.setRenderingHint(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON
        );

        coordinateScaler.apply(g2d);

        screenManager.render(g2d);

        g2d.dispose();
    }

    public MouseInput getMouseInput() {
        return mouseInput;
    }

    public KeyboardInput getKeyboardInput() {
        return keyboardInput;
    }

    public CoordinateScaler getCoordinateScaler() {
        return coordinateScaler;
    }
}
