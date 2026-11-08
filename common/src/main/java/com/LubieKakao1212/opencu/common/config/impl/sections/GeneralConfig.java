package com.LubieKakao1212.opencu.common.config.impl.sections;

import com.LubieKakao1212.opencu.common.config.api.IGeneralConfig;
import com.LubieKakao1212.opencu.common.config.api.base.IEnergyToggle;
import com.LubieKakao1212.opencu.common.config.impl.MasterEnergyToggle;
import me.fzzyhmstrs.fzzy_config.annotations.Action;
import me.fzzyhmstrs.fzzy_config.annotations.RequiresAction;
import me.fzzyhmstrs.fzzy_config.config.ConfigSection;

@RequiresAction(action = Action.RESTART)
public class GeneralConfig extends ConfigSection implements IGeneralConfig {

    public GeneralConfig(IGeneralConfig defaults) {
        energy = new MasterEnergyToggle();
        energy.enabled = defaults.energy().energyEnabledInternal();
    }

    @Desc("Global energy values/modifiers for all devices in the mod")
    public MasterEnergyToggle energy;

    @Override
    public IEnergyToggle energy() {
        return energy;
    }
}
