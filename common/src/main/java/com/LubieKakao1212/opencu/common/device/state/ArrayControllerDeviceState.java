package com.LubieKakao1212.opencu.common.device.state;

import com.LubieKakao1212.opencu.common.network.Sender;
import com.LubieKakao1212.opencu.common.peripheral.device.IDeviceApi;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.NotNull;

public class ArrayControllerDeviceState extends DeviceStateBase {

    public ArrayControllerDeviceState(@NotNull Runnable markDirtyDelegate) {
        super(markDirtyDelegate);
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
