package com.LubieKakao1212.opencu.common.peripheral.device;

import com.LubieKakao1212.opencu.common.OpenCUModCommon;
import com.LubieKakao1212.opencu.common.device.state.IDeviceState;
import com.LubieKakao1212.opencu.common.device.state.ShooterDeviceState;
import dan200.computercraft.api.lua.LuaException;
import dan200.computercraft.api.lua.LuaFunction;

public class ShooterDeviceApi extends DeviceApiBase {

    //Reference cycle?
    private final ShooterDeviceState state;

    public ShooterDeviceApi(ShooterDeviceState state) {
        this.state = state;
    }

    @Override
    @LuaFunction
    public final String getApiId() throws LuaException {
        assertValid();
        return OpenCUModCommon.MODID + ":shooter";
    }

    @Override
    public IDeviceState parentState() {
        return state;
    }

    @LuaFunction
    public final double getSpread() throws LuaException {
        assertValid();
        return state.getSpread();
    }

    @LuaFunction
    public final double getForce() throws LuaException {
        assertValid();
        return state.getForce();
    }

    @LuaFunction
    public final double getEnergyUsage() throws LuaException {
        assertValid();
        return state.getBaseEnergyUsage();
    }
}
