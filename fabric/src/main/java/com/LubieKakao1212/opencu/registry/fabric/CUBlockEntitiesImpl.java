package com.LubieKakao1212.opencu.registry.fabric;

import com.LubieKakao1212.opencu.common.block.entity.BlockEntityDeviceContainer6Dir;
import com.LubieKakao1212.opencu.common.block.entity.BlockEntityModularFrame;
import com.LubieKakao1212.opencu.fabric.block.entity.BlockEntityDeviceContainer6DirImpl;
import com.LubieKakao1212.opencu.fabric.block.entity.BlockEntityModularFrameImpl;
import com.LubieKakao1212.opencu.registry.CUBlockEntities;
import com.LubieKakao1212.opencu.registry.CUFramedDevices;
import io.wispforest.owo.registration.reflect.BlockEntityRegistryContainer;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.item.ItemStack;

public class CUBlockEntitiesImpl implements BlockEntityRegistryContainer {

    public static final BlockEntityType<BlockEntityModularFrame> MODULAR_FRAME = FabricBlockEntityTypeBuilder.<BlockEntityModularFrame>create(BlockEntityModularFrameImpl::new).addBlock(CUBlocks.MODULAR_FRAME).build();

    //region Placed Devices
    public static final BlockEntityType<BlockEntityDeviceContainer6Dir> REPULSOR = FabricBlockEntityTypeBuilder.create(
            BlockEntityDeviceContainer6DirImpl.factory(() -> CUBlockEntitiesImpl.REPULSOR, () -> new ItemStack(CUBlocks.REPULSOR), () -> CUFramedDevices.REPULSOR)
    ).addBlock(CUBlocks.REPULSOR).build();

    //region Dispensers
    public static final BlockEntityType<BlockEntityDeviceContainer6Dir> DISPENSER_GOLD = FabricBlockEntityTypeBuilder.create(
            BlockEntityDeviceContainer6DirImpl.factory(() -> CUBlockEntitiesImpl.DISPENSER_GOLD, () -> new ItemStack(CUBlocks.DISPENSER_GOLD), () -> CUFramedDevices.GOLD_DISPENSER)
    ).addBlock(CUBlocks.DISPENSER_GOLD).build();

    public static final BlockEntityType<BlockEntityDeviceContainer6Dir> DISPENSER_DIAMOND = FabricBlockEntityTypeBuilder.create(
            BlockEntityDeviceContainer6DirImpl.factory(() -> CUBlockEntitiesImpl.DISPENSER_DIAMOND, () -> new ItemStack(CUBlocks.DISPENSER_DIAMOND), () -> CUFramedDevices.DIAMOND_DISPENSER)
    ).addBlock(CUBlocks.DISPENSER_DIAMOND).build();

    public static final BlockEntityType<BlockEntityDeviceContainer6Dir> DISPENSER_NETHERITE = FabricBlockEntityTypeBuilder.create(
            BlockEntityDeviceContainer6DirImpl.factory(() -> CUBlockEntitiesImpl.DISPENSER_NETHERITE, () -> new ItemStack(CUBlocks.DISPENSER_NETHERITE), () -> CUFramedDevices.NETHERITE_DISPENSER)
    ).addBlock(CUBlocks.DISPENSER_NETHERITE).build();
    //endregion
    //endregion

    public static BlockEntityType<BlockEntityModularFrame> modularFrame() {
        return MODULAR_FRAME;
    }

}
