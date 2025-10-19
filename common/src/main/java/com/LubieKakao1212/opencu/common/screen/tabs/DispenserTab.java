package com.LubieKakao1212.opencu.common.screen.tabs;

import com.LubieKakao1212.opencu.common.OpenCUModCommon;
import com.LubieKakao1212.opencu.common.screen.DeviceContainerScreen;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.joml.Vector2i;

public class DispenserTab extends DeviceContainerScreenTab {

    public static final Identifier backgroundTexture = new Identifier(OpenCUModCommon.MODID, "textures/gui/dispenser_gui_tab.png");

    public DispenserTab() {

    }

    @Override
    public Identifier getDefaultBackgroundTexture() {
        return backgroundTexture;
    }

    @Override
    public Text getTabName() {
        return Text.translatable("info.opencu.gui.dc.tab.dispenser");
    }
}
