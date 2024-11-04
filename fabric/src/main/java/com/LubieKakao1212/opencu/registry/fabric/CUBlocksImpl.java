package com.LubieKakao1212.opencu.registry.fabric;

import com.LubieKakao1212.opencu.common.block.BlockDevice6Dir;
import com.LubieKakao1212.opencu.fabric.block.BlockModularFrame;
import io.wispforest.owo.itemgroup.OwoItemSettings;
import io.wispforest.owo.registration.reflect.BlockRegistryContainer;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.Material;
import net.minecraft.item.BlockItem;
import org.jetbrains.annotations.NotNull;

public class CUBlocksImpl implements BlockRegistryContainer {

    public static Block REPULSOR = new BlockDevice6Dir(FabricBlockSettings.of(Material.METAL).strength(3f).nonOpaque(), () -> CUBlockEntitiesImpl.REPULSOR);//new BlockRepulsor(FabricBlockSettings.of(Material.METAL).strength(3f).nonOpaque());
    public static Block MODULAR_FRAME = new BlockModularFrame(FabricBlockSettings.of(Material.METAL).strength(3f).nonOpaque());

    @NotNull
    public static Block repulsor() {
        return REPULSOR;
    }

    @NotNull
    public static Block modularFrame() {
        return MODULAR_FRAME;
    }

    @Override
    public BlockItem createBlockItem(Block block, String identifier) {
        return new BlockItem(block, new OwoItemSettings().group(CUItemGroups.OCU_MAIN));
    }
}
