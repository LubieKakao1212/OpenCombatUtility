package com.LubieKakao1212.opencu.registry;

import com.LubieKakao1212.opencu.OpenCUConfigCommon;
import com.LubieKakao1212.opencu.common.device.*;

public class CUFramedDevices {

    public static final IFramedDevice REPULSOR = new RepulsorDevice();

    public static final IFramedDevice VANILLA_DROPPER = new DispenserConstant(
            CUDispensers.VANILLA_DROPPER,
            OpenCUConfigCommon.vanillaDispenserDevice()
    );

    public static final IFramedDevice VANILLA_DISPENSER = new DispenserConstant(
            CUDispensers.VANILLA_DISPENSER,
            OpenCUConfigCommon.vanillaDispenserDevice()
    );

    public static final IFramedDevice GOLD_DISPENSER = new DispenserConstant(
            CUDispensers.VANILLA_DISPENSER,
            OpenCUConfigCommon.goldenDispenserDevice()
    );

    public static final IFramedDevice DIAMOND_DISPENSER = new DispenserConstant(
            CUDispensers.VANILLA_DISPENSER,
            OpenCUConfigCommon.diamondDispenserDevice()
    );

    public static final IFramedDevice NETHERITE_DISPENSER = new DispenserConstant(
            CUDispensers.VANILLA_DISPENSER,
            OpenCUConfigCommon.netheriteDispenserDevice()
    );


    public static final IFramedDevice SIMPLE_TRACKER = new TrackerBase(OpenCUConfigCommon.trackerDevice());
    public static final IFramedDevice ARRAY_CONTROLLER = new ArrayControllerDevice();

}
