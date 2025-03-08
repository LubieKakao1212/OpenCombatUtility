package com.LubieKakao1212.opencu.fabric.apilookup;

import com.LubieKakao1212.opencu.fabric.block.entity.BlockEntityDeviceContainer6DirImpl;
import com.LubieKakao1212.opencu.fabric.block.entity.BlockEntityModularFrameImpl;
import com.LubieKakao1212.opencu.registry.fabric.CUBlockEntitiesImpl;
import net.fabricmc.fabric.api.transfer.v1.item.ItemStorage;

public class APILookupItemStorage {

    public static void register() {
        ItemStorage.SIDED.registerForBlockEntities((blockEntity, context) -> {
            var device = (BlockEntityDeviceContainer6DirImpl) blockEntity;
            return device.exposedAmmoStorage;
        }, CUBlockEntitiesImpl.DISPENSER_DIAMOND, CUBlockEntitiesImpl.DISPENSER_GOLD, CUBlockEntitiesImpl.DISPENSER_NETHERITE);

        ItemStorage.SIDED.registerForBlockEntity((blockEntity, context) -> ((BlockEntityModularFrameImpl)blockEntity).ammoStorage,
                CUBlockEntitiesImpl.MODULAR_FRAME);
    }

}
