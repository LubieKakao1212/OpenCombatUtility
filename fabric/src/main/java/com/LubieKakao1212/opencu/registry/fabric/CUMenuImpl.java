package com.LubieKakao1212.opencu.registry.fabric;

import com.LubieKakao1212.opencu.common.screen.handler.DeviceContainerScreenHandler;
import com.LubieKakao1212.opencu.common.screen.slot.LockedSlot;
import com.LubieKakao1212.opencu.common.screen.slot.SlotProvider;
import io.wispforest.owo.registration.reflect.AutoRegistryContainer;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.resource.featuretoggle.FeatureFlags;
import net.minecraft.screen.ScreenHandlerType;

import java.util.function.Supplier;

public class CUMenuImpl implements AutoRegistryContainer<ScreenHandlerType<?>> {

    public static final ScreenHandlerType<DeviceContainerScreenHandler> DEVICE_CONTAINER = new ScreenHandlerType<>(
            clientFactory(
                    () -> CUMenuImpl.DEVICE_CONTAINER,
                    SlotProvider.concat(
                            (idx, x, y) -> new LockedSlot(x, y),
                            1,
                            SlotProvider.dummy(9)
                    )
            ),
            FeatureFlags.VANILLA_FEATURES);
    public static final ScreenHandlerType<DeviceContainerScreenHandler> MODULAR_FRAME = new ScreenHandlerType<>(
            clientFactory(
                    () -> CUMenuImpl.MODULAR_FRAME,
                    SlotProvider.dummy(10)
            ),
            FeatureFlags.VANILLA_FEATURES);

    public static ScreenHandlerType<DeviceContainerScreenHandler> deviceContainer() {
        return DEVICE_CONTAINER;
    }

    public static ScreenHandlerType<DeviceContainerScreenHandler> modularFrame() {
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

    private static ScreenHandlerType.Factory<DeviceContainerScreenHandler> clientFactory(Supplier<ScreenHandlerType<?>> type, SlotProvider clientSlotProvider) {
        return (syncId, playerInventory) -> new DeviceContainerScreenHandler(type.get(), syncId, playerInventory, clientSlotProvider, 3);
    }

}
