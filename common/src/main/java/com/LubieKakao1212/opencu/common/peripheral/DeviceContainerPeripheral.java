package com.LubieKakao1212.opencu.common.peripheral;

import com.LubieKakao1212.opencu.common.device.IDeviceContainer;
import com.LubieKakao1212.opencu.common.peripheral.device.IDeviceApi;
import com.LubieKakao1212.opencu.common.util.RedstoneControlType;
import dan200.computercraft.api.lua.LuaException;
import dan200.computercraft.api.lua.LuaFunction;
import dan200.computercraft.api.peripheral.IPeripheral;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class DeviceContainerPeripheral implements IPeripheral {

    protected final IDeviceContainer target;
    protected final String type;

    public DeviceContainerPeripheral(IDeviceContainer deviceContainer, String type) {
        this.target = deviceContainer;
        this.type = type;
    }

    /**
     * Should return a string that uniquely identifies this type of peripheral.
     * This can be queried from lua by calling {@code peripheral.getType()}
     *
     * @return A string identifying the type of peripheral.
     */
    @NotNull
    @Override
    public String getType() {
        return type;
    }

    /**
     * Determine whether this peripheral is equivalent to another one.
     * <p>
     * The minimal example should at least check whether they are the same object. However, you may wish to check if
     * they point to the same block or tile entity.
     *
     * @param other The peripheral to compare against. This may be {@code null}.
     * @return Whether these peripherals are equivalent.
     */
    @Override
    public boolean equals(@Nullable IPeripheral other) {
        if(other instanceof DeviceContainerPeripheral otherDispenser) {
            return otherDispenser.target.isSameAs(target);
        }
        return false;
    }

    @Nullable
    @Override
    public Object getTarget() {
        return target;
    }

    @LuaFunction
    public final void setRedstoneControl(String type) throws LuaException {
        try {
            var rsct = RedstoneControlType.valueOf(type);
            target.setRedstoneControlType(rsct);
        }
        catch (IllegalArgumentException e) {
            throw new LuaException(e.getMessage());
        }
    }

    @LuaFunction
    public final String getRedstoneControl() {
        return target.getRedstoneControlType().toString();
    }

    @LuaFunction
    public final IDeviceApi getDeviceApi() {
        return target.getState().getApi();
    }

    @LuaFunction
    public final boolean activate() {
        target.scheduleActivation();
        return true;
    }
}
