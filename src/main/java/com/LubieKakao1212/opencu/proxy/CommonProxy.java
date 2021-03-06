package com.LubieKakao1212.opencu.proxy;

import com.LubieKakao1212.opencu.OpenCUMod;
import com.LubieKakao1212.opencu.network.NetworkHandler;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class CommonProxy {

    public void init(FMLPreInitializationEvent event) {

    }

    public void init(FMLInitializationEvent event) {
        NetworkHandler.init();
    }

    public void init(FMLPostInitializationEvent event) {

    }

    public void registerItemRenderer(Item item, int meta, String id) {
        ModelLoader.setCustomModelResourceLocation(item, meta, new ModelResourceLocation(new ResourceLocation(OpenCUMod.MODID, id), "inventory"));
    }

}
