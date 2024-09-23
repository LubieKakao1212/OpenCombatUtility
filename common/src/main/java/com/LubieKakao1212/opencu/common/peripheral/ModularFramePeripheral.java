package com.LubieKakao1212.opencu.common.peripheral;

import com.LubieKakao1212.opencu.common.block.entity.BlockEntityModularFrame;
import com.LubieKakao1212.opencu.registry.CUIds;
import dan200.computercraft.api.lua.*;
import org.jetbrains.annotations.NotNull;

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
