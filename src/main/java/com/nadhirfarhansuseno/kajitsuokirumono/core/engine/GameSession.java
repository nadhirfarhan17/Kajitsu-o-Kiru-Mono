
package com.nadhirfarhansuseno.kajitsuokirumono.core.engine;

import com.nadhirfarhansuseno.kajitsuokirumono.core.entities.Blade;
import com.nadhirfarhansuseno.kajitsuokirumono.core.entities.Bomb;
import com.nadhirfarhansuseno.kajitsuokirumono.core.entities.Fruit;
import com.nadhirfarhansuseno.kajitsuokirumono.core.entities.NormalFruit;
import com.nadhirfarhansuseno.kajitsuokirumono.core.entities.PotionItem;
import com.nadhirfarhansuseno.kajitsuokirumono.core.entities.ToughFruit;
import com.nadhirfarhansuseno.kajitsuokirumono.core.managers.BladeManager;
import com.nadhirfarhansuseno.kajitsuokirumono.core.managers.CoinManager;
import com.nadhirfarhansuseno.kajitsuokirumono.core.managers.InventoryManager;
import com.nadhirfarhansuseno.kajitsuokirumono.core.managers.ScoreManager;
import com.nadhirfarhansuseno.kajitsuokirumono.core.managers.ShopManager;
import com.nadhirfarhansuseno.kajitsuokirumono.core.systems.CollisionSystem;
import com.nadhirfarhansuseno.kajitsuokirumono.core.systems.CoinDropSystem;
import com.nadhirfarhansuseno.kajitsuokirumono.core.systems.DifficultySystem;
import com.nadhirfarhansuseno.kajitsuokirumono.core.systems.PotionSystem;
import com.nadhirfarhansuseno.kajitsuokirumono.core.systems.SpawnSystem;
import com.nadhirfarhansuseno.kajitsuokirumono.core.systems.StatDegradationSystem;
import com.nadhirfarhansuseno.kajitsuokirumono.shared.enums.PotionType;
import com.nadhirfarhansuseno.kajitsuokirumono.shared.models.FruitSliceEvent;
import com.nadhirfarhansuseno.kajitsuokirumono.shared.models.PlayerStats;
import com.nadhirfarhansuseno.kajitsuokirumono.shared.models.RewardEvent;

import java.awt.Color;
import java.awt.Point;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Queue;
import java.util.Random;
import java.util.Set;

public class GameSession {

    private static final int NORMAL_FRUIT_SCORE = 10;
    private static final int TOUGH_FRUIT_SCORE = 25;

    private static final double RED_BOMB_RATIO = 0.20;
    private static final double POTION_SPAWN_CHANCE = 0.08;

    private static final double FRENZY_SPEED_MULTIPLIER = 1.5;
    private static final double FRENZY_SPAWN_INTERVAL_MULTIPLIER = 0.6;

    private static final int SCREEN_WIDTH = 1280;
    private static final int SCREEN_HEIGHT = 720;
    private static final int MIN_SPAWN_DISTANCE = 100;
    private static final int SPAWN_MARGIN = 60;

    private final PotionSystem potionSystem;
    private final ScoreManager scoreManager;
    private final CoinManager coinManager;
    private final CoinDropSystem coinDropSystem;
    private final InventoryManager inventoryManager;
    private final DifficultySystem difficultySystem;
    private final BladeManager bladeManager;
    private final CollisionSystem collisionSystem;
    private final SpawnSystem spawnSystem;
    private final StatDegradationSystem statDegradationSystem;
    private final ShopManager shopManager;

    private final PlayerStats playerStats;

    private final List<Fruit> fruits;
    private final Set<Fruit> hitFruitsThisStroke;
    private final Set<Fruit> rewardedFruits;

    private final List<RewardEvent> pendingRewardEvents;
    private final List<FruitSliceEvent> pendingFruitSliceEvents;

    private final Random random;

    private int lives;
    private int coinsEarned;

    private double elapsedTime;
    private double spawnTimer;

    private boolean gameOver;

    public GameSession(
            PlayerStats playerStats,
            CoinManager coinManager,
            InventoryManager inventoryManager
    ) {
        this.playerStats = playerStats;
        this.coinManager = coinManager;
        this.inventoryManager = inventoryManager;

        this.potionSystem = new PotionSystem();
        this.scoreManager = new ScoreManager(potionSystem);
        this.coinDropSystem = new CoinDropSystem();
        this.difficultySystem = new DifficultySystem();
        this.bladeManager = new BladeManager();
        this.collisionSystem = new CollisionSystem();
        this.spawnSystem = new SpawnSystem();

        this.statDegradationSystem =
                new StatDegradationSystem(playerStats);

        this.shopManager = new ShopManager(
                coinManager,
                playerStats
        );

        this.fruits = new ArrayList<>();
        this.hitFruitsThisStroke = new HashSet<>();
        this.rewardedFruits = new HashSet<>();

        this.pendingRewardEvents = new ArrayList<>();
        this.pendingFruitSliceEvents = new ArrayList<>();

        this.random = new Random();

        this.bladeManager.setSpeedLevel(playerStats.getSpeed());

        this.lives = 3;
        this.coinsEarned = 0;
        this.elapsedTime = 0;
        this.spawnTimer = 0;
        this.gameOver = false;
    }

    public void update(double deltaTime) {
        if (gameOver) {
            return;
        }

        elapsedTime += deltaTime;

        potionSystem.update(deltaTime);
        difficultySystem.update(deltaTime);
        statDegradationSystem.update(deltaTime);

        bladeManager.setSpeedLevel(playerStats.getSpeed());

        if (!potionSystem.isActive(PotionType.FREEZE)) {
            updateSpawning(deltaTime);
            updateFruits(deltaTime);
        }
    }

    private void updateSpawning(double deltaTime) {
        spawnTimer += deltaTime;

        double interval = difficultySystem.getSpawnInterval();

        if (potionSystem.isActive(PotionType.FRENZY)) {
            interval *= FRENZY_SPAWN_INTERVAL_MULTIPLIER;
        }

        if (spawnTimer >= interval) {
            spawnTimer = 0;
            spawnWave();
        }
    }

    private void spawnWave() {
        int minAmount = difficultySystem.getMinSpawnAmount();
        int maxAmount = difficultySystem.getMaxSpawnAmount();

        int amount = random.nextInt(
                maxAmount - minAmount + 1
        ) + minAmount;

        List<Double> spawnPositions = generateSpawnPositions(amount);

        for (int i = 0; i < amount; i++) {
            spawnRandomObject(spawnPositions.get(i));

            if (gameOver) {
                break;
            }
        }

        if (!gameOver &&
                random.nextDouble() < POTION_SPAWN_CHANCE) {
            spawnRandomPotion();
        }
    }

    private List<Double> generateSpawnPositions(int amount) {
        List<Double> positions = new ArrayList<>();

        int minX = SPAWN_MARGIN;
        int maxX = SCREEN_WIDTH - SPAWN_MARGIN;

        int availableWidth = maxX - minX;

        int minimumDistance = Math.min(
                MIN_SPAWN_DISTANCE,
                availableWidth / Math.max(1, amount - 1)
        );

        int attempts = 0;
        int maxAttempts = 100;

        while (positions.size() < amount && attempts < maxAttempts) {
            double candidate = minX +
                    random.nextDouble() * availableWidth;

            boolean valid = true;

            for (double position : positions) {
                if (Math.abs(candidate - position) < minimumDistance) {
                    valid = false;
                    break;
                }
            }

            if (valid) {
                positions.add(candidate);
            }

            attempts++;
        }

        if (positions.size() < amount) {
            positions.clear();

            double spacing = availableWidth / (double) amount;

            for (int i = 0; i < amount; i++) {
                positions.add(
                        minX + spacing * (i + 0.5)
                );
            }
        }

        return positions;
    }

    private double getSpeedMultiplier() {
        return switch (difficultySystem.getDifficultyLevel()) {
            case EASY -> 1.0;
            case NORMAL -> 1.05;
            case HARD -> 1.10;
            case EXPERT -> 1.15;
            case EXTREME -> 1.20;
        };
    }

    private void spawnRandomObject(double spawnX) {
        double spawnRoll = random.nextDouble();

        double bombChance = difficultySystem.getBombChance();
        double toughChance = difficultySystem.getToughFruitChance();

        double speedMultiplier = getSpeedMultiplier();

        int normalDurability =
                playerStats.getDurability() >= 5 ? 1 : 2;

        int toughDurability =
                playerStats.getDurability() >= 5 ? 3 : 4;

        Fruit spawnedFruit;

        if (spawnRoll < bombChance) {

            boolean redBomb =
                    random.nextDouble() < RED_BOMB_RATIO;

            spawnedFruit = spawnSystem.spawnBomb(
                    SCREEN_WIDTH,
                    SCREEN_HEIGHT,
                    redBomb,
                    speedMultiplier
            );

        } else if (spawnRoll < bombChance + toughChance) {

            spawnedFruit = spawnSystem.spawnToughFruit(
                    SCREEN_WIDTH,
                    SCREEN_HEIGHT,
                    toughDurability,
                    speedMultiplier
            );

        } else {

            spawnedFruit = spawnSystem.spawnNormalFruit(
                    SCREEN_WIDTH,
                    SCREEN_HEIGHT,
                    normalDurability,
                    speedMultiplier
            );
        }

        spawnedFruit.setX(spawnX);
        addFruit(spawnedFruit);
    }

    private void spawnRandomPotion() {
        double spawnX = SPAWN_MARGIN +
                random.nextDouble() *
                        (SCREEN_WIDTH - SPAWN_MARGIN * 2);

        PotionItem potion = spawnSystem.spawnPotion(
                SCREEN_WIDTH,
                SCREEN_HEIGHT,
                getSpeedMultiplier()
        );

        potion.setX(spawnX);
        addFruit(potion);
    }

    private void updateFruits(double deltaTime) {
        double movementMultiplier =
                potionSystem.isActive(PotionType.FRENZY)
                        ? FRENZY_SPEED_MULTIPLIER
                        : 1.0;

        Iterator<Fruit> iterator = fruits.iterator();

        while (iterator.hasNext()) {
            Fruit fruit = iterator.next();

            if (fruit.isSliced()) {
                continue;
            }

            fruit.setMovementSpeedMultiplier(movementMultiplier);

            fruit.update(deltaTime, SCREEN_WIDTH, SCREEN_HEIGHT);

            if (fruit.getY() - fruit.getRadius() > SCREEN_HEIGHT) {

                if (fruit instanceof NormalFruit ||
                        fruit instanceof ToughFruit) {
                    loseLife();
                }

                iterator.remove();

                if (gameOver) {
                    break;
                }
            }
        }
    }

    public void processSlicingPoints(
            List<Point> points,
            boolean mousePressed
    ) {
        if (gameOver) {
            return;
        }

        if (!mousePressed) {
            Blade blade = bladeManager.getBlade();

            bladeManager.update(
                    0,
                    blade.getX(),
                    blade.getY(),
                    false
            );

            hitFruitsThisStroke.clear();
            return;
        }

        if (points == null || points.isEmpty()) {
            return;
        }

        Point firstPoint = points.get(0);

        bladeManager.update(
                0,
                firstPoint.x,
                firstPoint.y,
                true
        );

        for (Point point : points) {
            bladeManager.processMovementPoint(point);
            checkCollisions();

            if (gameOver) {
                break;
            }
        }
    }

    private void checkCollisions() {
        Blade blade = bladeManager.getBlade();

        if (!blade.isActive() || gameOver) {
            return;
        }

        double slashX = blade.getX() - blade.getPreviousX();
        double slashY = blade.getY() - blade.getPreviousY();

        double slashLength = Math.hypot(slashX, slashY);

        // Arah gerakan kedua belahan tegak lurus dengan tebasan.
        double splitDirectionX = 1.0;
        double splitDirectionY = 0.0;

        if (slashLength > 0.0001) {
            splitDirectionX = -slashY / slashLength;
            splitDirectionY = slashX / slashLength;
        }

        for (Fruit fruit : fruits) {
            if (fruit.isSliced()) {
                continue;
            }

            boolean collided =
                    collisionSystem.checkCollision(blade, fruit);

            if (!collided) {
                if (!(fruit instanceof Bomb)) {
                    hitFruitsThisStroke.remove(fruit);
                }

                continue;
            }

            if (!hitFruitsThisStroke.add(fruit)) {
                continue;
            }

            if (fruit instanceof NormalFruit normalFruit) {
                normalFruit.takeDamage();

                if (normalFruit.isSliced()) {
                    createFruitSliceEvent(
                            normalFruit,
                            splitDirectionX,
                            splitDirectionY
                    );

                    rewardFruit(normalFruit);
                }

            } else if (fruit instanceof ToughFruit toughFruit) {
                toughFruit.takeDamage();

                if (toughFruit.isSliced()) {
                    createFruitSliceEvent(
                            toughFruit,
                            splitDirectionX,
                            splitDirectionY
                    );

                    rewardFruit(toughFruit);
                }

            } else if (fruit instanceof PotionItem potionItem) {
                potionItem.slice();

                inventoryManager.addPotion(
                        potionItem.getPotionType()
                );

            } else if (fruit instanceof Bomb bomb) {

                if (bomb.isInstantGameOver()) {
                    endGame();

                } else {
                    bomb.explode();
                    loseLife();

                    if (!gameOver) {
                        triggerBlackBombChain(bomb);
                    }
                }

                if (gameOver) {
                    break;
                }
            }
        }
    }

    private void createFruitSliceEvent(
            Fruit fruit,
            double directionX,
            double directionY
    ) {
        Color color;

        if (fruit instanceof NormalFruit normalFruit) {
            color = normalFruit.getColor();

        } else if (fruit instanceof ToughFruit toughFruit) {
            color = toughFruit.getColor();

        } else {
            return;
        }

        pendingFruitSliceEvents.add(
                new FruitSliceEvent(
                        fruit.getX(),
                        fruit.getY(),
                        fruit.getRadius(),
                        color,
                        directionX,
                        directionY
                )
        );
    }

    private void triggerBlackBombChain(Bomb initialBomb) {
        Queue<Bomb> explosionQueue = new ArrayDeque<>();
        explosionQueue.add(initialBomb);

        while (!explosionQueue.isEmpty()) {
            Bomb currentBomb = explosionQueue.poll();

            double explosionX = currentBomb.getX();
            double explosionY = currentBomb.getY();

            for (Fruit fruit : fruits) {
                if (fruit.isSliced()) {
                    continue;
                }

                if (fruit instanceof NormalFruit normalFruit) {
                    normalFruit.slice();

                    hitFruitsThisStroke.add(fruit);

                    // Arah potongan menjauh dari pusat ledakan.
                    double directionX =
                            normalFruit.getX() - explosionX;

                    double directionY =
                            normalFruit.getY() - explosionY;

                    createFruitSliceEvent(
                            normalFruit,
                            directionX,
                            directionY
                    );

                    rewardFruit(normalFruit);

                } else if (fruit instanceof ToughFruit toughFruit) {
                    toughFruit.slice();

                    hitFruitsThisStroke.add(fruit);

                    double directionX =
                            toughFruit.getX() - explosionX;

                    double directionY =
                            toughFruit.getY() - explosionY;

                    createFruitSliceEvent(
                            toughFruit,
                            directionX,
                            directionY
                    );

                    rewardFruit(toughFruit);

                } else if (fruit instanceof Bomb bomb) {
                    if (!bomb.isInstantGameOver() &&
                            !bomb.isExploded()) {

                        bomb.explode();
                        hitFruitsThisStroke.add(bomb);
                        explosionQueue.add(bomb);
                    }
                }
            }
        }
    }

    private void rewardFruit(Fruit fruit) {
        if (!rewardedFruits.add(fruit)) {
            return;
        }

        int awardedScore = 0;
        int coins = 0;

        if (fruit instanceof NormalFruit) {
            awardedScore = scoreManager.addScore(NORMAL_FRUIT_SCORE);
            coins = coinDropSystem.calculateNormalFruitCoins();

        } else if (fruit instanceof ToughFruit) {
            awardedScore = scoreManager.addScore(TOUGH_FRUIT_SCORE);
            coins = coinDropSystem.calculateToughFruitCoins();
        }

        coinManager.addCoins(coins);

        pendingRewardEvents.add(
                new RewardEvent(
                        fruit.getX(),
                        fruit.getY(),
                        awardedScore,
                        coins
                )
        );
    }

    public List<RewardEvent> consumeRewardEvents() {
        List<RewardEvent> events =
                new ArrayList<>(pendingRewardEvents);

        pendingRewardEvents.clear();

        return events;
    }

    public List<FruitSliceEvent> consumeFruitSliceEvents() {
        List<FruitSliceEvent> events =
                new ArrayList<>(pendingFruitSliceEvents);

        pendingFruitSliceEvents.clear();

        return events;
    }

    public void addFruit(Fruit fruit) {
        if (fruit != null && !gameOver) {
            if (potionSystem.isActive(PotionType.FRENZY)) {
                fruit.setMovementSpeedMultiplier(
                        FRENZY_SPEED_MULTIPLIER
                );
            }

            fruits.add(fruit);
        }
    }

    public void cleanupSlicedFruits() {
        fruits.removeIf(Fruit::isSliced);
        hitFruitsThisStroke.removeIf(Fruit::isSliced);
        rewardedFruits.removeIf(Fruit::isSliced);
    }

    public List<Fruit> getFruits() {
        return fruits;
    }

    public SpawnSystem getSpawnSystem() {
        return spawnSystem;
    }

    public PotionSystem getPotionSystem() {
        return potionSystem;
    }

    public ScoreManager getScoreManager() {
        return scoreManager;
    }

    public CoinManager getCoinManager() {
        return coinManager;
    }

    public CoinDropSystem getCoinDropSystem() {
        return coinDropSystem;
    }

    public InventoryManager getInventoryManager() {
        return inventoryManager;
    }

    public DifficultySystem getDifficultySystem() {
        return difficultySystem;
    }

    public BladeManager getBladeManager() {
        return bladeManager;
    }

    public CollisionSystem getCollisionSystem() {
        return collisionSystem;
    }

    public PlayerStats getPlayerStats() {
        return playerStats;
    }

    public StatDegradationSystem getStatDegradationSystem() {
        return statDegradationSystem;
    }

    public ShopManager getShopManager() {
        return shopManager;
    }

    public int getLives() {
        return lives;
    }

    public double getElapsedTime() {
        return elapsedTime;
    }

    public boolean isGameOver() {
        return gameOver;
    }

    public int getCoinsEarned() {
        return coinsEarned;
    }

    private void bankSessionCoins() {
        coinsEarned = coinManager.getCurrentCoins();
        coinManager.bankCoins();
    }

    public void loseLife() {
        if (gameOver) {
            return;
        }

        lives--;

        if (lives <= 0) {
            lives = 0;
            gameOver = true;
            statDegradationSystem.stop();
            bankSessionCoins();
        }
    }

    public void endGame() {
        if (gameOver) {
            return;
        }

        gameOver = true;
        statDegradationSystem.stop();
        bankSessionCoins();
    }
}
