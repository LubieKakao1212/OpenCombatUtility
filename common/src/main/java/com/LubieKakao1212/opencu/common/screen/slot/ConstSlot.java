package com.LubieKakao1212.opencu.common.screen.slot;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.Slot;

public class ConstSlot extends Slot {

    private ItemStack stack;

    public ConstSlot(ItemStack stack, int index, int x, int y) {
        super(null, index, x, y);
        this.stack = stack;
    }

    @Override
    public boolean canInsert(ItemStack stack) { return false; }

    @Override
    public boolean canTakeItems(PlayerEntity playerEntity) { return false; }

    @Override
    public void setStackNoCallbacks(ItemStack stack) { }

    @Override
    public ItemStack getStack() { return stack; }

    @Override
    public void onTakeItem(PlayerEntity player, ItemStack stack) {}

    @Override
    public void markDirty() { }
}
