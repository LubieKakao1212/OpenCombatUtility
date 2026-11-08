package com.LubieKakao1212.opencu.common.config.impl.sections;

import com.LubieKakao1212.opencu.common.config.api.IDispenserDeviceConfig;
import me.fzzyhmstrs.fzzy_config.annotations.Action;
import me.fzzyhmstrs.fzzy_config.annotations.RequiresAction;
import me.fzzyhmstrs.fzzy_config.validation.number.ValidatedDouble;
import me.fzzyhmstrs.fzzy_config.validation.number.ValidatedNumber;

@RequiresAction(action = Action.RESTART)
public class DispenserDeviceConfig extends DeviceConfig implements IDispenserDeviceConfig {

    @Desc("Minimum (Default) spread for this dispenser")
    public ValidatedDouble spread;

    @Desc("Maximum (Default) force applied to dispensed objects")
    public ValidatedDouble force;

    @Desc("Base Energy per Shot, multiplied by per item cost multipliers")
    public ValidatedDouble energyPerShot;

    @Desc("Affects damage of dispensed arrows similar to the \"Power\" enchant\nDamage multiplier (I think)")
    public ValidatedDouble power;

    public DispenserDeviceConfig(IDispenserDeviceConfig defaults) {
        super(defaults);
        spread = ValidatedDouble.withIncrement(new ValidatedDouble(defaults.spread(), 360, 0, ValidatedNumber.WidgetType.TEXTBOX_WITH_BUTTONS), 1.0);
        force = new ValidatedDouble(defaults.force(), Double.MAX_VALUE, 0.05);
        energyPerShot = new ValidatedDouble(defaults.baseEnergy(), Double.MAX_VALUE, 0);
        power = new ValidatedDouble(defaults.power(), Double.MAX_VALUE, 0);
    }

    @Override
    public double spread() {
        return spread.get();
    }

    @Override
    public double force() {
        return force.get();
    }

    @Override
    public double baseEnergy() {
        return energyPerShot.get();
    }

    @Override
    public double power() {
        return power.get();
    }

    @Override
    public double maxSpread() {
        return 360; //TODO? Consider un-hardcoding
    }
}
