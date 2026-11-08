package com.LubieKakao1212.opencu.common.config.impl.sections;

import com.LubieKakao1212.opencu.common.config.api.base.ICapacitorConfig;
import me.fzzyhmstrs.fzzy_config.annotations.Action;
import me.fzzyhmstrs.fzzy_config.annotations.RequiresAction;
import me.fzzyhmstrs.fzzy_config.config.ConfigSection;
import me.fzzyhmstrs.fzzy_config.validation.number.ValidatedInt;

@RequiresAction(action = Action.RESTART)
public class CapacitorConfig extends ConfigSection implements ICapacitorConfig {

    @Desc("Energy capacity, if enabled if false this value will be ignored")
    public ValidatedInt energyCapacity;

    @Desc("Is energy usage and storage enabled for this context")
    public boolean enabled;

    public CapacitorConfig(ICapacitorConfig defaults) {
        energyCapacity = new ValidatedInt(defaults.energyCapacity(), Integer.MAX_VALUE, 0);
        enabled = defaults.energyEnabledInternal();
    }

    @Override
    public int energyCapacity() {
        return energyCapacity.get();
    }

    @Override
    public boolean energyEnabledInternal() {
        return enabled;
    }
}
