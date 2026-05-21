package com.LubieKakao1212.opencu.forge.util;

import net.minecraft.item.ItemStack;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.world.World;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;
import org.jetbrains.annotations.NotNull;

public class ItemHandlerUtil {

    public static void scatterAndEmpty(@NotNull IItemHandlerModifiable handler, @NotNull World world, double x, double y, double z) {
        for (int i = 0; i < handler.getSlots(); i++) {
            var stack = handler.getStackInSlot(i);
            if(!stack.isEmpty()) {
                ItemScatterer.spawn(world, x, y, z, stack);
                handler.setStackInSlot(i, ItemStack.EMPTY);
            }
        }
    }

    public static DefaultedList<ItemStack> copyAsDefaultedList(@NotNull IItemHandler handler) {
        var result = DefaultedList.ofSize(handler.getSlots(), ItemStack.EMPTY);
        for (int i = 0; i < handler.getSlots(); i++) {
            var stack = handler.getStackInSlot(i);
            result.set(i, stack.copy());
        }
        return result;
    }

    public static void setFromDefaultedList(@NotNull IItemHandlerModifiable handler, DefaultedList<ItemStack> source) {
        for (int i = 0; i < handler.getSlots(); i++) {
            var stack = source.get(i);
            handler.setStackInSlot(i, stack);
        }
    }


}
