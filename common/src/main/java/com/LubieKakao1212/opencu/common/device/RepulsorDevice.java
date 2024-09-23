package com.LubieKakao1212.opencu.common.device;

import com.LubieKakao1212.opencu.OpenCUConfigCommon;
import com.LubieKakao1212.opencu.common.device.state.IDeviceState;
import com.LubieKakao1212.opencu.common.device.state.RepulsorDeviceState;
import com.LubieKakao1212.opencu.common.pulse.EntityPulseType;
import com.LubieKakao1212.opencu.common.pulse.PulseData;
import com.LubieKakao1212.opencu.common.transaction.DeviceActivationContext;
import com.lubiekakao1212.qulib.math.Aim;
import com.lubiekakao1212.qulib.math.extensions.Vector3dExtensions;
import com.lubiekakao1212.qulib.math.mc.Vector3m;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.joml.Vector3d;

public class RepulsorDevice implements IFramedDevice {


    /**
     * @param ctx       used to fetch ammo, use energy, and add leftovers
     */
    @Override
    public void activate(IDeviceContainer container, IDeviceState state, World world, BlockPos pos, Aim aim, DeviceActivationContext ctx) {
        OpenCUConfigCommon.RepulsorDeviceConfig config = OpenCUConfigCommon.repulsorDevice();

        //TODO remove distance cost from config and registry

        double maxRadius = config.maxRadius();

        Vector3d pulseOrigin = new Vector3m(pos.toCenterPos());

        var repState = (RepulsorDeviceState)state;
        var pulseData = repState.getPulseData();
        var pulseType = repState.getPulseType();

        double radius = pulseData.radius;
        double volumeRatio = (radius * radius * radius) / (maxRadius * maxRadius * maxRadius);
        double forceRatio = Math.abs(pulseData.force);

        EntityPulseType.EnergyUsage energyUsageMul = pulseType.getEnergyUsage();
        int energyUsage = (int)Math.floor(
                volumeRatio * forceRatio * config.powerCost() * energyUsageMul.fromPower);

        if(ctx.energy().useEnergy(energyUsage, ctx.ctx()) == energyUsage) {
            var dir = aim.toQuaternion().transform(Vector3dExtensions.INSTANCE.getNORTH());
            pulseType.executePulse(world, pulseOrigin, new PulseData.Directional(pulseData, dir));
            ctx.ctx().commit();
        }
    }

    @Override
    public void tick(IDeviceContainer container, IDeviceState state, World world, BlockPos pos, Aim aim, DeviceActivationContext ctx) {
        //empty
    }

    @Override
    public double getPitchAlignmentSpeed() {
        return 15;
    }

    @Override
    public double getYawAlignmentSpeed() {
        return 15;
    }

    @Override
    public IDeviceState getNewState() {
        return new RepulsorDeviceState();
    }
}
