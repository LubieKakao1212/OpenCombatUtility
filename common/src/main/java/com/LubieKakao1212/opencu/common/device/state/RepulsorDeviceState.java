package com.LubieKakao1212.opencu.common.device.state;

import com.LubieKakao1212.opencu.NetworkUtil;
import com.LubieKakao1212.opencu.OpenCUConfigCommon;
import com.LubieKakao1212.opencu.common.network.packet.device.repulsor.PacketS2CUpdateRepulsorBlend;
import com.LubieKakao1212.opencu.common.peripheral.device.IDeviceApi;
import com.LubieKakao1212.opencu.common.peripheral.device.RepulsorDeviceApi;
import com.LubieKakao1212.opencu.common.pulse.EntityPulseType;
import com.LubieKakao1212.opencu.common.pulse.PulseData;
import com.LubieKakao1212.opencu.common.util.Lazy;
import com.LubieKakao1212.opencu.common.util.SyncDouble;
import com.LubieKakao1212.opencu.registry.CUPulse;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class RepulsorDeviceState extends DeviceStateBase {

    private final Lazy<RepulsorDeviceApi> api;
    private EntityPulseType pulseType = CUPulse.defaultPulse();
    private final PulseData pulseData = new PulseData();
    private final OpenCUConfigCommon.RepulsorDeviceConfig config;
    private final SyncDouble blendSync;

    //region Client Fields
    /**
     * Client field
     */
    private long lastActivationTimestamp = 0;
    //endregion

    public RepulsorDeviceState(OpenCUConfigCommon.RepulsorDeviceConfig config) {
        this.config = config;
        //TODO set from config
        pulseData.radius = 3.0;
        pulseData.force = 0.5;
        pulseData.directionBlend = 0.5f;
        api = new Lazy<>(() -> new RepulsorDeviceApi(this));
        this.blendSync = new SyncDouble(-100, 1f / 256f, () -> pulseData.directionBlend);
    }

    /**
     * Returns a cc api for this state instance.
     * Do not call unless CC:Tweaked is present
     */
    @Override
    public IDeviceApi getApi() {
        return api.getValue();
    }

    @Override
    public NbtCompound serialize() {

        var nbtOut = new NbtCompound();

        NbtCompound pulseTag = pulseData.serialize();
        pulseTag.putString("type", pulseType.getRegistryKey().toString());
        nbtOut.put("pulse", pulseTag);

        return nbtOut;
    }

    @Override
    public void deserialize(NbtCompound nbt) {
        if(nbt.contains("pulse", NbtElement.COMPOUND_TYPE)) {
            NbtCompound pulseTag = nbt.getCompound("pulse");

            setPulseType(
                    EntityPulseType.getOrThrow(
                            new Identifier(pulseTag.getString("type"))
                    )
            );
            pulseData.deserialize(pulseTag);
        }
    }

    public void sync(World world, BlockPos pos) {
        blendSync.sync(
            (value) -> {
                NetworkUtil.sendToAllTracking(new PacketS2CUpdateRepulsorBlend(pos, (float)value.doubleValue()), (ServerWorld) world, pos);
            }
        );
    }

    public void setPulseType(EntityPulseType pulseType) {
        this.pulseType = pulseType;
    }

    public EntityPulseType getPulseType() {
        return pulseType;
    }

    public PulseData getPulseData() {
        return pulseData;
    }

    public int getEnergyUsage() {
        double maxRadius = config.maxRadius();

        double radius = pulseData.radius;
        double volumeRatio = (radius * radius * radius) / (maxRadius * maxRadius * maxRadius);
        double forceRatio = Math.abs(pulseData.force);

        EntityPulseType.EnergyUsage energyUsageMul = pulseType.getEnergyUsage();
        return (int)Math.floor(
                volumeRatio * forceRatio * config.powerCost() * energyUsageMul.fromPower);
    }

    public double setForce(double force) {
        if(Math.abs(force) > 1) {
            force = Math.signum(force);
        }
        pulseData.force = force;
        return force;
    }

    public double getForce() {
        return pulseData.force;
    }

    public double setRadius(double radius) {
        if(radius < 0) {
            radius = 0;
        }else if(radius > config.maxRadius()) {
            radius = config.maxRadius();
        }
        pulseData.radius = radius;
        return radius;
    }

    public double getRadius() {
        return pulseData.radius;
    }

    public double getMaxRadius() {
        return config.maxRadius();
    }

    public double setDirectionBlend(double blend) {
        if(blend < 0) {
            blend = 0;
        }else if(blend > 1) {
            blend = 1;
        }
        pulseData.directionBlend = blend;
        return blend;
    }

    public double getDirectionBlend() {
        return pulseData.directionBlend;
    }

    //region Client Methods

    /**
     * Client Method
     */
    public long getLastActivationTimestamp() {
        return lastActivationTimestamp;
    }

    /**
     * Client Method
     */
    public void setLastActivationTimestamp(long lastActivationTimestamp) {
        this.lastActivationTimestamp = lastActivationTimestamp;
    }

    //endregion
}
