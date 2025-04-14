package com.LubieKakao1212.opencu.common.screen.slot;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;

public class ConstSlot extends ToggleableSlot {

    private static Inventory dummy = new SimpleInventory(1);

    private ItemStack stack;

    public ConstSlot(ItemStack stack, int x, int y) {
        super(dummy, 0, x, y);
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
