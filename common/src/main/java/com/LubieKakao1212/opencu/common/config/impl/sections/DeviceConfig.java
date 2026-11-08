package com.LubieKakao1212.opencu.common.config.impl.sections;

import com.LubieKakao1212.opencu.common.config.api.base.ICapacitorConfig;
import com.LubieKakao1212.opencu.common.config.api.base.IDeviceConfig;
import me.fzzyhmstrs.fzzy_config.config.ConfigSection;
import me.fzzyhmstrs.fzzy_config.validation.number.ValidatedDouble;
import me.fzzyhmstrs.fzzy_config.validation.number.ValidatedNumber;

public class DeviceConfig extends ConfigSection implements IDeviceConfig {

    public CapacitorConfig energy;
    public ValidatedDouble rotationSpeed;

    public DeviceConfig(IDeviceConfig defaults) {
        energy = new CapacitorConfig(defaults.energy());
        rotationSpeed = ValidatedNumber.withIncrement(new ValidatedDouble(defaults.rotationSpeed(), 180 * 20, 0.0, ValidatedNumber.WidgetType.TEXTBOX_WITH_BUTTONS), 1.0);
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
