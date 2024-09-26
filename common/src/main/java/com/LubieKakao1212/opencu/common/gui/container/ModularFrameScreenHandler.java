package com.LubieKakao1212.opencu.common.gui.container;

import com.LubieKakao1212.opencu.common.block.entity.BlockEntityModularFrame;
import com.LubieKakao1212.opencu.registry.CUBlocks;
import net.minecraft.block.Block;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.ArrayPropertyDelegate;
import net.minecraft.screen.PropertyDelegate;
import net.minecraft.screen.ScreenHandlerContext;

public class ModularFrameScreenHandler extends DeviceContainerScreenHandler {

    public ModularFrameScreenHandler(int id, PlayerInventory playerInventory) {
        this(id, playerInventory, SlotProvider.dummy(blockSlotCount), ScreenHandlerContext.EMPTY, new ArrayPropertyDelegate(BlockEntityModularFrame.screenPropertyCount));
    }

    public ModularFrameScreenHandler(int id, PlayerInventory playerInventory, SlotProvider deviceContainerSlots, ScreenHandlerContext context, PropertyDelegate properties) {
        super(CUBlocks.modularFrame(), id, playerInventory, deviceContainerSlots, context, properties, true);
    }
}
