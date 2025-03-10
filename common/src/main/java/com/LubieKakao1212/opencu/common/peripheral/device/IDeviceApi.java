package com.LubieKakao1212.opencu.common.peripheral.device;

import com.LubieKakao1212.opencu.common.device.state.IDeviceState;
import dan200.computercraft.api.lua.LuaException;
import dan200.computercraft.api.lua.LuaFunction;

public interface IDeviceApi {

    String getApiId() throws LuaException;

    /**
     * Should be checked inside every LuaFunction
     * @return
     */
    default boolean isValid() {
        return parentState().isValid();
    }

    default void assertValid() throws LuaException {
        if(!isValid()) {
            throw new LuaException("The device is no longer valid");
        }
    }

    IDeviceState parentState();
}
