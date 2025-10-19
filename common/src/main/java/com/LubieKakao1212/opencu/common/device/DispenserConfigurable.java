package com.LubieKakao1212.opencu.common.device;

import com.LubieKakao1212.opencu.common.device.state.IDeviceState;
import com.LubieKakao1212.opencu.common.device.state.ShooterDeviceState;
import org.jetbrains.annotations.NotNull;

public class DispenserConfigurable extends ShooterBase {

    private final double minSpread;
    private final double maxSpread;
    private final double maxForce;
    private final double baseEnergy;

    public DispenserConfigurable(ShotMappings mappings, float alignmentSpeed, double minSpread, double maxSpread, double maxForce, double baseEnergy) {
        super(mappings, alignmentSpeed);
        this.minSpread = minSpread;
        this.maxSpread = maxSpread;
        this.maxForce = maxForce;
        this.baseEnergy = baseEnergy;
    }

    //TODO Not working
    @Override
    public @NotNull IDeviceState getNewState(Runnable markDirtyDelegate) {
        return new ShooterDeviceState(maxForce, minSpread, baseEnergy, -1.0, markDirtyDelegate);
    }


    @Override
    public boolean energyEnabled() {
        return true;
    }

}
