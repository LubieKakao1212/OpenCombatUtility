package com.LubieKakao1212.opencu.common.device;

import com.LubieKakao1212.opencu.common.config.api.IDispenserDeviceConfig;
import com.LubieKakao1212.opencu.common.device.state.IDeviceState;
import com.LubieKakao1212.opencu.common.device.state.ShooterDeviceState;
import org.jetbrains.annotations.NotNull;

public class DispenserConstant extends ShooterBase {

    public DispenserConstant(IDispenserDeviceConfig config, ShotMappings mappings) {
        super(config, mappings);
    }

    @Override
    public @NotNull IDeviceState getNewState(Runnable markDirtyDelegate) {
        return new ShooterDeviceState(config.force(), config.spread(), config.baseEnergy(), config.power(), markDirtyDelegate);
    }
}
