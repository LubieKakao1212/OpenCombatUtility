package com.LubieKakao1212.opencu.common.screen.slot;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;

public class LockedSlot extends ToggleableSlot {

    public LockedSlot(int x, int y) {
        super(new SimpleInventory(1), 0, x, y);
    }

    @Override
    public boolean canInsert(ItemStack stack) {
        return false;
    }

    @Override
    public boolean canTakeItems(PlayerEntity playerEntity) {
        return false;
    }
}
