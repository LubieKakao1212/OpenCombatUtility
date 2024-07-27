package com.LubieKakao1212.opencu.common.device.state;

public abstract class DeviceStateBase implements IDeviceState {

    private boolean isValid = true;

    @Override
    public void invalidate() {
        isValid = false;
    }

    @Override
    public boolean isValid() {
        return this.isValid;
    }
}
