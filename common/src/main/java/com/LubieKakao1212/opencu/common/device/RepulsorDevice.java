package com.LubieKakao1212.opencu.common.device;

import com.LubieKakao1212.opencu.NetworkUtil;
import com.LubieKakao1212.opencu.OpenCUConfigCommon;
import com.LubieKakao1212.opencu.common.device.state.IDeviceState;
import com.LubieKakao1212.opencu.common.device.state.RepulsorDeviceState;
import com.LubieKakao1212.opencu.common.network.packet.device.repulsor.PacketS2CRepulsorActivationTimestamp;
import com.LubieKakao1212.opencu.common.pulse.PulseData;
import com.LubieKakao1212.opencu.common.screen.tabs.DeviceContainerScreenTab;
import com.LubieKakao1212.opencu.common.screen.tabs.RepulsorTab;
import com.LubieKakao1212.opencu.common.transaction.DeviceActivationContext;
import com.lubiekakao1212.qulib.math.mc.Vector3m;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3d;

public class RepulsorDevice implements IFramedDevice {


    /**
     * @param ctx used to fetch ammo, use energy, and add leftovers
     */
    @Override
    public void activate(IDeviceContainer container, IDeviceState state, World world, BlockPos pos, Vector3d aimForward, DeviceActivationContext ctx) {
        OpenCUConfigCommon.RepulsorDeviceConfig config = OpenCUConfigCommon.repulsorDevice();

        //TODO remove distance cost from config and registry

        Vector3d pulseOrigin = new Vector3m(pos.toCenterPos());

        var repState = (RepulsorDeviceState)state;
        var pulseData = repState.getPulseData();
        var pulseType = repState.getPulseType();

        var energyUsage = repState.getEnergyUsage();

        if(ctx.energy().useEnergy(energyUsage, ctx.ctx()) == energyUsage) {
            var dir = aimForward;
            pulseType.executePulse(world, pulseOrigin, new PulseData.Directional(pulseData, dir));

            //Update visuals
            NetworkUtil.sendToAllTracking(new PacketS2CRepulsorActivationTimestamp(pos, world.getTime()), (ServerWorld) world, pos);

            ctx.ctx().commit();
        }
    }

    @Override
    public void tick(IDeviceContainer container, IDeviceState state, World world, BlockPos pos, Vector3d aimForward, DeviceActivationContext ctx) {
        //server only
        ((RepulsorDeviceState) state).sync(world, pos);
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
    public @NotNull IDeviceState getNewState() {
        //TODO fetch config
        return new RepulsorDeviceState(OpenCUConfigCommon.repulsorDevice());
    }

    @Override
    public boolean ammoEnabled() {
        return false;
    }

    @Override
    public boolean energyEnabled() {
        return OpenCUConfigCommon.repulsorDevice().energy().isEnergyEnabled();
    }

    /**
     * Client Method
     */
    @Override
    public @Nullable DeviceContainerScreenTab getScreenTab() {
        return new RepulsorTab();
    }


}
