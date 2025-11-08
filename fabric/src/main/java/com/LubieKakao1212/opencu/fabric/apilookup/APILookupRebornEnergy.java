package com.LubieKakao1212.opencu.fabric.apilookup;

import com.LubieKakao1212.opencu.fabric.block.entity.BlockEntityDeviceContainer6DirImpl;
import com.LubieKakao1212.opencu.fabric.block.entity.BlockEntityModularFrameImpl;
import com.LubieKakao1212.opencu.registry.fabric.CUBlockEntitiesImpl;
import team.reborn.energy.api.EnergyStorage;

public class APILookupRebornEnergy {

    public static void register() {
        EnergyStorage.SIDED.registerForBlockEntity((be, side) -> {
            var frame = (BlockEntityModularFrameImpl) be;
            return frame.exposedEnegyStorage;
        }, CUBlockEntitiesImpl.MODULAR_FRAME);

        EnergyStorage.SIDED.registerForBlockEntities((be, side) -> {
            var be6 = (BlockEntityDeviceContainer6DirImpl) be;
            return be6.exposedEnergyStorage;
        },
                CUBlockEntitiesImpl.REPULSOR,
                CUBlockEntitiesImpl.DISPENSER_GOLD,
                CUBlockEntitiesImpl.DISPENSER_DIAMOND,
                CUBlockEntitiesImpl.DISPENSER_NETHERITE);
    }


}
