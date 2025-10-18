package com.LubieKakao1212.opencu.common.util;

import com.LubieKakao1212.opencu.OpenCUConfigCommon;
import com.LubieKakao1212.opencu.registry.CUPulse;
import net.minecraft.util.Identifier;

public class ConfigUtil {

    public static double getPulsePowerForDisplay(Identifier pulseId, double power) {
        if(pulseId.equals(CUPulse.STASIS_ID)) {
            return Math.round(power * 1000.0) / 10.; // 100.0%
        }
        return power * OpenCUConfigCommon.repulsorDevice().forceScale();
    }
}
