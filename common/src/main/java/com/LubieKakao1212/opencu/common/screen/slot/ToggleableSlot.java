package com.LubieKakao1212.opencu.common.screen.slot;

import net.minecraft.inventory.Inventory;
import net.minecraft.screen.slot.Slot;

public class ToggleableSlot extends Slot {

    private boolean isEnabled;

    public ToggleableSlot(Inventory inventory, int index, int x, int y) {
        super(inventory, index, x, y);
    }

    @Override
    public boolean isEnabled() {
        return isEnabled;
    }

    public void setEnabled(boolean enabled) {
        isEnabled = enabled;
    }
}
