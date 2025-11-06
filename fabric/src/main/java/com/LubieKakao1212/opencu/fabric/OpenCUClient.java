package com.LubieKakao1212.opencu.fabric;

import com.LubieKakao1212.opencu.common.block.entity.renderer.RendererDeviceContainer6Dir;
import com.LubieKakao1212.opencu.common.block.entity.renderer.RendererModularFrame;
import com.LubieKakao1212.opencu.common.screen.DeviceContainerScreen;
import com.LubieKakao1212.opencu.common.screen.ModularFrameScreen;
import com.LubieKakao1212.opencu.fabric.event.TooltipHandler;
import com.LubieKakao1212.opencu.fabric.model.CUModelLoadPlugin;
import com.LubieKakao1212.opencu.registry.CUBlockEntities;
import com.LubieKakao1212.opencu.registry.CUMenu;
import com.LubieKakao1212.opencu.registry.fabric.CUBlockEntitiesImpl;
import com.LubieKakao1212.opencu.registry.fabric.CUMenuImpl;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.model.loading.v1.ModelLoadingPlugin;
import net.minecraft.client.gui.screen.ingame.HandledScreens;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactories;

public class OpenCUClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        NetworkUtilImpl.clientInit();
        registerRenderers();
    }

    private void registerRenderers() {
        BlockEntityRendererFactories.register(CUBlockEntities.modularFrame(), RendererModularFrame::new);

        BlockEntityRendererFactories.register(CUBlockEntitiesImpl.REPULSOR, RendererDeviceContainer6Dir::new);
        BlockEntityRendererFactories.register(CUBlockEntitiesImpl.DISPENSER_GOLD, RendererDeviceContainer6Dir::new);
        BlockEntityRendererFactories.register(CUBlockEntitiesImpl.DISPENSER_DIAMOND, RendererDeviceContainer6Dir::new);
        BlockEntityRendererFactories.register(CUBlockEntitiesImpl.DISPENSER_NETHERITE, RendererDeviceContainer6Dir::new);

        HandledScreens.register(CUMenuImpl.MODULAR_FRAME, ModularFrameScreen::new);
        HandledScreens.register(CUMenuImpl.DEVICE_CONTAINER, DeviceContainerScreen::new);


        ModelLoadingPlugin.register(new CUModelLoadPlugin());

        TooltipHandler.init();
    }
}
