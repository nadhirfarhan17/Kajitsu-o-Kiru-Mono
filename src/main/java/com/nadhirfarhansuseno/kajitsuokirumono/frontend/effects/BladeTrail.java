
package com.nadhirfarhansuseno.kajitsuokirumono.frontend.effects;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class BladeTrail {

    private static final int MAX_POINTS = 15;
    private static final double POINT_LIFETIME = 0.18;

    private static final int MAX_HOVER_POINTS = 25;
    private static final double HOVER_LIFETIME = 0.22;

    private static final Color TRAIL_COLOR =
            new Color(130, 220, 255);

    private static final Color HOVER_COLOR =
            new Color(100, 190, 255);

    private final List<TrailPoint> points;
    private final List<TrailPoint> hoverPoints;

    public BladeTrail() {
        points = new ArrayList<>();
        hoverPoints = new ArrayList<>();
    }

    public void addPoint(double x, double y) {
        points.add(
                new TrailPoint(
                        x,
                        y,
                        POINT_LIFETIME
                )
        );

        while (points.size() > MAX_POINTS) {
            points.remove(0);
        }
    }

    public void addHoverPoint(double x, double y) {
        hoverPoints.add(
                new TrailPoint(
                        x,
                        y,
                        HOVER_LIFETIME
                )
        );

        while (hoverPoints.size() > MAX_HOVER_POINTS) {
            hoverPoints.remove(0);
        }
    }

    public void update(double deltaTime) {
        updatePoints(points, deltaTime);
        updatePoints(hoverPoints, deltaTime);
    }

    private void updatePoints(
            List<TrailPoint> target,
            double deltaTime
    ) {
        Iterator<TrailPoint> iterator = target.iterator();

        while (iterator.hasNext()) {
            TrailPoint point = iterator.next();

            point.lifetime -= deltaTime;

            if (point.lifetime <= 0) {
                iterator.remove();
            }
        }
    }

    public void render(Graphics2D g2d) {
        renderTrail(
                g2d,
                points,
                POINT_LIFETIME,
                TRAIL_COLOR,
                16f,
                8f,
                3f,
                65,
                130,
                240,
                true
        );
    }

    public void renderHover(Graphics2D g2d) {
        renderTrail(
                g2d,
                hoverPoints,
                HOVER_LIFETIME,
                HOVER_COLOR,
                4f,
                0f,
                1.5f,
                45,
                0,
                100,
                false
        );
    }

    private void renderTrail(
            Graphics2D g2d,
            List<TrailPoint> target,
            double lifetime,
            Color color,
            float outerWidth,
            float innerWidth,
            float coreWidth,
            int outerAlpha,
            int innerAlpha,
            int coreAlpha,
            boolean glowing
    ) {
        if (target.size() < 2) {
            return;
        }

        Graphics2D graphics =
                (Graphics2D) g2d.create();

        graphics.setRenderingHint(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON
        );

        for (int i = 1; i < target.size(); i++) {
            TrailPoint previous = target.get(i - 1);
            TrailPoint current = target.get(i);

            float fade = (float) Math.max(
                    0,
                    Math.min(
                            1,
                            current.lifetime / lifetime
                    )
            );

            int x1 = (int) previous.x;
            int y1 = (int) previous.y;

            int x2 = (int) current.x;
            int y2 = (int) current.y;

            if (glowing) {
                graphics.setColor(
                        createAlphaColor(color, outerAlpha * fade)
                );

                graphics.setStroke(
                        new BasicStroke(
                                outerWidth,
                                BasicStroke.CAP_ROUND,
                                BasicStroke.JOIN_ROUND
                        )
                );

                graphics.drawLine(x1, y1, x2, y2);

                graphics.setColor(
                        createAlphaColor(color, innerAlpha * fade)
                );

                graphics.setStroke(
                        new BasicStroke(
                                innerWidth,
                                BasicStroke.CAP_ROUND,
                                BasicStroke.JOIN_ROUND
                        )
                );

                graphics.drawLine(x1, y1, x2, y2);

                graphics.setColor(
                        new Color(
                                245,
                                252,
                                255,
                                Math.max(0, Math.min(255, (int) (coreAlpha * fade)))
                        )
                );

                graphics.setStroke(
                        new BasicStroke(
                                coreWidth,
                                BasicStroke.CAP_ROUND,
                                BasicStroke.JOIN_ROUND
                        )
                );

                graphics.drawLine(x1, y1, x2, y2);

            } else {
                graphics.setColor(
                        createAlphaColor(color, coreAlpha * fade)
                );

                graphics.setStroke(
                        new BasicStroke(
                                coreWidth,
                                BasicStroke.CAP_ROUND,
                                BasicStroke.JOIN_ROUND
                        )
                );

                graphics.drawLine(x1, y1, x2, y2);
            }
        }

        graphics.dispose();
    }

    private Color createAlphaColor(
            Color color,
            float alpha
    ) {
        int safeAlpha = Math.max(
                0,
                Math.min(255, (int) alpha)
        );

        return new Color(
                color.getRed(),
                color.getGreen(),
                color.getBlue(),
                safeAlpha
        );
    }

    public void clear() {
        points.clear();
    }

    public void clearHover() {
        hoverPoints.clear();
    }

    public void clearAll() {
        points.clear();
        hoverPoints.clear();
    }

    private static class TrailPoint {

        private final double x;
        private final double y;

        private double lifetime;

        public TrailPoint(
                double x,
                double y,
                double lifetime
        ) {
            this.x = x;
            this.y = y;
            this.lifetime = lifetime;
        }
    }
}
