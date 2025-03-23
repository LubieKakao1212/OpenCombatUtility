package com.LubieKakao1212.opencu.registry.fabric;

import com.LubieKakao1212.opencu.common.screen.handler.DeviceContainerScreenHandler;
import com.LubieKakao1212.opencu.common.screen.slot.SlotProvider;
import io.wispforest.owo.registration.reflect.AutoRegistryContainer;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.resource.featuretoggle.FeatureFlags;
import net.minecraft.screen.ScreenHandlerType;

import java.util.function.Supplier;

public class CUMenuImpl implements AutoRegistryContainer<ScreenHandlerType<?>> {

    public static final ScreenHandlerType<DeviceContainerScreenHandler> MODULAR_FRAME = new ScreenHandlerType<>(
            clientFactory(
                    () -> CUMenuImpl.MODULAR_FRAME,
                    SlotProvider.concat(
                            SlotProvider.dummy(9),
                            9,
                            SlotProvider.constant(new Supplier[] {
                                    () -> new ItemStack(CUBlocksImpl.DISPENSER_NETHERITE)
                            })
                    )
            ),
            FeatureFlags.VANILLA_FEATURES);
//    private static ScreenHandlerType<DeviceContainerScreenHandler> DISPENSER = new ScreenHandlerType<>(DeviceContainerScreenHandler::new, FeatureFlags.VANILLA_FEATURES);

    public static ScreenHandlerType<DeviceContainerScreenHandler> deviceContainer() {
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
