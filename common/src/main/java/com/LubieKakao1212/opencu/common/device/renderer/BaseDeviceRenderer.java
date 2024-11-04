package com.LubieKakao1212.opencu.common.device.renderer;

import com.LubieKakao1212.opencu.common.device.IDeviceContainer;
import com.LubieKakao1212.opencu.common.device.IFramedDevice;
import com.LubieKakao1212.opencu.common.device.state.IDeviceState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.world.ClientWorld;
import org.jetbrains.annotations.NotNull;

public class BaseDeviceRenderer implements IDeviceRenderer {

//    @Override
//    public void render(@NotNull World world, @NotNull IDeviceContainer container, @NotNull IFramedDevice device, @NotNull IDeviceState state, float partialTick, @NotNull MatrixStack matrixStack, @NotNull VertexConsumerProvider bufferSource, int packedLight, int packedOverlay) {
//        var deviceItem = container.getDeviceItem();
//        if(deviceItem != null) {
//            MinecraftClient.getInstance().getItemRenderer().renderItem(deviceItem, ModelTransformationMode.NONE, packedLight, packedOverlay, matrixStack, bufferSource, world, 0);
//        }
//    }

    @Override
    public void render(@NotNull ClientWorld world, @NotNull IDeviceContainer container, @NotNull IFramedDevice device, @NotNull IDeviceState state, @NotNull Context context, float partialTick, @NotNull MatrixStack matrixStack, @NotNull VertexConsumerProvider bufferSource, int packedLight, int packedOverlay) {
        var deviceItem = container.getDeviceItem();
        if(deviceItem != null) {
            //var itemModel = context.itemRenderer().getModels().getModel(deviceItem);
            //var vertexConsumer = bufferSource.getBuffer(RenderLayer.getTranslucent());
            //context.blockModelRenderer().render(world, itemModel, Blocks.AIR.getDefaultState(), context.worldPos(), matrixStack, vertexConsumer, false, world.random, 0, packedOverlay);
            //TODO apply ambient occlusion
            MinecraftClient.getInstance().getItemRenderer().renderItem(deviceItem, ModelTransformationMode.NONE, packedLight, packedOverlay, matrixStack, bufferSource, world, 0);
        }
    }
}
