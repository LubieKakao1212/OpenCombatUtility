package com.LubieKakao1212.opencu.registry;

import com.LubieKakao1212.opencu.common.config.api.IOpenCUConfig;
import com.LubieKakao1212.opencu.common.device.*;

public class CUFramedDevices {

    public static final IFramedDevice REPULSOR = new RepulsorDevice();

    public static final IFramedDevice VANILLA_DROPPER = new DispenserConstant(
            CUDispensers.VANILLA_DROPPER,
            IOpenCUConfig.CONFIG.vanillaDispenserDevice()
    );

    public static final IFramedDevice VANILLA_DISPENSER = new DispenserConstant(
            CUDispensers.VANILLA_DISPENSER,
            IOpenCUConfig.CONFIG.vanillaDispenserDevice()
    );

    public static final IFramedDevice GOLD_DISPENSER = new DispenserConstant(
            CUDispensers.VANILLA_DISPENSER,
            IOpenCUConfig.CONFIG.goldenDispenserDevice()
    );

    public static final IFramedDevice DIAMOND_DISPENSER = new DispenserConstant(
            CUDispensers.VANILLA_DISPENSER,
            IOpenCUConfig.CONFIG.diamondDispenserDevice()
    );

    public static final IFramedDevice NETHERITE_DISPENSER = new DispenserConstant(
            CUDispensers.VANILLA_DISPENSER,
            IOpenCUConfig.CONFIG.netheriteDispenserDevice()
    );


    public static final IFramedDevice SIMPLE_TRACKER = new TrackerBase(IOpenCUConfig.CONFIG.trackerDevice());
    public static final IFramedDevice ARRAY_CONTROLLER = new ArrayControllerDevice();

}
