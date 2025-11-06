package com.LubieKakao1212.opencu.registry.fabric;

import com.LubieKakao1212.opencu.common.OpenCUModCommon;
import io.wispforest.owo.itemgroup.Icon;
import io.wispforest.owo.itemgroup.OwoItemGroup;
import net.minecraft.util.Identifier;

public class CUItemGroups {

    public static final OwoItemGroup OCU_MAIN = OwoItemGroup.builder(new Identifier(OpenCUModCommon.MODID, "ocu-main"), () -> Icon.of(CUBlocks.REPULSOR))
            .build();

    public static void init() {
        OCU_MAIN.initialize();
    }

}
