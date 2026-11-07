package com.LubieKakao1212.opencu.common.config.impl.sections;

import com.LubieKakao1212.opencu.common.config.api.base.ICapacitorConfig;
import com.LubieKakao1212.opencu.common.config.api.base.IDeviceConfig;
import me.fzzyhmstrs.fzzy_config.config.ConfigSection;
import me.fzzyhmstrs.fzzy_config.validation.number.ValidatedDouble;

public class DeviceConfig extends ConfigSection implements IDeviceConfig {

    public CapacitorConfig energy;
    public ValidatedDouble rotationSpeed;

    public DeviceConfig(IDeviceConfig defaults) {
        energy = new CapacitorConfig(defaults.energy());
        rotationSpeed = new ValidatedDouble(defaults.rotationSpeed(), 180 * 20, 0.0);
    }

    @Override
    public ICapacitorConfig energy() {
        return energy;
    }

    @Override
    public double rotationSpeed() {
        return rotationSpeed.get();
    }
}
