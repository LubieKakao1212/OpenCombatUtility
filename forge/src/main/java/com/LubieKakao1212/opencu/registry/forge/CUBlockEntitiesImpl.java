package com.LubieKakao1212.opencu.registry.forge;

import com.LubieKakao1212.opencu.common.OpenCUModCommon;
import com.LubieKakao1212.opencu.common.block.entity.BlockEntityDeviceContainer6Dir;
import com.LubieKakao1212.opencu.common.block.entity.BlockEntityModularFrame;
import com.LubieKakao1212.opencu.forge.block.entity.BlockEntityDeviceContainer6DirImpl;
import com.LubieKakao1212.opencu.forge.block.entity.BlockEntityModularFrameImpl;
import com.LubieKakao1212.opencu.registry.CUFramedDevices;
import com.LubieKakao1212.opencu.registry.CUIds;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.item.ItemStack;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class CUBlockEntitiesImpl {

    public static RegistryObject<BlockEntityType<BlockEntityModularFrame>> MODULAR_FRAME;

    public static RegistryObject<BlockEntityType<BlockEntityDeviceContainer6Dir>> REPULSOR;
    public static RegistryObject<BlockEntityType<BlockEntityDeviceContainer6Dir>> DISPENSER_GOLD;
    public static RegistryObject<BlockEntityType<BlockEntityDeviceContainer6Dir>> DISPENSER_DIAMOND;
    public static RegistryObject<BlockEntityType<BlockEntityDeviceContainer6Dir>> DISPENSER_NETHERITE;

    private static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, OpenCUModCommon.MODID);

    static {
        MODULAR_FRAME = BLOCK_ENTITIES.register(CUIds.Str.DISPENSER, () -> BlockEntityType.Builder.<BlockEntityModularFrame>create(BlockEntityModularFrameImpl::new, CUBlocks.MODULAR_FRAME.get()).build(null));

        REPULSOR            = BLOCK_ENTITIES.register(CUIds.Str.REPULSOR,            () -> BlockEntityType.Builder.create(BlockEntityDeviceContainer6DirImpl.factory(() -> REPULSOR.get(),            () -> new ItemStack(CUBlocks.REPULSOR.get()),            () -> CUFramedDevices.REPULSOR), CUBlocks.REPULSOR.get()).build(null));
        DISPENSER_GOLD      = BLOCK_ENTITIES.register(CUIds.Str.DISPENSER_GOLD,      () -> BlockEntityType.Builder.create(BlockEntityDeviceContainer6DirImpl.factory(() -> DISPENSER_GOLD.get(),      () -> new ItemStack(CUBlocks.DISPENSER_GOLD.get()),      () -> CUFramedDevices.GOLD_DISPENSER), CUBlocks.DISPENSER_GOLD.get()).build(null));
        DISPENSER_DIAMOND   = BLOCK_ENTITIES.register(CUIds.Str.DISPENSER_DIAMOND,   () -> BlockEntityType.Builder.create(BlockEntityDeviceContainer6DirImpl.factory(() -> DISPENSER_DIAMOND.get(),   () -> new ItemStack(CUBlocks.DISPENSER_DIAMOND.get()),   () -> CUFramedDevices.DIAMOND_DISPENSER), CUBlocks.DISPENSER_DIAMOND.get()).build(null));
        DISPENSER_NETHERITE = BLOCK_ENTITIES.register(CUIds.Str.DISPENSER_NETHERITE, () -> BlockEntityType.Builder.create(BlockEntityDeviceContainer6DirImpl.factory(() -> DISPENSER_NETHERITE.get(), () -> new ItemStack(CUBlocks.DISPENSER_NETHERITE.get()), () -> CUFramedDevices.NETHERITE_DISPENSER), CUBlocks.DISPENSER_NETHERITE.get()).build(null));

        CURegister.register(BLOCK_ENTITIES);
    }

    public static void init() { }

    public static BlockEntityType<BlockEntityModularFrame> modularFrame() {
        return MODULAR_FRAME.get();
    }

}
