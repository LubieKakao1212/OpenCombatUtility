package com.LubieKakao1212.opencu.registry.forge;

import com.LubieKakao1212.opencu.common.OpenCUModCommon;
import com.LubieKakao1212.opencu.common.block.entity.BlockEntityDeviceContainer6Dir;
import com.LubieKakao1212.opencu.common.block.entity.BlockEntityModularFrame;
import com.LubieKakao1212.opencu.forge.block.entity.BlockEntityModularFrameImpl;
import com.LubieKakao1212.opencu.registry.CUIds;
import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.apache.commons.lang3.NotImplementedException;

public class CUBlockEntitiesImpl {

    public static RegistryObject<BlockEntityType<BlockEntityModularFrame>> MODULAR_FRAME;

    public static RegistryObject<BlockEntityType<BlockEntityDeviceContainer6Dir>> REPULSOR;
    public static RegistryObject<BlockEntityType<BlockEntityDeviceContainer6Dir>> DISPENSER_GOLD;
    public static RegistryObject<BlockEntityType<BlockEntityDeviceContainer6Dir>> DISPENSER_DIAMOND;
    public static RegistryObject<BlockEntityType<BlockEntityDeviceContainer6Dir>> DISPENSER_NETHERITE;

    private static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, OpenCUModCommon.MODID);

    static {
        MODULAR_FRAME = BLOCK_ENTITIES.register(CUIds.Str.DISPENSER, () -> BlockEntityType.Builder.<BlockEntityModularFrame>create(BlockEntityModularFrameImpl::new, CUBlocks.MODULAR_FRAME.get()).build(null));

        REPULSOR = BLOCK_ENTITIES.register(CUIds.Str.REPULSOR, () -> BlockEntityType.Builder.<BlockEntityDeviceContainer6Dir>create((pos, state) ->  { throw new RuntimeException("Not Implemented"); }, CUBlocks.REPULSOR.get()).build(null));
        DISPENSER_GOLD = BLOCK_ENTITIES.register(CUIds.Str.DISPENSER_GOLD, () -> BlockEntityType.Builder.<BlockEntityDeviceContainer6Dir>create((pos, state) ->  { throw new RuntimeException("Not Implemented"); }, CUBlocks.DISPENSER_GOLD.get()).build(null));
        DISPENSER_DIAMOND = BLOCK_ENTITIES.register(CUIds.Str.DISPENSER_DIAMOND, () -> BlockEntityType.Builder.<BlockEntityDeviceContainer6Dir>create((pos, state) ->  { throw new RuntimeException("Not Implemented"); }, CUBlocks.DISPENSER_DIAMOND.get()).build(null));
        DISPENSER_NETHERITE = BLOCK_ENTITIES.register(CUIds.Str.DISPENSER_NETHERITE, () -> BlockEntityType.Builder.<BlockEntityDeviceContainer6Dir>create((pos, state) ->  { throw new RuntimeException("Not Implemented"); }, CUBlocks.DISPENSER_NETHERITE.get()).build(null));

        CURegister.register(BLOCK_ENTITIES);
    }

    public static void init() {
//        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
//        BLOCK_ENTITIES.register(bus);
    }

    public static BlockEntityType<BlockEntityModularFrame> modularFrame() {
        return MODULAR_FRAME.get();
    }

}
