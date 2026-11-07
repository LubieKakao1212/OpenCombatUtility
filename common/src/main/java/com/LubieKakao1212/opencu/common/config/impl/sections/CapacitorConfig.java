package com.LubieKakao1212.opencu.common.config.impl.sections;

import com.LubieKakao1212.opencu.common.config.api.base.ICapacitorConfig;
import me.fzzyhmstrs.fzzy_config.config.ConfigSection;

public class CapacitorConfig extends ConfigSection implements ICapacitorConfig {

    @Desc("Energy capacity, if enabled if false this value will be ignored")
    public int energyCapacity;

    @Desc("Is energy usage and storage enabled for this context")
    public boolean enabled;

    public CapacitorConfig(ICapacitorConfig defaults) {
        energyCapacity = defaults.energyCapacity();
        enabled = defaults.energyEnabledInternal();
    }

    @Override
    public int energyCapacity() {
        return energyCapacity;
    }

    @Override
    public boolean energyEnabledInternal() {
        return enabled;
    }
}
