package com.LubieKakao1212.opencu.common.mixin;

import com.LubieKakao1212.opencu.common.screen.DeviceContainerScreen;
import com.LubieKakao1212.opencu.common.screen.slot.IDrawSlotAsLocked;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.Slot;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(HandledScreen.class)
public class SlotDrawCMixin {

    @Unique
    private static final Identifier openCU$Texture = DeviceContainerScreen.mainTexture;

    @Redirect(method = "drawSlot(Lnet/minecraft/client/gui/DrawContext;Lnet/minecraft/screen/slot/Slot;)V",
                at = @At(
                        value = "INVOKE",
                        target = "Lnet/minecraft/client/gui/DrawContext;drawItemInSlot(Lnet/minecraft/client/font/TextRenderer;Lnet/minecraft/item/ItemStack;IILjava/lang/String;)V"))
    public void drawLock(DrawContext instance, TextRenderer textRenderer, ItemStack stack, int x, int y, String countOverride, DrawContext context, Slot slot) {
        instance.drawItemInSlot(textRenderer, stack, x, y, countOverride);
        if(slot instanceof IDrawSlotAsLocked) {
            context.getMatrices().push();
            context.getMatrices().translate(0,0,210f); //Translate in front of the items

            context.drawTexture(openCU$Texture, x, y, 197, 2, 16, 16);
            context.getMatrices().pop();
        }
    }
}
