package com.LubieKakao1212.opencu.common.device;

import com.LubieKakao1212.opencu.common.block.entity.IRedstoneControlled;
import com.LubieKakao1212.opencu.common.device.state.IDeviceState;

public interface IDeviceContainer extends IRedstoneControlled {

    IFramedDevice getDevice();

    IDeviceState getState();

    void scheduleActivation();

    boolean isSameAs(IDeviceContainer deviceContainer);

}
