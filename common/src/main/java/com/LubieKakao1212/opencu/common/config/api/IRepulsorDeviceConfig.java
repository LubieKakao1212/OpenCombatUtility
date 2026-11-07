package com.LubieKakao1212.opencu.common.config.api;

import com.LubieKakao1212.opencu.common.config.api.base.IDeviceConfig;

public interface IRepulsorDeviceConfig extends IDeviceConfig {

    double maxRadius();
    double forceScale();
    double powerCost();

}
