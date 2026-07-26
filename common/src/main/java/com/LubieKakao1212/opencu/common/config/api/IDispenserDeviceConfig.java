package com.LubieKakao1212.opencu.common.config.api;

import com.LubieKakao1212.opencu.common.config.api.base.IDeviceConfig;

public interface IDispenserDeviceConfig extends IDeviceConfig {

    double spread();
    double force();
    double baseEnergy();
    double power();

    /**
     * Use only for configurable dispensers
     */
    double maxSpread();

}
