package com.LubieKakao1212.opencu.fabric.apilookup;

import com.LubieKakao1212.opencu.common.peripheral.DeviceContainerPeripheral;
import com.LubieKakao1212.opencu.common.peripheral.ModularFramePeripheral;
import com.LubieKakao1212.opencu.registry.CUBlockEntities;
import com.LubieKakao1212.opencu.registry.CUIds;
import dan200.computercraft.api.peripheral.PeripheralLookup;

public class APILookupPeripheral {

    public static void register() {
        PeripheralLookup.get().registerForBlockEntity((be, dir) -> new ModularFramePeripheral(be), CUBlockEntities.modularFrame());
        PeripheralLookup.get().registerForBlockEntity((be, dir) -> new DeviceContainerPeripheral(be, CUIds.REPULSOR.toString()), CUBlockEntities.repulsor());
    }
}
