package com.LubieKakao1212.opencu.registry.forge;

import com.LubieKakao1212.opencu.common.screen.DeviceContainerScreen;
import com.LubieKakao1212.opencu.common.screen.ModularFrameScreen;
import com.LubieKakao1212.opencu.registry.CUMenu;
import net.minecraft.client.gui.screen.ingame.HandledScreens;

public class CUGuis {

    public static void init() {
        HandledScreens.register(CUMenu.modularFrame(), ModularFrameScreen::new);
        HandledScreens.register(CUMenu.deviceContainer(), DeviceContainerScreen::new);
    }


}
