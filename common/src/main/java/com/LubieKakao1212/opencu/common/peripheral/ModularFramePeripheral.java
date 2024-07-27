package com.LubieKakao1212.opencu.common.peripheral;

import com.LubieKakao1212.opencu.common.block.entity.BlockEntityModularFrame;
import com.LubieKakao1212.opencu.common.peripheral.device.IDeviceApi;
import com.LubieKakao1212.opencu.common.util.RedstoneControlType;
import com.LubieKakao1212.opencu.registry.CUIds;
import dan200.computercraft.api.lua.*;
import dan200.computercraft.api.peripheral.IPeripheral;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ModularFramePeripheral extends DeviceContainerPeripheral {

    public ModularFramePeripheral(@NotNull BlockEntityModularFrame modularFrame) {
        super(modularFrame, CUIds.MODULAR_FRAME.toString());
    }

    @LuaFunction
    public final boolean aim(double yaw, double pitch) {
        frame().aim(pitch, yaw);
        return true;
    }

    @LuaFunction
    public final boolean isAligned() {
        return frame().isAligned();
    }

    @LuaFunction
    public final boolean isRequiresLock() {
        return frame().isRequiresLock();
    }

    @LuaFunction
    public final void setRequiresLock(boolean requiresLock) {
        frame().setRequiresLock(requiresLock);
    }

    private BlockEntityModularFrame frame() {
        return (BlockEntityModularFrame) target;
    }
}
