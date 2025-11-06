package com.LubieKakao1212.opencu;

import com.LubieKakao1212.opencu.common.device.IFramedDevice;
import com.LubieKakao1212.opencu.common.device.renderer.IDeviceRenderer;
import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class PlatformUtil {

    @ExpectPlatform
    public static IFramedDevice getDeviceFrom(@NotNull ItemStack stack) { return null; }

}
