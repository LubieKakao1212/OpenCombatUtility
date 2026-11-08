package com.LubieKakao1212.opencu.registry;

import com.LubieKakao1212.opencu.common.config.api.IOpenCUConfig;
import com.LubieKakao1212.opencu.common.device.*;

public class CUFramedDevices {

    public static final IFramedDevice REPULSOR = new RepulsorDevice(IOpenCUConfig.CONFIG.repulsorDevice());

    public static final IFramedDevice VANILLA_DROPPER = new DispenserConstant(
            IOpenCUConfig.CONFIG.vanillaDispenserDevice(),
            CUDispensers.VANILLA_DROPPER
    );

    public static final IFramedDevice VANILLA_DISPENSER = new DispenserConstant(
            IOpenCUConfig.CONFIG.vanillaDispenserDevice(),
            CUDispensers.VANILLA_DISPENSER
    );

    public static final IFramedDevice GOLD_DISPENSER = new DispenserConstant(
            IOpenCUConfig.CONFIG.goldenDispenserDevice(),
            CUDispensers.VANILLA_DISPENSER
    );

    public static final IFramedDevice DIAMOND_DISPENSER = new DispenserConstant(
            IOpenCUConfig.CONFIG.diamondDispenserDevice(),
            CUDispensers.VANILLA_DISPENSER
    );

    public static final IFramedDevice NETHERITE_DISPENSER = new DispenserConstant(
            IOpenCUConfig.CONFIG.netheriteDispenserDevice(),
            CUDispensers.VANILLA_DISPENSER
    );


    public static final IFramedDevice SIMPLE_TRACKER = new TrackerBase(IOpenCUConfig.CONFIG.trackerDevice());
//    public static final IFramedDevice ARRAY_CONTROLLER = new ArrayControllerDevice();

}
