package com.LubieKakao1212.opencu.common.config.impl.sections;

import com.LubieKakao1212.opencu.common.config.api.ITrackerDeviceConfig;
import me.fzzyhmstrs.fzzy_config.annotations.Action;
import me.fzzyhmstrs.fzzy_config.annotations.RequiresAction;
import me.fzzyhmstrs.fzzy_config.config.ConfigSection;
import me.fzzyhmstrs.fzzy_config.validation.number.ValidatedDouble;
import me.fzzyhmstrs.fzzy_config.validation.number.ValidatedNumber;

@RequiresAction(action = Action.RESTART)
public class TrackerDeviceConfig extends ConfigSection implements ITrackerDeviceConfig {

    @Desc("Maximum tracking range of trackers")
    public ValidatedDouble range;

    @Desc("Passive Energy consumption per tick\nDoes not need to be an integer")
    public ValidatedDouble energyPerTick;

    @Desc("Energy consumption per tick per connected device while tracking an \nDoes not need to be an integer")
    public ValidatedDouble energyPerConnection;

    public ValidatedDouble rotationSpeed;

    public TrackerDeviceConfig(ITrackerDeviceConfig defaults) {
        range = new ValidatedDouble(defaults.range(), Double.MAX_VALUE, 0);
        energyPerTick = new ValidatedDouble(defaults.energyPerTick(), Double.MAX_VALUE, 0);
        energyPerConnection = new ValidatedDouble(defaults.energyPerActiveConnectionPerTick(), Double.MAX_VALUE, 0);

        rotationSpeed = ValidatedNumber.withIncrement(new ValidatedDouble(defaults.rotationSpeed(), 180 * 20, 0.0, ValidatedNumber.WidgetType.TEXTBOX_WITH_BUTTONS), 1.0);
    }

    @Override
    public double range() {
        return range.get();
    }

    @Override
    public double energyPerTick() {
        return energyPerTick.get();
    }

    @Override
    public double energyPerActiveConnectionPerTick() {
        return energyPerConnection.get();
    }

    @Override
    public double rotationSpeed() {
        return rotationSpeed.get();
    }
}
