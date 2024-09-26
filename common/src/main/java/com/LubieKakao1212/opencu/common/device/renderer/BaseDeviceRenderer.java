package com.LubieKakao1212.opencu.common.device.renderer;

import com.LubieKakao1212.opencu.common.device.IDeviceContainer;
import com.LubieKakao1212.opencu.common.device.IFramedDevice;
import com.LubieKakao1212.opencu.common.device.state.IDeviceState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.ModelIdentifier;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class BaseDeviceRenderer implements IDeviceRenderer {

    @Override
    public void render(@NotNull World world, @NotNull IDeviceContainer container, @NotNull IFramedDevice device, @NotNull IDeviceState state, @Nullable ItemStack deviceItem, float partialTick, @NotNull MatrixStack matrixStack, @NotNull VertexConsumerProvider bufferSource, int packedLight, int packedOverlay) {
        if(deviceItem != null) {
            matrixStack.push();
            matrixStack.scale(2f,2f,2f);
            MinecraftClient.getInstance().getItemRenderer().renderItem(deviceItem, ModelTransformationMode.FIXED, packedLight, packedOverlay, matrixStack, bufferSource, world, 0);
            matrixStack.pop();
        }
    }
}
