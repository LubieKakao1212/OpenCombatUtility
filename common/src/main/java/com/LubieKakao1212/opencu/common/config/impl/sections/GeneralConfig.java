package com.LubieKakao1212.opencu.common.config.impl.sections;

import com.LubieKakao1212.opencu.common.config.api.IGeneralConfig;
import com.LubieKakao1212.opencu.common.config.api.base.IEnergyToggle;
import com.LubieKakao1212.opencu.common.config.impl.MasterEnergyToggle;
import me.fzzyhmstrs.fzzy_config.config.ConfigSection;

public class GeneralConfig extends ConfigSection implements IGeneralConfig {

    public boolean enableEnergy;

    public GeneralConfig(IGeneralConfig defaults) {
        enableEnergy = defaults.energy().energyEnabledInternal();
    }

    private MasterEnergyToggle cached = null;

    @Override
    public IEnergyToggle energy() {
        if(cached == null) {
            cached = new MasterEnergyToggle();
            cached.enabled = enableEnergy;
        }
        return cached;
    }
}
