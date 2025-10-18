package com.LubieKakao1212.opencu.registry;

import com.LubieKakao1212.opencu.common.OpenCUModCommon;
import com.LubieKakao1212.opencu.common.pulse.EntityPulseType;
import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class CUPulse {


    public static final String REPULSOR_ID_str = OpenCUModCommon.MODID + ":repulsor";
    public static final String VECTOR_ID_str = OpenCUModCommon.MODID + ":vector";
    public static final String STASIS_ID_str = OpenCUModCommon.MODID + ":stasis";

    public static final Identifier REPULSOR_ID = new Identifier(OpenCUModCommon.MODID, "repulsor");
    public static final Identifier VECTOR_ID = new Identifier(OpenCUModCommon.MODID, "vector");
    public static final Identifier STASIS_ID = new Identifier(OpenCUModCommon.MODID, "stasis");

    @NotNull
    @ExpectPlatform
    public static EntityPulseType defaultPulse() {
        return null;
    }


    @Nullable
    @ExpectPlatform
    public static EntityPulseType get(Identifier id) { return null; }

}
