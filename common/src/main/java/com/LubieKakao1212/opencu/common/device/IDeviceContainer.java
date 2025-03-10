package com.LubieKakao1212.opencu.common.device;

import com.LubieKakao1212.opencu.common.block.entity.IRedstoneControlled;
import com.LubieKakao1212.opencu.common.device.state.IDeviceState;
import net.minecraft.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

public interface IDeviceContainer extends IRedstoneControlled {

    @Nullable
    ItemStack getDeviceItem();

    @NotNull
    IFramedDevice getDevice();

    @NotNull
    IDeviceState getState();

    void scheduleActivation();

    boolean isSameAs(IDeviceContainer deviceContainer);

}
