package com.LubieKakao1212.opencu.registry.fabric;

import com.LubieKakao1212.opencu.common.block.BlockDevice6Dir;
import com.LubieKakao1212.opencu.common.block.BlockModularFrame;
import io.wispforest.owo.itemgroup.OwoItemSettings;
import io.wispforest.owo.registration.reflect.BlockRegistryContainer;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import org.jetbrains.annotations.NotNull;

public class CUBlocksImpl implements BlockRegistryContainer {

    public static Block MODULAR_FRAME = new BlockModularFrame(FabricBlockSettings.create().strength(3f).nonOpaque());

    //region placed devices
    public static Block REPULSOR = new BlockDevice6Dir(FabricBlockSettings.create().strength(3f).nonOpaque(), () -> CUBlockEntitiesImpl.REPULSOR);

    public static Block DISPENSER_GOLD = new BlockDevice6Dir(FabricBlockSettings.create().strength(3f).nonOpaque(), () -> CUBlockEntitiesImpl.DISPENSER_GOLD);
    public static Block DISPENSER_DIAMOND = new BlockDevice6Dir(FabricBlockSettings.create().strength(3f).nonOpaque(), () -> CUBlockEntitiesImpl.DISPENSER_DIAMOND);
    public static Block DISPENSER_NETHERITE = new BlockDevice6Dir(FabricBlockSettings.create().strength(3f).nonOpaque(), () -> CUBlockEntitiesImpl.DISPENSER_NETHERITE);
    //endregion

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
