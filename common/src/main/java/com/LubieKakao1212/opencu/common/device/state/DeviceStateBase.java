package com.LubieKakao1212.opencu.common.device.state;

import org.jetbrains.annotations.NotNull;

public abstract class DeviceStateBase implements IDeviceState {

    private boolean isValid = true;

    @NotNull
    private final Runnable markDirtyDelegate;

    public DeviceStateBase(@NotNull Runnable markDirtyDelegate) {
        this.markDirtyDelegate = markDirtyDelegate;
    }

    @Override
    public void invalidate() {
        isValid = false;
    }

    @Override
    public boolean isValid() {
        return this.isValid;
    }

    @Override
    public void markDirty() {
        markDirtyDelegate.run();
    }
}
