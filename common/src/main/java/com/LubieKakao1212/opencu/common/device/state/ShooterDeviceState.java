package com.LubieKakao1212.opencu.common.device.state;

import com.LubieKakao1212.opencu.common.network.Sender;
import com.LubieKakao1212.opencu.common.peripheral.device.IDeviceApi;
import com.LubieKakao1212.opencu.common.peripheral.device.ShooterDeviceApi;
import com.LubieKakao1212.opencu.common.util.Lazy;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.BlockPos;

public class ShooterDeviceState extends DeviceStateBase {

    private final Lazy<ShooterDeviceApi> api;

    private double force;
    private double spread;
    private double power;
    private double baseEnergyUsage;

    public ShooterDeviceState(double force, double spread, double baseEnergyUsage, double power, Runnable markDirtyDelegate) {
        super(markDirtyDelegate);
        api = new Lazy<>(() -> new ShooterDeviceApi(this));
        this.force = force;
        this.spread = spread;
        this.baseEnergyUsage = baseEnergyUsage;
        this.power = power;
    }

    /**
     * Returns a cc api for this state instance.
     * Do not call unless CC:Tweaked is present
     *
     * @return
     */
    @Override
    public IDeviceApi getApi() {
        return api.getValue();
    }

    public double getSpread() {
        return spread;
    }

    public double getForce() {
        return force;
    }

    public double getBaseEnergyUsage() {
        return baseEnergyUsage;
    }

    public double getPower() {
        return power;
    }

    @Override
    public NbtCompound serialize() {
        return new NbtCompound();
    }

    @Override
    public void deserialize(NbtCompound nbt) {

    }

    @Override
    public void forceSync(Sender packetSender, BlockPos pos) {
        //TODO
    }
}
