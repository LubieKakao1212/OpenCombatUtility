package com.LubieKakao1212.opencu.common.peripheral.device;

import com.LubieKakao1212.opencu.common.device.state.IDeviceState;
import dan200.computercraft.api.lua.LuaFunction;

public abstract class DeviceApiBase implements IDeviceApi {

    @LuaFunction
    @Override
    public final boolean isValid() {
        return IDeviceApi.super.isValid();
    }

}
