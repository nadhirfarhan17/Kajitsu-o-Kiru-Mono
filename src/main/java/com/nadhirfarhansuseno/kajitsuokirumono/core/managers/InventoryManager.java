
package com.nadhirfarhansuseno.kajitsuokirumono.core.managers;

import com.nadhirfarhansuseno.kajitsuokirumono.shared.enums.PotionType;

import java.util.EnumMap;
import java.util.Map;

public class InventoryManager {

    private static final int MAX_POTION_CAPACITY = 5;

    private final Map<PotionType, Integer> inventory;

    public InventoryManager() {
        inventory = new EnumMap<>(PotionType.class);

        for (PotionType type : PotionType.values()) {
            inventory.put(type, 0);
        }
    }

    public boolean addPotion(PotionType type) {
        if (type == null) {
            return false;
        }

        int currentAmount = inventory.get(type);

        if (currentAmount >= MAX_POTION_CAPACITY) {
            return false;
        }

        inventory.put(type, currentAmount + 1);
        return true;
    }

    public boolean usePotion(PotionType type) {
        if (type == null) {
            return false;
        }

        int currentAmount = inventory.get(type);

        if (currentAmount <= 0) {
            return false;
        }

        inventory.put(type, currentAmount - 1);
        return true;
    }

    public int getPotionCount(PotionType type) {
        if (type == null) {
            return 0;
        }

        return inventory.get(type);
    }

    public int getMaxPotionCapacity() {
        return MAX_POTION_CAPACITY;
    }

    public void reset() {
        for (PotionType type : PotionType.values()) {
            inventory.put(type, 0);
        }
    }
}
