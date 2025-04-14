package com.LubieKakao1212.opencu.registry;

import com.LubieKakao1212.opencu.common.screen.handler.DeviceContainerScreenHandler;
import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.screen.ScreenHandlerType;

public class CUMenu {

    @ExpectPlatform
    public static ScreenHandlerType<DeviceContainerScreenHandler> deviceContainer() {
        return null;
    }

    @ExpectPlatform
    public static ScreenHandlerType<DeviceContainerScreenHandler> modularFrame() {
        return null;
    }

}
