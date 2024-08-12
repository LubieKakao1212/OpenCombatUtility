package com.LubieKakao1212.opencu.registry.fabric;

import com.LubieKakao1212.opencu.common.OpenCUModCommon;
import com.LubieKakao1212.opencu.common.item.ItemAimTool;
import com.LubieKakao1212.opencu.common.item.ItemLinkTool;
import io.wispforest.owo.itemgroup.OwoItemSettings;
import io.wispforest.owo.registration.reflect.ItemRegistryContainer;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.item.Item;

public class CUItems implements ItemRegistryContainer {

    public static final Item AIM_TOOL = new ItemAimTool(new OwoItemSettings().maxCount(1).group(CUItemGroups.OCU_MAIN));

    public static final Item LINK_TOOL = new ItemLinkTool(new OwoItemSettings().maxCount(1).group(CUItemGroups.OCU_MAIN));

    public static final Item DISPENSER_GOLD = new Item(new OwoItemSettings().group(CUItemGroups.OCU_MAIN));
    public static final Item DISPENSER_DIAMOND = new Item(new OwoItemSettings().group(CUItemGroups.OCU_MAIN));
    public static final Item DISPENSER_NETHERITE = new Item(new OwoItemSettings().group(CUItemGroups.OCU_MAIN));


}
