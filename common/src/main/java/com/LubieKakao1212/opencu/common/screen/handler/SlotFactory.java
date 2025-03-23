package com.LubieKakao1212.opencu.common.screen.handler;

import net.minecraft.screen.slot.Slot;

@FunctionalInterface
public interface SlotFactory {

    Slot get(int x, int y);

}
