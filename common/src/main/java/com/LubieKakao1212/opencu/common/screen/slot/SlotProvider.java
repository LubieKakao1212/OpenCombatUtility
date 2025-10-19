package com.LubieKakao1212.opencu.common.screen.slot;

import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.Slot;

import java.util.function.Supplier;

@FunctionalInterface
public interface SlotProvider {

    Slot createSlot(int idx, int x, int y);

    static SlotProvider of(Inventory inv) {
        return (idx, x, y) -> new ToggleableSlot(inv, idx, x, y);
    }

    static SlotProvider dummy(int slotCount) {
        return of(new SimpleInventory(slotCount));
    }

    static SlotProvider constant(Supplier<ItemStack>[] stacks) {
        return (idx, x, y) -> new ConstSlot(stacks[idx].get(), x, y);
    }

    static SlotProvider concat(SlotProvider first, int firstCount, SlotProvider second) {
        return (idx, x, y) -> idx < firstCount ? first.createSlot(idx, x, y) : second.createSlot(idx - firstCount, x, y);
    }
}
