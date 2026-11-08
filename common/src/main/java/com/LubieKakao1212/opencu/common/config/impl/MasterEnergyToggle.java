package com.LubieKakao1212.opencu.common.config.impl;

import com.LubieKakao1212.opencu.common.config.api.base.IEnergyToggle;
import me.fzzyhmstrs.fzzy_config.config.ConfigSection;

//TODO change type name to MasterEnergy while adding global multipliers for capacity and usage
public class MasterEnergyToggle extends ConfigSection implements IEnergyToggle {

    @Desc("Is energy usage enabled for anything, takes priority over more specific toggles")
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
