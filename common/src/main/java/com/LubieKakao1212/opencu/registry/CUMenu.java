package com.LubieKakao1212.opencu.registry;

import com.LubieKakao1212.opencu.common.gui.container.DeviceContainerScreenHandler;
import com.LubieKakao1212.opencu.common.gui.container.ModularFrameScreenHandler;
import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.screen.ScreenHandlerType;

public class CUMenu {

    @ExpectPlatform
    public static ScreenHandlerType<ModularFrameScreenHandler> modularFrame() {
        return null;
    }

}
