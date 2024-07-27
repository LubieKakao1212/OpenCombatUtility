package com.LubieKakao1212.opencu.common.block.entity;

import com.LubieKakao1212.opencu.common.device.IDeviceContainer;
import com.LubieKakao1212.opencu.common.device.IFramedDevice;
import com.LubieKakao1212.opencu.common.device.state.IDeviceState;
import com.LubieKakao1212.opencu.common.util.RedstoneControlType;
import net.minecraft.block.entity.BlockEntity;

public class DeviceBlockEntity extends BlockEntity implements IDeviceContainer {

    private IFramedDevice device;
    private IDeviceState state;

    public DeviceBlockEntity(IFramedDevice device) {
        super(null, null, null);
    }


    @Override
    public void cycleRedstoneControl() {

    }

    @Override
    public void setRedstoneControlType(RedstoneControlType type) {

    }

    @Override
    public RedstoneControlType getRedstoneControlType() {
        return null;
    }

    @Override
    public IFramedDevice getDevice() {
        return null;
    }

    @Override
    public IDeviceState getState() {
        return null;
    }

    @Override
    public void scheduleActivation() {

    }

    @Override
    public boolean isSameAs(IDeviceContainer deviceContainer) {
        return false;
    }
}
