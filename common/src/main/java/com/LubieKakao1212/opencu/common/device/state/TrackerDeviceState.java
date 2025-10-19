package com.LubieKakao1212.opencu.common.device.state;

import com.LubieKakao1212.opencu.common.network.Sender;
import com.LubieKakao1212.opencu.common.peripheral.device.IDeviceApi;
import com.LubieKakao1212.opencu.common.util.Pulser;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.BlockPos;

public class TrackerDeviceState extends DeviceStateBase {

    private double trackingRange;
    private double energyPerTick;
    private double energyPerActiveConnectionPerTick;

    public final Pulser rsPulseTimer;
    public double energyLeftover;
    public boolean noEnergy;

    public TrackerDeviceState(double trackingRange, double energyPerTick, double energyPerActiveConnectionPerTick, Runnable markDirtyDelegate) {
        super(markDirtyDelegate);
        this.trackingRange = trackingRange;
        this.energyPerTick = energyPerTick;
        this.energyPerActiveConnectionPerTick = energyPerActiveConnectionPerTick;
        this.energyLeftover = 0;
        this.noEnergy = false;
        this.rsPulseTimer = new Pulser(2);
    }

    /**
     * Returns a cc api for this state instance.
     * Do not call unless CC:Tweaked is present
     *
     * @return
     */
    @Override
    public IDeviceApi getApi() {
        return null;
    }

    @Override
    public NbtCompound serialize() {
        var nbt = new NbtCompound();

        return nbt;
    }

    @Override
    public void deserialize(NbtCompound nbt) {

    }

    @Override
    public void forceSync(Sender packetSender, BlockPos pos) {
        //TODO
    }

    public double getTrackingRange() {
        return trackingRange;
    }

    public double getEnergyPerTick() {
        return energyPerTick;
    }

    public double getEnergyPerActiveConnectionPerTick() {
        return energyPerActiveConnectionPerTick;
    }
}
