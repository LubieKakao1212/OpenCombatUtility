package com.LubieKakao1212.opencu.common.screen.handler;

import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerType;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public abstract class ScreenHandlerWithTabs extends ScreenHandler {

    public static final int playerSlotCount = 36;

    protected ScreenHandlerWithTabs(@Nullable ScreenHandlerType<?> type, int syncId) {
        super(type, syncId);
    }

    public void addSlotBlock(int startX, int startY, int blockWidth, int blockHeight, int slotSize, SlotFactory slotFactory) {
        for(int y = 0; y < blockHeight; y++)
            for(int x = 0; x < blockWidth; x++) {
                this.addSlot(slotFactory.get(startX + x * slotSize,  startY + y * slotSize));
            }
    }


//    @FunctionalInterface
//    public interface Factory {
//
//    }
}
