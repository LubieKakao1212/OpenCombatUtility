package com.LubieKakao1212.opencu.registry.forge;

import com.LubieKakao1212.opencu.common.screen.ModularFrameScreen;
import com.LubieKakao1212.opencu.registry.CUMenu;
import net.minecraft.client.gui.screen.ingame.HandledScreens;

public class CUGuis {

    public static void inti() {
        HandledScreens.register(CUMenu.modularFrame(), ModularFrameScreen::new);
    }


}
