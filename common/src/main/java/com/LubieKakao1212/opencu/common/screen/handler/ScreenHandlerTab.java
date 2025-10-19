package com.LubieKakao1212.opencu.common.screen.handler;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;

public abstract class ScreenHandlerTab<THandler extends ScreenHandlerWithTabs> {

    public void setupContentSlots(THandler handler) {

    }

    public abstract ItemStack quickMoveFromTab(PlayerEntity player, int tabSlotIndex);

    public abstract ItemStack quickMoveFromPlayer(PlayerEntity player, int index);

}
