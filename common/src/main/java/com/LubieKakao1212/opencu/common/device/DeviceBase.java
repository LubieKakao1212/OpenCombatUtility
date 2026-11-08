package com.LubieKakao1212.opencu.common.device;

import com.LubieKakao1212.opencu.common.config.api.base.IDeviceConfig;
import org.jetbrains.annotations.NotNull;

public abstract class DeviceBase<TConfig extends IDeviceConfig> implements IFramedDevice {

    protected final TConfig config;

    /**
     *
     * @param config If this device cannot be used standalone (without modular frame), should be null
     */
    public DeviceBase(TConfig config) {
        this.config = config;
    }

    @Override
    public @NotNull IDeviceConfig getConfig() {
        return config;
    }

    @Override
    public double getPitchAlignmentSpeed() {
        return config.rotationSpeed();
    }

    @Override
    public double getYawAlignmentSpeed() {
        return config.rotationSpeed();
    }
}
