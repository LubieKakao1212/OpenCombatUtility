package com.LubieKakao1212.opencu.common.screen.tabs;

import com.LubieKakao1212.opencu.common.screen.DeviceContainerScreen;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.joml.Vector2f;
import org.joml.Vector2i;

public abstract class DeviceContainerScreenTab {

    public void init(DeviceContainerScreen screen) { }

    public void preRenderTab(DeviceContainerScreen screen, DrawContext context, int mouseX, int mouseY, float partialTick) { }

    public void renderTabBackground(DeviceContainerScreen screen, DrawContext context, int mouseX, int mouseY, float partialTick) {
        context.drawTexture(getDefaultBackgroundTexture(), screen.getX(), screen.getY(), 0, 0, screen.getBgWidth(), screen.getBgHeight());
    }

    public Identifier getDefaultBackgroundTexture() { return DeviceContainerScreen.mainTexture; }

    public abstract Text getTabName();

}