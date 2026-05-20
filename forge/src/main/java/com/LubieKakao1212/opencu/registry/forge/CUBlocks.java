package com.LubieKakao1212.opencu.registry.forge;

import com.LubieKakao1212.opencu.common.OpenCUModCommon;
import com.LubieKakao1212.opencu.common.block.BlockDevice6Dir;
import com.LubieKakao1212.opencu.common.block.BlockModularFrame;
import com.LubieKakao1212.opencu.registry.CUIds;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class CUBlocks {

    public static RegistryObject<Block> MODULAR_FRAME;

    //region placed devices
    public static RegistryObject<Block> REPULSOR;

    public static RegistryObject<Block> DISPENSER_GOLD;
    public static RegistryObject<Block> DISPENSER_DIAMOND;
    public static RegistryObject<Block> DISPENSER_NETHERITE;
    //endregion

    private static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, OpenCUModCommon.MODID);

    static
    {
        MODULAR_FRAME = registerBlockItem(BLOCKS.register(CUIds.Str.MODULAR_FRAME, () -> new BlockModularFrame(AbstractBlock.Settings.create().strength(3f).nonOpaque())));

        REPULSOR = registerBlockItem(BLOCKS.register(CUIds.Str.REPULSOR, () -> new BlockDevice6Dir(AbstractBlock.Settings.create().strength(3f).nonOpaque(), () -> CUBlockEntitiesImpl.REPULSOR.get())));
        DISPENSER_GOLD = registerBlockItem(BLOCKS.register(CUIds.Str.DISPENSER_GOLD, () -> new BlockDevice6Dir(AbstractBlock.Settings.create().strength(3f).nonOpaque(), () -> CUBlockEntitiesImpl.DISPENSER_GOLD.get())));
        DISPENSER_DIAMOND = registerBlockItem(BLOCKS.register(CUIds.Str.DISPENSER_DIAMOND, () -> new BlockDevice6Dir(AbstractBlock.Settings.create().strength(3f).nonOpaque(), () -> CUBlockEntitiesImpl.DISPENSER_DIAMOND.get())));
        DISPENSER_NETHERITE = registerBlockItem(BLOCKS.register(CUIds.Str.DISPENSER_NETHERITE, () -> new BlockDevice6Dir(AbstractBlock.Settings.create().strength(3f).nonOpaque(), () -> CUBlockEntitiesImpl.DISPENSER_NETHERITE.get())));

        //TODO check if this works
        CURegister.register(BLOCKS);
    }

    public static void init() {
        //CURegister.register(BLOCKS);
    }

    private static RegistryObject<Block> registerBlockItem(RegistryObject<Block> obj) {
        CUItems.register(obj.getId().getPath(), () -> new BlockItem(obj.get(), new Item.Settings()));
        return obj;
    }



}
