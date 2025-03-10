package com.LubieKakao1212.opencu.common.device;

import com.LubieKakao1212.opencu.OpenCUConfigCommon;
import com.LubieKakao1212.opencu.common.device.state.IDeviceState;
import com.LubieKakao1212.opencu.common.device.state.ShooterDeviceState;

public class DispenserConstant extends ShooterBase {

    private final double constantSpread;
    private final double constantForce;
    private final double baseEnergy;
    private final double basePower;

    private final boolean energyEnabled;

    public DispenserConstant(ShotMappings mappings, OpenCUConfigCommon.DispenserDeviceConfig config) {
        this(mappings, (float)config.rotationSpeed() / 20.0f, config.spread(), config.force(), config.baseEnergy(), config.power(), config.energy().isEnergyEnabled());
    }

    public DispenserConstant(ShotMappings mappings, float alignmentSpeed, double constantSpread, double constantForce, double baseEnergy, double power, boolean energyEnabled) {
        super(mappings, alignmentSpeed);
        this.constantForce = constantForce;
        this.constantSpread = constantSpread;
        this.baseEnergy = baseEnergy;
        this.basePower = power;
        this.energyEnabled = energyEnabled;
    }

    public DispenserConstant(ShotMappings mappings, float alignmentSpeed, double constantSpread, double constantForce, double baseEnergy, boolean energyEnabled) {
        this(mappings, alignmentSpeed, constantSpread, constantForce, baseEnergy, 1.0, energyEnabled);
    }


    @Override
    public IDeviceState getNewState() {
        return new ShooterDeviceState(constantForce, constantSpread, baseEnergy, basePower);
    }

    @Override
    public boolean energyEnabled() {
        return energyEnabled;
    }
}
