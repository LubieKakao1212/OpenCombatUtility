package com.LubieKakao1212.opencu.fabric;

import com.LubieKakao1212.opencu.common.device.IFramedDevice;
import com.LubieKakao1212.opencu.fabric.apilookup.APILookupIFramedDevice;
import net.minecraft.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class PlatformUtilImpl {
    public static IFramedDevice getDeviceFrom(@NotNull ItemStack stack) {
        return APILookupIFramedDevice.FRAMED_DEVICES.find(stack, null);
    }
}
