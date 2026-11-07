package com.LubieKakao1212.opencu.common.config.impl.sections;

import com.LubieKakao1212.opencu.common.config.api.IGeneralConfig;
import com.LubieKakao1212.opencu.common.config.api.base.IEnergyToggle;
import com.LubieKakao1212.opencu.common.config.impl.MasterEnergyToggle;
import me.fzzyhmstrs.fzzy_config.config.ConfigSection;

public class GeneralConfig extends ConfigSection implements IGeneralConfig {

    public GeneralConfig(IGeneralConfig defaults) {
        energy = new MasterEnergyToggle();
        energy.enabled = defaults.energy().energyEnabledInternal();
    }

    public MasterEnergyToggle energy;

    @Override
    public IEnergyToggle energy() {
        return energy;
    }
}
