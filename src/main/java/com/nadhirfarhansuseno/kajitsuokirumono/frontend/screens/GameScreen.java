
package com.nadhirfarhansuseno.kajitsuokirumono.frontend.screens;

import com.nadhirfarhansuseno.kajitsuokirumono.core.engine.GameSession;
import com.nadhirfarhansuseno.kajitsuokirumono.core.entities.Bomb;
import com.nadhirfarhansuseno.kajitsuokirumono.core.entities.Fruit;
import com.nadhirfarhansuseno.kajitsuokirumono.core.entities.NormalFruit;
import com.nadhirfarhansuseno.kajitsuokirumono.core.entities.PotionItem;
import com.nadhirfarhansuseno.kajitsuokirumono.core.entities.ToughFruit;
import com.nadhirfarhansuseno.kajitsuokirumono.frontend.effects.BladeTrail;
import com.nadhirfarhansuseno.kajitsuokirumono.frontend.effects.ExplosionEffectRenderer;
import com.nadhirfarhansuseno.kajitsuokirumono.frontend.effects.FloatingRewardRenderer;
import com.nadhirfarhansuseno.kajitsuokirumono.frontend.effects.FruitSliceRenderer;
import com.nadhirfarhansuseno.kajitsuokirumono.frontend.effects.ParticleSystem;
import com.nadhirfarhansuseno.kajitsuokirumono.frontend.effects.PotionEffectRenderer;
import com.nadhirfarhansuseno.kajitsuokirumono.frontend.hud.GameHUD;
import com.nadhirfarhansuseno.kajitsuokirumono.frontend.input.KeyboardInput;
import com.nadhirfarhansuseno.kajitsuokirumono.frontend.input.MouseInput;
import com.nadhirfarhansuseno.kajitsuokirumono.frontend.input.PotionInputHandler;
import com.nadhirfarhansuseno.kajitsuokirumono.shared.models.FruitSliceEvent;
import com.nadhirfarhansuseno.kajitsuokirumono.shared.models.RewardEvent;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.util.ArrayList;
import java.util.List;

public class GameScreen implements Screen {

    private static final Color BACKGROUND_COLOR =
            new Color(25, 35, 55);

    private final ScreenManager screenManager;
    private final GameSession gameSession;
    private final PotionInputHandler potionInputHandler;
    private final GameHUD gameHUD;
    private final MouseInput mouseInput;

    private final PotionEffectRenderer potionEffectRenderer;
    private final FloatingRewardRenderer floatingRewardRenderer;
    private final ExplosionEffectRenderer explosionEffectRenderer;
    private final ParticleSystem particleSystem;
    private final FruitSliceRenderer fruitSliceRenderer;

    private final BladeTrail bladeTrail;

    private boolean gameOverTransitioned;
    private boolean wasMousePressed;

    public GameScreen(
            ScreenManager screenManager,
            KeyboardInput keyboardInput
    ) {
        this.screenManager = screenManager;
        this.mouseInput = screenManager.getMouseInput();

        this.gameSession = new GameSession(
                screenManager.getPlayerStats(),
                screenManager.getCoinManager(),
                screenManager.getInventoryManager()
        );

        this.potionInputHandler = new PotionInputHandler(
                keyboardInput,
                gameSession.getInventoryManager(),
                gameSession.getPotionSystem()
        );

        this.gameHUD = new GameHUD(gameSession);

        this.particleSystem = new ParticleSystem();
        this.potionEffectRenderer = new PotionEffectRenderer();
        this.floatingRewardRenderer = new FloatingRewardRenderer();
        this.explosionEffectRenderer = new ExplosionEffectRenderer();
        this.fruitSliceRenderer = new FruitSliceRenderer();

        this.bladeTrail = new BladeTrail();

        this.gameOverTransitioned = false;
        this.wasMousePressed = false;
    }

    @Override
    public void update(double deltaTime) {
        if (gameOverTransitioned) {
            return;
        }

        gameSession.update(deltaTime);

        potionInputHandler.update();

        List<Point> movementPoints =
                mouseInput.consumeMovementPoints();

        List<Point> hoverMovementPoints =
                mouseInput.consumeHoverMovementPoints();

        boolean mousePressed = mouseInput.isMousePressed();

        gameSession.processSlicingPoints(
                movementPoints,
                mousePressed
        );

        // Consume fruit slice events.
        collectFruitSliceEvents();

        updateBladeTrail(
                deltaTime,
                movementPoints,
                hoverMovementPoints,
                mousePressed
        );

        collectRewardEvents();
        createFruitSliceEffects();

        particleSystem.update(deltaTime);
        fruitSliceRenderer.update(deltaTime);
        potionEffectRenderer.update(deltaTime);
        floatingRewardRenderer.update(deltaTime);
        explosionEffectRenderer.update(deltaTime);

        checkGameOverTransition();
    }

    private void collectFruitSliceEvents() {
        List<FruitSliceEvent> events =
                gameSession.consumeFruitSliceEvents();

        for (FruitSliceEvent event : events) {
            fruitSliceRenderer.addSlice(event);
        }
    }

    private void updateBladeTrail(
            double deltaTime,
            List<Point> movementPoints,
            List<Point> hoverMovementPoints,
            boolean mousePressed
    ) {
        bladeTrail.update(deltaTime);

        if (mousePressed) {

            // Remove hover effect when slicing starts.
            bladeTrail.clearHover();

            // Start a fresh slicing trail.
            if (!wasMousePressed) {
                bladeTrail.clear();
                wasMousePressed = true;
            }

            for (Point point : movementPoints) {
                if (point != null) {
                    bladeTrail.addPoint(
                            point.x,
                            point.y
                    );
                }
            }

        } else {

            wasMousePressed = false;

            // Add points for the normal hover trail.
            for (Point point : hoverMovementPoints) {
                if (point != null) {
                    bladeTrail.addHoverPoint(
                            point.x,
                            point.y
                    );
                }
            }
        }
    }

    private void checkGameOverTransition() {
        if (!gameSession.isGameOver() || gameOverTransitioned) {
            return;
        }

        gameOverTransitioned = true;

        int finalScore =
                gameSession.getScoreManager().getScore();

        int coinsEarned =
                gameSession.getCoinsEarned();

        screenManager.setScreen(
                new GameOverScreen(
                        screenManager,
                        finalScore,
                        coinsEarned
                )
        );
    }

    private void collectRewardEvents() {
        List<RewardEvent> events =
                gameSession.consumeRewardEvents();

        for (RewardEvent event : events) {
            floatingRewardRenderer.addReward(event);
        }
    }

    private void createFruitSliceEffects() {
        List<Fruit> fruits = gameSession.getFruits();
        List<Fruit> slicedFruits = new ArrayList<>();

        for (Fruit fruit : fruits) {
            if (!fruit.isSliced()) {
                continue;
            }

            if (fruit instanceof NormalFruit normalFruit) {
                particleSystem.createEffect(
                        fruit.getX(),
                        fruit.getY(),
                        normalFruit.getColor(),
                        15
                );

            } else if (fruit instanceof ToughFruit) {
                particleSystem.createEffect(
                        fruit.getX(),
                        fruit.getY(),
                        new Color(120, 100, 80),
                        20
                );

            } else if (fruit instanceof PotionItem potionItem) {
                particleSystem.createEffect(
                        fruit.getX(),
                        fruit.getY(),
                        potionItem.getColor(),
                        20
                );

            } else if (fruit instanceof Bomb bomb) {
                Color bombColor = bomb.isInstantGameOver()
                        ? Color.RED
                        : Color.BLACK;

                particleSystem.createEffect(
                        fruit.getX(),
                        fruit.getY(),
                        bombColor,
                        25
                );

                if (bomb.isInstantGameOver()) {
                    explosionEffectRenderer.addRedBombExplosion(
                            fruit.getX(),
                            fruit.getY()
                    );
                } else {
                    explosionEffectRenderer.addBlackBombExplosion(
                            fruit.getX(),
                            fruit.getY()
                    );
                }
            }

            slicedFruits.add(fruit);
        }

        fruits.removeAll(slicedFruits);
    }

    @Override
    public void render(Graphics2D g2d) {
        Graphics2D graphics =
                (Graphics2D) g2d.create();

        graphics.setRenderingHint(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON
        );

        graphics.setColor(BACKGROUND_COLOR);
        graphics.fillRect(0, 0, 1280, 720);

        renderFruits(graphics);

        // Render split fruit animation.
        fruitSliceRenderer.render(graphics);

        // Subtle cursor hover trail.
        bladeTrail.renderHover(graphics);

        // Brighter slicing trail.
        bladeTrail.render(graphics);

        particleSystem.render(graphics);
        explosionEffectRenderer.render(graphics);

        gameHUD.render(graphics);

        potionEffectRenderer.render(
                graphics,
                1280,
                720,
                gameSession.getPotionSystem()
        );

        floatingRewardRenderer.render(graphics);

        graphics.dispose();
    }

    private void renderFruits(Graphics2D g2d) {
        for (Fruit fruit : gameSession.getFruits()) {
            if (fruit.isSliced()) {
                continue;
            }

            if (fruit instanceof NormalFruit normalFruit) {
                drawNormalFruit(g2d, normalFruit);

            } else if (fruit instanceof ToughFruit toughFruit) {
                drawToughFruit(g2d, toughFruit);

            } else if (fruit instanceof PotionItem potionItem) {
                drawPotion(g2d, potionItem);

            } else if (fruit instanceof Bomb bomb) {
                drawBomb(g2d, bomb);
            }
        }
    }

    private void drawNormalFruit(
            Graphics2D g2d,
            NormalFruit fruit
    ) {
        int radius = fruit.getRadius();

        int x = (int) fruit.getX() - radius;
        int y = (int) fruit.getY() - radius;

        g2d.setColor(fruit.getColor());
        g2d.fillOval(x, y, radius * 2, radius * 2);

        g2d.setColor(new Color(255, 255, 255, 100));
        g2d.fillOval(
                x + radius / 3,
                y + radius / 4,
                radius / 2,
                radius / 3
        );

        g2d.setColor(new Color(40, 40, 40));
        g2d.drawOval(x, y, radius * 2, radius * 2);
    }

    private void drawToughFruit(
            Graphics2D g2d,
            ToughFruit fruit
    ) {
        int radius = fruit.getRadius();

        int x = (int) fruit.getX() - radius;
        int y = (int) fruit.getY() - radius;

        g2d.setColor(new Color(120, 100, 80));
        g2d.fillOval(
                x,
                y,
                radius * 2,
                radius * 2
        );

        g2d.setColor(new Color(160, 140, 110));
        g2d.fillOval(
                x + radius / 3,
                y + radius / 4,
                radius / 2,
                radius / 3
        );

        g2d.setColor(new Color(70, 55, 40));
        g2d.drawOval(
                x,
                y,
                radius * 2,
                radius * 2
        );

        g2d.setColor(Color.WHITE);
        g2d.drawString(
                "TOUGH",
                (int) fruit.getX() - 20,
                (int) fruit.getY() + 5
        );
    }

    private void drawPotion(
            Graphics2D g2d,
            PotionItem potion
    ) {
        int centerX = (int) potion.getX();
        int centerY = (int) potion.getY();

        int radius = potion.getRadius();

        int bottleWidth = radius;
        int bottleHeight = radius * 2;

        int bottleX = centerX - bottleWidth / 2;
        int bottleY = centerY - bottleHeight / 2;

        Color potionColor = potion.getColor();

        // Bottle neck
        g2d.setColor(new Color(210, 220, 230));
        g2d.fillRoundRect(
                centerX - 7,
                bottleY - 9,
                14,
                13,
                4,
                4
        );

        // Bottle cap
        g2d.setColor(new Color(70, 75, 90));
        g2d.fillRoundRect(
                centerX - 9,
                bottleY - 13,
                18,
                7,
                3,
                3
        );

        // Bottle body
        g2d.setColor(new Color(220, 230, 245));
        g2d.fillRoundRect(
                bottleX,
                bottleY,
                bottleWidth,
                bottleHeight,
                12,
                12
        );

        // Potion liquid
        g2d.setColor(potionColor);
        g2d.fillRoundRect(
                bottleX + 4,
                centerY,
                bottleWidth - 8,
                bottleHeight / 2 - 4,
                8,
                8
        );

        // Bottle outline
        g2d.setColor(Color.WHITE);
        g2d.drawRoundRect(
                bottleX,
                bottleY,
                bottleWidth,
                bottleHeight,
                12,
                12
        );

        // Shine
        g2d.setColor(new Color(255, 255, 255, 180));
        g2d.fillRoundRect(
                bottleX + 6,
                bottleY + 9,
                5,
                12,
                4,
                4
        );

        // Potion symbol
        g2d.setColor(Color.WHITE);
        g2d.drawString(
                "P",
                centerX - 4,
                centerY + 8
        );
    }

    private void drawBomb(
            Graphics2D g2d,
            Bomb bomb
    ) {
        int radius = bomb.getRadius();

        int x = (int) bomb.getX() - radius;
        int y = (int) bomb.getY() - radius;

        Color bombColor = bomb.isInstantGameOver()
                ? new Color(220, 45, 45)
                : new Color(30, 30, 30);

        g2d.setColor(bombColor);
        g2d.fillOval(
                x,
                y,
                radius * 2,
                radius * 2
        );

        g2d.setColor(new Color(150, 150, 150));
        g2d.fillOval(
                x + radius / 3,
                y + radius / 4,
                radius / 2,
                radius / 3
        );

        g2d.setColor(Color.WHITE);
        g2d.drawOval(
                x,
                y,
                radius * 2,
                radius * 2
        );

        // Fuse
        g2d.setColor(new Color(180, 150, 100));
        g2d.drawLine(
                (int) bomb.getX(),
                y,
                (int) bomb.getX() + 8,
                y - 12
        );

        g2d.setColor(Color.ORANGE);
        g2d.fillOval(
                (int) bomb.getX() + 5,
                y - 16,
                8,
                8
        );
    }

    @Override
    public void onEscapePressed() {
        if (gameSession.isGameOver()) {
            return;
        }

        screenManager.setScreen(
                new PauseScreen(screenManager, this)
        );
    }

    public GameSession getGameSession() {
        return gameSession;
    }
}
