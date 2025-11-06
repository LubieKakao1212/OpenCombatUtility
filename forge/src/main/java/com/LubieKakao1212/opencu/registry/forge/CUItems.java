package com.LubieKakao1212.opencu.registry.forge;

import com.LubieKakao1212.opencu.common.OpenCUModCommon;
import com.LubieKakao1212.opencu.common.item.ItemAimTool;
import com.LubieKakao1212.opencu.common.item.ItemLinkTool;
import net.minecraft.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

public class CUItems {

    public static final RegistryObject<Item> AIM_TOOL;

    public static final RegistryObject<Item> LINK_TOOL;

    private static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, OpenCUModCommon.MODID);

    static {
        AIM_TOOL = ITEMS.register(ID.AIM_TOOL, () -> new ItemAimTool(new Item.Settings().maxCount(1)));
        LINK_TOOL = ITEMS.register(ID.LINK_TOOL, () -> new ItemLinkTool(new Item.Settings().maxCount(1)));
    }

    public static void init() {
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        ITEMS.register(bus);
    }

    public static void register(String id, Supplier<Item> item) {
        ITEMS.register(id, item);
    }


    public static class ID {
        public static final String AIM_TOOL = "aim_tool";
        public static final String LINK_TOOL = "link_tool";
    }
}
