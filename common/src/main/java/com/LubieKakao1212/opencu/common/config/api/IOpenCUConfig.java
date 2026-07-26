package com.LubieKakao1212.opencu.common.config.api;

import com.LubieKakao1212.opencu.common.config.impl.OpenCUConfig;
import me.fzzyhmstrs.fzzy_config.api.ConfigApiJava;

public interface IOpenCUConfig {

    IOpenCUConfig CONFIG = ConfigApiJava.registerAndLoadConfig(OpenCUConfig::new);

    IGeneralConfig general();

    IModularFrameConfig modularFrame();

    IRepulsorDeviceConfig repulsorDevice();

    IDispenserDeviceConfig vanillaDispenserDevice();
    IDispenserDeviceConfig goldenDispenserDevice();
    IDispenserDeviceConfig diamondDispenserDevice();
    IDispenserDeviceConfig netheriteDispenserDevice();

    ITrackerDeviceConfig trackerDevice();

}
