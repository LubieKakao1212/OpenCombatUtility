package com.LubieKakao1212.opencu.registry.fabric;

import com.LubieKakao1212.opencu.common.gui.ModularFrameScreen;
import com.LubieKakao1212.opencu.common.gui.container.DeviceContainerScreenHandler;
import com.LubieKakao1212.opencu.common.gui.container.ModularFrameScreenHandler;
import io.wispforest.owo.registration.reflect.AutoRegistryContainer;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.resource.featuretoggle.FeatureFlags;
import net.minecraft.screen.ScreenHandlerType;

public class CUMenuImpl implements AutoRegistryContainer<ScreenHandlerType<?>> {

    private static ScreenHandlerType<ModularFrameScreenHandler> MODULAR_FRAME = new ScreenHandlerType<>(ModularFrameScreenHandler::new, FeatureFlags.VANILLA_FEATURES);

    public static ScreenHandlerType<ModularFrameScreenHandler> modularFrame() {
        return MODULAR_FRAME;
    }


    /**
     * @return The registry the fields of this class should be registered into
     */
    @Override
    public Registry<ScreenHandlerType<?>> getRegistry() {
        return Registries.SCREEN_HANDLER;
    }

    /**
     * @return The class of <b>T</b>
     */
    @Override
    public Class<ScreenHandlerType<?>> getTargetFieldType() {
        return (Class<ScreenHandlerType<?>>) (Object) ScreenHandlerType.class;
    }
}
