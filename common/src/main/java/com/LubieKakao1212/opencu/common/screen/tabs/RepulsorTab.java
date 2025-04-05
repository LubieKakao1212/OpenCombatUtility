package com.LubieKakao1212.opencu.common.screen.tabs;

import com.LubieKakao1212.opencu.common.OpenCUModCommon;
import com.LubieKakao1212.opencu.common.screen.DeviceContainerScreen;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class RepulsorTab extends DeviceContainerScreenTab {

    public static final Identifier backgroundTexture = new Identifier(OpenCUModCommon.MODID, "textures/gui/repulsor_gui_tab.png");

    public RepulsorTab() { }

    @Override
    public Identifier getDefaultBackgroundTexture() {
        return backgroundTexture;
    }

    @Override
    public ItemStack icon(DeviceContainerScreen screen) {
        return new ItemStack(Items.DISPENSER);
    }

    @Override
    public Text getTabName() {
        return Text.translatable("info.opencu.gui.dc.tab.repulsor");
    }
}
