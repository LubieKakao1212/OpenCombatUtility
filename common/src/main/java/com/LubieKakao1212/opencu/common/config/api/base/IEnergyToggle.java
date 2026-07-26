package com.LubieKakao1212.opencu.common.config.api.base;

import com.LubieKakao1212.opencu.common.config.api.IOpenCUConfig;
import org.jetbrains.annotations.ApiStatus;

public interface IEnergyToggle {

    default IEnergyToggle parent() {
        return IOpenCUConfig.CONFIG.general().energy();
    }

    default boolean isEnergyEnabled() {
        return parent().isEnergyEnabled() && energyEnabledInternal();
    }

    /**
     * Internal method. Use {@link #isEnergyEnabled()} instead
     */
    @ApiStatus.Internal
    boolean energyEnabledInternal();

}
