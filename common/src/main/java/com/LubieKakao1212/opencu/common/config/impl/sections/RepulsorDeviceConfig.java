package com.LubieKakao1212.opencu.common.config.impl.sections;

import com.LubieKakao1212.opencu.common.config.api.IRepulsorDeviceConfig;
import me.fzzyhmstrs.fzzy_config.annotations.Action;
import me.fzzyhmstrs.fzzy_config.annotations.RequiresAction;
import me.fzzyhmstrs.fzzy_config.validation.number.ValidatedDouble;
import me.fzzyhmstrs.fzzy_config.validation.number.ValidatedNumber;

@RequiresAction(action = Action.RESTART)
public class RepulsorDeviceConfig extends DeviceConfig implements IRepulsorDeviceConfig {

    @Desc("Maximum pulse radius")
    public ValidatedDouble range;

    @Desc("Maximum pulse force")
    public ValidatedDouble force;

    @Desc("Energy usage at maximum range and maximum force")
    public ValidatedDouble energyUsage;

    public RepulsorDeviceConfig(IRepulsorDeviceConfig defaults) {
        super(defaults);
        range = ValidatedDouble.withIncrement(new ValidatedDouble(defaults.maxRadius(), Double.MAX_VALUE, 0.0, ValidatedNumber.WidgetType.TEXTBOX_WITH_BUTTONS), 0.5);
        force = new ValidatedDouble(defaults.forceScale(), Double.MAX_VALUE, 0.0, ValidatedNumber.WidgetType.TEXTBOX);
        energyUsage = new ValidatedDouble(defaults.powerCost(), Double.MAX_VALUE, 0.0, ValidatedNumber.WidgetType.TEXTBOX);
    }

    @Override
    public double maxRadius() {
        return range.get();
    }

    @Override
    public double forceScale() {
        return force.get();
    }

    @Override
    public double powerCost() {
        return energyUsage.get();
    }
}
