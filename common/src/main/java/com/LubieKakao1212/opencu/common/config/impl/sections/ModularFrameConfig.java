package com.LubieKakao1212.opencu.common.config.impl.sections;

import com.LubieKakao1212.opencu.common.config.api.IModularFrameConfig;
import com.LubieKakao1212.opencu.common.config.api.base.ICapacitorConfig;
import me.fzzyhmstrs.fzzy_config.annotations.Action;
import me.fzzyhmstrs.fzzy_config.annotations.RequiresAction;
import me.fzzyhmstrs.fzzy_config.config.ConfigSection;

@RequiresAction(action = Action.RESTART)
public class ModularFrameConfig extends ConfigSection implements IModularFrameConfig {

    @Desc("Energy capacity and toggle")
    public CapacitorConfig energy;

    public ModularFrameConfig(IModularFrameConfig defaults) {
        this.energy = new CapacitorConfig(defaults.energy());
    }

    @Override
    public ICapacitorConfig energy() {
        return energy;
    }

}
