package com.LubieKakao1212.opencu.common.config.impl;

import com.LubieKakao1212.opencu.common.config.api.base.IEnergyToggle;

public class MasterEnergyToggle implements IEnergyToggle {

    public boolean enabled = true;

    @Override
    public boolean isEnergyEnabled() {
        return energyEnabledInternal();
    }

    @Override
    public boolean energyEnabledInternal() {
        return enabled;
    }

}
