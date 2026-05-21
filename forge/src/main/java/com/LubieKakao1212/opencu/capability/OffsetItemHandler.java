package com.LubieKakao1212.opencu.capability;

import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandlerModifiable;
import org.jetbrains.annotations.NotNull;

public class OffsetItemHandler implements IItemHandlerModifiable {

    private final IItemHandlerModifiable sourceHandler;
    private final int offset;

    public OffsetItemHandler(IItemHandlerModifiable sourceHandler, int offset) {
        if(sourceHandler.getSlots() <= offset) {
            throw new IllegalArgumentException();
        }
        this.sourceHandler = sourceHandler;
        this.offset = offset;
    }

    @Override
    public void setStackInSlot(int i, @NotNull ItemStack arg) {
        sourceHandler.setStackInSlot(i + offset, arg);
    }

    @Override
    public int getSlots() {
        return sourceHandler.getSlots() - 1;
    }

    @Override
    public @NotNull ItemStack getStackInSlot(int i) {
        return sourceHandler.getStackInSlot(i + offset);
    }

    @Override
    public @NotNull ItemStack insertItem(int i, @NotNull ItemStack arg, boolean bl) {
        return sourceHandler.insertItem(i + offset, arg, bl);
    }

    @Override
    public @NotNull ItemStack extractItem(int i, int j, boolean bl) {
        return sourceHandler.extractItem(i + offset, j, bl);
    }

    @Override
    public int getSlotLimit(int i) {
        return sourceHandler.getSlotLimit(i + offset);
    }

    @Override
    public boolean isItemValid(int i, @NotNull ItemStack arg) {
        return sourceHandler.isItemValid(i + offset, arg);
    }
}
