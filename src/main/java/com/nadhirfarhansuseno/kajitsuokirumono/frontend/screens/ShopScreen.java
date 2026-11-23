
package com.nadhirfarhansuseno.kajitsuokirumono.frontend.screens;

import com.nadhirfarhansuseno.kajitsuokirumono.core.managers.ShopManager;
import com.nadhirfarhansuseno.kajitsuokirumono.frontend.components.Button;
import com.nadhirfarhansuseno.kajitsuokirumono.shared.enums.PotionType;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

public class ShopScreen implements Screen {

    private static final Color BACKGROUND_COLOR =
            new Color(25, 35, 55);

    private static final Color BAR_COLOR =
            new Color(39, 53, 77);

    private static final Color BAR_BORDER =
            new Color(75, 95, 125);

    private static final Color TITLE_COLOR =
            new Color(255, 190, 75);

    private static final Color TEXT_COLOR =
            Color.WHITE;

    private static final Color SECONDARY_COLOR =
            new Color(185, 198, 215);

    private static final Color SUCCESS_COLOR =
            new Color(100, 220, 130);

    private static final Color ERROR_COLOR =
            new Color(255, 100, 100);

    private final ScreenManager screenManager;
    private final ShopManager shopManager;

    private final Button backButton;

    private final Button durabilityButton;
    private final Button speedButton;

    private final Button freezeButton;
    private final Button frenzyButton;
    private final Button doublePointsButton;

    private String purchaseMessage = "";
    private Color messageColor = TEXT_COLOR;

    public ShopScreen(ScreenManager screenManager) {
        this.screenManager = screenManager;

        this.shopManager = new ShopManager(
                screenManager.getCoinManager(),
                screenManager.getPlayerStats(),
                screenManager.getInventoryManager()
        );

        backButton = new Button(
                40, 35, 140, 45,
                "BACK",
                () -> screenManager.setScreen(
                        new MainMenuScreen(screenManager)
                )
        );

        durabilityButton = new Button(
                900, 258, 160, 48,
                "UPGRADE",
                this::buyDurability
        );

        speedButton = new Button(
                900, 340, 160, 48,
                "UPGRADE",
                this::buySpeed
        );

        freezeButton = new Button(
                900, 422, 160, 48,
                "BUY",
                () -> buyPotion(PotionType.FREEZE)
        );

        frenzyButton = new Button(
                900, 504, 160, 48,
                "BUY",
                () -> buyPotion(PotionType.FRENZY)
        );

        doublePointsButton = new Button(
                900, 586, 160, 48,
                "BUY",
                () -> buyPotion(PotionType.DOUBLE_POINTS)
        );
    }

    private void buyDurability() {
        boolean success = shopManager.buyDurabilityUpgrade();

        if (success) {
            showMessage("Durability upgraded successfully!", true);
        } else {
            showMessage(
                    "Not enough coins or maximum level reached!",
                    false
            );
        }
    }

    private void buySpeed() {
        boolean success = shopManager.buySpeedUpgrade();

        if (success) {
            showMessage("Speed upgraded successfully!", true);
        } else {
            showMessage(
                    "Not enough coins or maximum level reached!",
                    false
            );
        }
    }

    private void buyPotion(PotionType type) {
        boolean success = shopManager.buyPotion(type);

        if (success) {
            showMessage(
                    type.name() + " potion purchased successfully!",
                    true
            );
        } else {
            showMessage(
                    "Not enough coins or potion inventory is full!",
                    false
            );
        }
    }

    private void showMessage(String message, boolean success) {
        purchaseMessage = message;
        messageColor = success ? SUCCESS_COLOR : ERROR_COLOR;
    }

    @Override
    public void update(double deltaTime) {
    }

    @Override
    public void render(Graphics2D g2d) {
        Graphics2D graphics =
                (Graphics2D) g2d.create();

        graphics.setRenderingHint(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON
        );

        drawBackground(graphics);
        drawHeader(graphics);
        drawShopRows(graphics);
        drawPurchaseMessage(graphics);

        backButton.render(graphics);

        durabilityButton.render(graphics);
        speedButton.render(graphics);

        freezeButton.render(graphics);
        frenzyButton.render(graphics);
        doublePointsButton.render(graphics);

        graphics.dispose();
    }

    private void drawBackground(Graphics2D g2d) {
        g2d.setColor(BACKGROUND_COLOR);
        g2d.fillRect(0, 0, 1280, 720);
    }

    private void drawHeader(Graphics2D g2d) {
        g2d.setFont(
                new Font("SansSerif", Font.BOLD, 44)
        );

        g2d.setColor(TITLE_COLOR);

        String title = "SHOP";

        int titleWidth =
                g2d.getFontMetrics().stringWidth(title);

        g2d.drawString(
                title,
                (1280 - titleWidth) / 2,
                95
        );

        g2d.setFont(
                new Font("SansSerif", Font.PLAIN, 17)
        );

        g2d.setColor(SECONDARY_COLOR);

        String subtitle =
                "Upgrade your abilities and purchase potions";

        int subtitleWidth =
                g2d.getFontMetrics().stringWidth(subtitle);

        g2d.drawString(
                subtitle,
                (1280 - subtitleWidth) / 2,
                128
        );

        g2d.setFont(
                new Font("SansSerif", Font.BOLD, 22)
        );

        g2d.setColor(TITLE_COLOR);

        String coins = "Total Coins: "
                + screenManager.getCoinManager().getTotalCoins();

        int coinsWidth =
                g2d.getFontMetrics().stringWidth(coins);

        g2d.drawString(
                coins,
                (1280 - coinsWidth) / 2,
                185
        );
    }

    private void drawShopRows(Graphics2D g2d) {
        drawRow(g2d, 240, "DURABILITY",
                "Improves fruit resistance",
                "Level: "
                        + screenManager.getPlayerStats().getDurability()
                        + "/10",
                "Price: " + shopManager.getDurabilityUpgradePrice());

        drawRow(g2d, 322, "SPEED",
                "Improves blade movement",
                "Level: "
                        + screenManager.getPlayerStats().getSpeed()
                        + "/10",
                "Price: " + shopManager.getSpeedUpgradePrice());

        drawRow(g2d, 404, "FREEZE POTION",
                "Freezes gameplay for 5 seconds",
                "Stock: "
                        + screenManager.getInventoryManager()
                        .getPotionCount(PotionType.FREEZE)
                        + "/5",
                "Price: "
                        + shopManager.getPotionPrice(PotionType.FREEZE));

        drawRow(g2d, 486, "FRENZY POTION",
                "Activates frenzy for 10 seconds",
                "Stock: "
                        + screenManager.getInventoryManager()
                        .getPotionCount(PotionType.FRENZY)
                        + "/5",
                "Price: "
                        + shopManager.getPotionPrice(PotionType.FRENZY));

        drawRow(g2d, 568, "DOUBLE POINTS POTION",
                "Doubles score for 15 seconds",
                "Stock: "
                        + screenManager.getInventoryManager()
                        .getPotionCount(PotionType.DOUBLE_POINTS)
                        + "/5",
                "Price: "
                        + shopManager.getPotionPrice(PotionType.DOUBLE_POINTS));
    }

    private void drawRow(
            Graphics2D g2d,
            int y,
            String title,
            String description,
            String stock,
            String price
    ) {
        int x = 210;
        int width = 860;
        int height = 68;

        g2d.setColor(BAR_COLOR);
        g2d.fillRoundRect(x, y, width, height, 14, 14);

        g2d.setColor(BAR_BORDER);
        g2d.drawRoundRect(x, y, width, height, 14, 14);

        g2d.setFont(
                new Font("SansSerif", Font.BOLD, 18)
        );

        g2d.setColor(TITLE_COLOR);
        g2d.drawString(title, 235, y + 27);

        g2d.setFont(
                new Font("SansSerif", Font.PLAIN, 14)
        );

        g2d.setColor(SECONDARY_COLOR);
        g2d.drawString(description, 235, y + 49);

        g2d.setFont(
                new Font("SansSerif", Font.BOLD, 15)
        );

        g2d.setColor(TEXT_COLOR);
        g2d.drawString(stock, 630, y + 27);

        g2d.setFont(
                new Font("SansSerif", Font.PLAIN, 15)
        );

        g2d.drawString(price, 630, y + 49);
    }

    private void drawPurchaseMessage(Graphics2D g2d) {
        if (purchaseMessage.isEmpty()) {
            return;
        }

        g2d.setFont(
                new Font("SansSerif", Font.BOLD, 15)
        );

        g2d.setColor(messageColor);

        int textWidth =
                g2d.getFontMetrics().stringWidth(purchaseMessage);

        g2d.drawString(
                purchaseMessage,
                (1280 - textWidth) / 2,
                680
        );
    }

    @Override
    public void onMouseMoved(int mouseX, int mouseY) {
        backButton.updateHover(mouseX, mouseY);

        durabilityButton.updateHover(mouseX, mouseY);
        speedButton.updateHover(mouseX, mouseY);

        freezeButton.updateHover(mouseX, mouseY);
        frenzyButton.updateHover(mouseX, mouseY);
        doublePointsButton.updateHover(mouseX, mouseY);
    }

    @Override
    public void onMouseClicked(int mouseX, int mouseY) {
        backButton.onClick(mouseX, mouseY);

        durabilityButton.onClick(mouseX, mouseY);
        speedButton.onClick(mouseX, mouseY);

        freezeButton.onClick(mouseX, mouseY);
        frenzyButton.onClick(mouseX, mouseY);
        doublePointsButton.onClick(mouseX, mouseY);
    }
}
