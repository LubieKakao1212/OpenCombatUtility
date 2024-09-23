package com.LubieKakao1212.opencu.fabric.apilookup;

import com.LubieKakao1212.opencu.common.device.IFramedDevice;
import com.LubieKakao1212.opencu.registry.CUBlocks;
import com.LubieKakao1212.opencu.registry.CUIds;
import com.LubieKakao1212.opencu.registry.fabric.CUItems;
import net.fabricmc.fabric.api.lookup.v1.item.ItemApiLookup;
import net.minecraft.item.Items;

import static com.LubieKakao1212.opencu.registry.CUFramedDevices.*;

public class APILookupIFramedDevice {

    public static ItemApiLookup<IFramedDevice, Void> FRAMED_DEVICES = ItemApiLookup.get(CUIds.FRAMED_DEVICE_API, IFramedDevice.class, Void.class);

    public static void init() {
        FRAMED_DEVICES.registerForItems((stack, ctx) -> VANILLA_DROPPER, Items.DROPPER);
        FRAMED_DEVICES.registerForItems((stack, ctx) -> VANILLA_DISPENSER, Items.DISPENSER);
        FRAMED_DEVICES.registerForItems((stack, ctx) -> GOLD_DISPENSER, CUItems.DISPENSER_GOLD);
        FRAMED_DEVICES.registerForItems((stack, ctx) -> DIAMOND_DISPENSER, CUItems.DISPENSER_DIAMOND);
        FRAMED_DEVICES.registerForItems((stack, ctx) -> NETHERITE_DISPENSER, CUItems.DISPENSER_NETHERITE);

        FRAMED_DEVICES.registerForItems((stack, ctx) -> REPULSOR, CUBlocks.repulsor());

        FRAMED_DEVICES.registerForItems((stack, ctx) -> SIMPLE_TRACKER, Items.ENDER_EYE);
        //Broken
        //DISPENSER.registerForItems((stack, ctx) -> array_controller, Items.ENDER_PEARL);
    }

}
