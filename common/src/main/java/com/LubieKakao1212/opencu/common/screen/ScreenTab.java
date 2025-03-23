package com.LubieKakao1212.opencu.common.screen;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;

public abstract class ScreenTab<THandler extends ScreenHandler, TScreen extends ScreenWithTabs<THandler>> {

    public void init(TScreen screen) { }

    public void render(TScreen screen, DrawContext context, int mouseX, int mouseY, float partialTick) { }

    public void drawIcon(TScreen screen, DrawContext context, int x, int y, float partialTick) {
        context.drawItem(icon(), x, y);
    }

    public abstract ItemStack icon();

}