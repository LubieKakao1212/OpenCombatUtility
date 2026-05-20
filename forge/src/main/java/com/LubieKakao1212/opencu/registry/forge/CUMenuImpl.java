package com.LubieKakao1212.opencu.registry.forge;

import com.LubieKakao1212.opencu.common.OpenCUModCommon;
import com.LubieKakao1212.opencu.common.screen.handler.DeviceContainerScreenHandler;
import com.LubieKakao1212.opencu.common.screen.slot.LockedSlot;
import com.LubieKakao1212.opencu.common.screen.slot.SlotProvider;
import com.LubieKakao1212.opencu.forge.proxy.Proxy;
import com.LubieKakao1212.opencu.registry.CUIds;
import net.minecraft.resource.featuretoggle.FeatureFlags;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

public class CUMenuImpl {

    private static final DeferredRegister<ScreenHandlerType<?>> MENUS = DeferredRegister.create(ForgeRegistries.MENU_TYPES, OpenCUModCommon.MODID);

    public static RegistryObject<ScreenHandlerType<DeviceContainerScreenHandler>> MODULAR_FRAME = MENUS.register(CUIds.MODULAR_FRAME.getPath(),
            () -> create(CUMenuImpl::modularFrame,
                    SlotProvider.dummy(10)));

    public static RegistryObject<ScreenHandlerType<DeviceContainerScreenHandler>> DEVICE_CONTAINER = MENUS.register(CUIds.DEVICE_CONTAINER.getPath(),
            () -> create(CUMenuImpl::deviceContainer,
                    SlotProvider.concat(
                            (idx, x, y) -> new LockedSlot(x, y),
                            1,
                            SlotProvider.dummy(9)
                    )));

    public static void init() {
        CURegister.register(MENUS);
    }

    @NotNull
    public static ScreenHandlerType<DeviceContainerScreenHandler> modularFrame() {
        return MODULAR_FRAME.get();
    }

    @NotNull
    public static ScreenHandlerType<DeviceContainerScreenHandler> deviceContainer() {
        return DEVICE_CONTAINER.get();
    }


    private static ScreenHandlerType<DeviceContainerScreenHandler> create(Supplier<ScreenHandlerType<?>> type, SlotProvider clientSlotProvider) {
        return IForgeMenuType.create((id, inv, buf) ->
                new DeviceContainerScreenHandler(
                        type.get(),
                        id, inv,
                        clientSlotProvider,
                        3
                ));
    }
}
