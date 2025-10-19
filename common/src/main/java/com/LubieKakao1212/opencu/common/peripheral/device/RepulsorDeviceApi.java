package com.LubieKakao1212.opencu.common.peripheral.device;

import com.LubieKakao1212.opencu.common.OpenCUModCommon;
import com.LubieKakao1212.opencu.common.device.state.IDeviceState;
import com.LubieKakao1212.opencu.common.device.state.RepulsorDeviceState;
import com.LubieKakao1212.opencu.registry.CUPulse;
import dan200.computercraft.api.lua.LuaException;
import dan200.computercraft.api.lua.LuaFunction;
import net.minecraft.util.Identifier;

public class RepulsorDeviceApi extends DeviceApiBase {

    public RepulsorDeviceState state;

    public RepulsorDeviceApi(RepulsorDeviceState state) {
        this.state = state;
    }

    @LuaFunction
    @Override
    public final String getApiId() throws LuaException {
        assertValid();
        return OpenCUModCommon.MODID + ":repulsor";
    }

    @Override
    public IDeviceState parentState() {
        return state;
    }

    @LuaFunction
    public void setPulseType(String typeId) throws LuaException {
        assertValid();
        var pulse = CUPulse.get(new Identifier(typeId));
        if(pulse == null) {
            throw new LuaException("Unknown pulse type: " + typeId);
        }
        state.setPulseType(pulse);
    }

    public String getPulseType() throws LuaException {
        assertValid();
        return state.getPulseType().getRegistryKey().toString();
    }

    @LuaFunction
    public double setForce(double force) throws LuaException {
        assertValid();
        return state.setForce(force);
    }

    @LuaFunction
    public double getForce() throws LuaException {
        assertValid();
        return state.getForce();
    }

    @LuaFunction
    public double setRadius(double force) throws LuaException {
        assertValid();
        return state.setRadius(force);
    }

    @LuaFunction
    public double getRadius(double force) throws LuaException {
        assertValid();
        return state.getRadius();
    }

    @LuaFunction
    public double getMaxRadius(double force) throws LuaException {
        assertValid();
        return state.getMaxRadius();
    }

    //TODO get and set pulseType

    @LuaFunction
    public int getEnergyUsage() throws LuaException {
        assertValid();
        return state.getEnergyUsage();
    }

}
