package com.LubieKakao1212.opencu.common.config.api;

import com.LubieKakao1212.opencu.common.config.api.base.ICapacitorConfig;
import com.LubieKakao1212.opencu.common.config.api.base.IDeviceConfig;

public interface ITrackerDeviceConfig extends IDeviceConfig {

    double range();
    double energyPerTick();
    double energyPerActiveConnectionPerTick();

    @Override
    default ICapacitorConfig energy() {
        return new ICapacitorConfig() {
            @Override
            public int energyCapacity() {
                return 0;
            }

            @Override
            public boolean energyEnabledInternal() {
                return false;
            }
        };
    }
}
