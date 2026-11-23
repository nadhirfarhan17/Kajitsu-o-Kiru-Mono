
package com.nadhirfarhansuseno.kajitsuokirumono.frontend.input;

import com.nadhirfarhansuseno.kajitsuokirumono.core.managers.InventoryManager;
import com.nadhirfarhansuseno.kajitsuokirumono.core.systems.PotionSystem;
import com.nadhirfarhansuseno.kajitsuokirumono.shared.enums.PotionType;

public class PotionInputHandler {

    private final KeyboardInput keyboardInput;
    private final InventoryManager inventoryManager;
    private final PotionSystem potionSystem;

    public PotionInputHandler(
            KeyboardInput keyboardInput,
            InventoryManager inventoryManager,
            PotionSystem potionSystem
    ) {
        this.keyboardInput = keyboardInput;
        this.inventoryManager = inventoryManager;
        this.potionSystem = potionSystem;
    }

    public void update() {
        handlePotion(
                keyboardInput.isFreezePressed(),
                PotionType.FREEZE
        );
        keyboardInput.resetFreezePressed();

        handlePotion(
                keyboardInput.isFrenzyPressed(),
                PotionType.FRENZY
        );
        keyboardInput.resetFrenzyPressed();

        handlePotion(
                keyboardInput.isDoublePointsPressed(),
                PotionType.DOUBLE_POINTS
        );
        keyboardInput.resetDoublePointsPressed();
    }

    private void handlePotion(
            boolean pressed,
            PotionType type
    ) {
        if (!pressed) {
            return;
        }

        if (inventoryManager.usePotion(type)) {
            potionSystem.activatePotion(type);
        }
    }
}
