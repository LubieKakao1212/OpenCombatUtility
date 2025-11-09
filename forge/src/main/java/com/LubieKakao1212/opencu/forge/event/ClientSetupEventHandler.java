package com.LubieKakao1212.opencu.forge.event;

import com.LubieKakao1212.opencu.common.OpenCUModCommon;
import com.LubieKakao1212.opencu.common.block.entity.renderer.RendererDeviceContainer6Dir;
import com.LubieKakao1212.opencu.common.block.entity.renderer.RendererModularFrame;
import com.LubieKakao1212.opencu.common.device.DispenserTooltip;
import com.LubieKakao1212.opencu.registry.forge.CUBlockEntitiesImpl;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(value = Dist.CLIENT, modid = OpenCUModCommon.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ClientSetupEventHandler {
    @SubscribeEvent
    public static void registerBlockEntityRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerBlockEntityRenderer(CUBlockEntitiesImpl.MODULAR_FRAME.get(), RendererModularFrame::new);

        event.registerBlockEntityRenderer(CUBlockEntitiesImpl.REPULSOR.get(), RendererDeviceContainer6Dir::new);
        event.registerBlockEntityRenderer(CUBlockEntitiesImpl.DISPENSER_GOLD.get(), RendererDeviceContainer6Dir::new);
        event.registerBlockEntityRenderer(CUBlockEntitiesImpl.DISPENSER_DIAMOND.get(), RendererDeviceContainer6Dir::new);
        event.registerBlockEntityRenderer(CUBlockEntitiesImpl.DISPENSER_NETHERITE.get(), RendererDeviceContainer6Dir::new);
    }
}
