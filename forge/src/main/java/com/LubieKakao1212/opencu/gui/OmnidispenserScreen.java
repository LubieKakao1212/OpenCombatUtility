package com.LubieKakao1212.opencu.gui;

import com.LubieKakao1212.opencu.OpenCUMod;
import com.LubieKakao1212.opencu.gui.container.OmnidispenserMenu;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class OmnidispenserScreen extends HandledScreen<OmnidispenserMenu> {

    private static final Identifier mainTexture = new Identifier(OpenCUMod.MODID, "textures/gui/omnidispenser_gui.png");

    public OmnidispenserScreen(OmnidispenserMenu container, PlayerInventory inv, Text titleIn) {
        super(container, inv, titleIn);
        this.backgroundWidth = 176;
        this.backgroundHeight = 166;
    }

    @Override
    public void render(MatrixStack poseStack, int mouseX, int mouseY, float partialTick) {
        this.renderBackground(poseStack);
        this.drawBackground(poseStack, partialTick, mouseX, mouseY);
        super.render(poseStack, mouseX, mouseY, partialTick);
        this.drawMouseoverTooltip(poseStack, mouseX, mouseY);
    }

    @Override
    protected void drawBackground(MatrixStack poseStack, float partialTick, int mouseX, int mouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexProgram);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, mainTexture);

        drawTexture(poseStack, x, y,0,0, backgroundWidth, backgroundHeight);
    }
}
