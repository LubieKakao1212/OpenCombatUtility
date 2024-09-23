package com.LubieKakao1212.opencu.common.device.state;

import com.LubieKakao1212.opencu.common.device.RepulsorDevice;
import com.LubieKakao1212.opencu.common.peripheral.device.IDeviceApi;
import com.LubieKakao1212.opencu.common.pulse.EntityPulseType;
import com.LubieKakao1212.opencu.common.pulse.PulseData;
import com.LubieKakao1212.opencu.registry.CUPulse;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.util.Identifier;

public class RepulsorDeviceState extends DeviceStateBase {

    private EntityPulseType pulseType = CUPulse.defaultPulse();
    private final PulseData pulseData = new PulseData();

    public RepulsorDeviceState() {
        pulseData.radius = 3.0;
        pulseData.force = 0.5;
    }

    /**
     * Returns a cc api for this state instance.
     * Do not call unless CC:Tweaked is present
     *
     * @return
     */
    @Override
    public IDeviceApi getApi() {
        //TODO
        return null;
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

    public void setPulseType(EntityPulseType pulseType) {
        this.pulseType = pulseType;
    }

    public EntityPulseType getPulseType() {
        return pulseType;
    }

    public PulseData getPulseData() {
        return pulseData;
    }
}
