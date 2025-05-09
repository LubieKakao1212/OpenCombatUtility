package com.LubieKakao1212.opencu.registry;

import com.LubieKakao1212.opencu.PlatformUtil;
import com.LubieKakao1212.opencu.common.device.IFramedDevice;
import com.LubieKakao1212.opencu.common.device.renderer.BaseDeviceRenderer;
import com.LubieKakao1212.opencu.common.device.renderer.IDeviceRenderer;
import com.LubieKakao1212.opencu.common.device.renderer.RepulsorDeviceRenderer;
import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

public class CUDeviceRanderers {

    private static final Map<IFramedDevice, IDeviceRenderer> renderers = new HashMap<>();

    public static final BaseDeviceRenderer FALLBACK = new BaseDeviceRenderer();
    public static final RepulsorDeviceRenderer REPULSOR = new RepulsorDeviceRenderer();

    static {
        renderers.put(CUFramedDevices.REPULSOR, REPULSOR);
    }

    public static IDeviceRenderer getRenderer(@Nullable IFramedDevice device) {
        return renderers.getOrDefault(device, FALLBACK);
    }

    public static IDeviceRenderer getRenderer(ItemStack itemStack) {
        return getRenderer(PlatformUtil.getDeviceFrom(itemStack));
    }
}
