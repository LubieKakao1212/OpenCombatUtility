package com.LubieKakao1212.opencu.common.device.state;

import com.LubieKakao1212.opencu.NetworkUtil;
import com.LubieKakao1212.opencu.OpenCUConfigCommon;
import com.LubieKakao1212.opencu.common.network.Sender;
import com.LubieKakao1212.opencu.common.network.packet.device.repulsor.PacketS2CUpdatePulseType;
import com.LubieKakao1212.opencu.common.network.packet.device.repulsor.PacketS2CUpdateRepulsorProperty;
import com.LubieKakao1212.opencu.common.peripheral.device.IDeviceApi;
import com.LubieKakao1212.opencu.common.peripheral.device.RepulsorDeviceApi;
import com.LubieKakao1212.opencu.common.pulse.EntityPulseType;
import com.LubieKakao1212.opencu.common.pulse.PulseData;
import com.LubieKakao1212.opencu.common.util.Lazy;
import com.LubieKakao1212.opencu.common.util.Observer;
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
    private final Observer<Identifier> typeObserver;
    private final Observer<Double> forceObserver;
    private final Observer<Double> radiusObserver;

    //region Client Fields
    /**
     * Client field
     */
    private long lastActivationTimestamp = 0;

    public int selectedType;
    //endregion

    public RepulsorDeviceState(OpenCUConfigCommon.RepulsorDeviceConfig config, Runnable markDirtyDelegate) {
        super(markDirtyDelegate);
        this.config = config;
        //TODO set from config
        pulseData.radius = 3.0;
        pulseData.force = 0.5;
        api = new Lazy<>(() -> new RepulsorDeviceApi(this));
        typeObserver = Observer.generic(() -> pulseType.getRegistryKey());
        forceObserver = Observer.numeric(this::getForce, 1f / 256f);
        radiusObserver = Observer.numeric(this::getRadius, 1f / 256f);
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

    @Override
    public void forceSync(Sender packetSender, BlockPos pos) {
        sendTypeId(packetSender, pos);
        sendProperty(packetSender, pos, Property.Force);
        sendProperty(packetSender, pos, Property.Radius);
    }

    public void sync(Sender packetSender, BlockPos pos) {
        typeObserver.update((value) -> sendTypeId(packetSender, pos));
        forceObserver.update((value) -> sendProperty(packetSender, pos, Property.Force));
        radiusObserver.update((value) -> sendProperty(packetSender, pos, Property.Radius));
    }

    public void setPulseType(EntityPulseType pulseType) {
        this.pulseType = pulseType;
        markDirty();
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
        markDirty();
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
        markDirty();
        return radius;
    }

    public double getRadius() {
        return pulseData.radius;
    }

    public double getMaxRadius() {
        return config.maxRadius();
    }

    public void setProperty(Property property, double value) {
        switch (property) {
            case Force -> setForce(value);
            case Radius -> setRadius(value);
        }
    }

    public double getProperty(Property property) {
        return switch (property) {
            case Force -> getForce();
            case Radius -> getRadius();
        };
    }

    public void setPropertyNormal(Property property, double valueNorm) {
        setProperty(property, switch (property) {
            case Force -> (valueNorm * 2.) - 1.;
            case Radius -> valueNorm * getMaxRadius();
        });
    }

    public double getPropertyNormal(Property property) {
        var prop = getProperty(property);
        return switch (property) {
            case Force -> (prop + 1.) / 2.;
            case Radius -> prop / getMaxRadius();
        };
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

    private void sendProperty(Sender packetSender, BlockPos pos, Property property) {
        packetSender.send(new PacketS2CUpdateRepulsorProperty(pos, property, (float)getPropertyNormal(property)));
    }

    private void sendTypeId(Sender packetSender, BlockPos pos) {
        packetSender.send(new PacketS2CUpdatePulseType(pos, config.pulseTypesOrdinal().indexOf(pulseType.getRegistryKey())));//TODO
    }

    public enum Property {
        Force,
        Radius
    }
}
