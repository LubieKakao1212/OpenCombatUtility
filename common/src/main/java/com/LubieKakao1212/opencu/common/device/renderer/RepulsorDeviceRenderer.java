package com.LubieKakao1212.opencu.common.device.renderer;

import com.LubieKakao1212.opencu.common.block.entity.renderer.Color;
import com.LubieKakao1212.opencu.common.device.IDeviceContainer;
import com.LubieKakao1212.opencu.common.device.IFramedDevice;
import com.LubieKakao1212.opencu.common.device.state.IDeviceState;
import com.LubieKakao1212.opencu.common.rendering.RenderingUtil;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.model.BasicBakedModel;
import net.minecraft.client.util.ModelIdentifier;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;

public class RepulsorDeviceRenderer implements IDeviceRenderer {

    public static final Color offlineColor = new Color(0.25f, 0.5f,0.5f, 1f);
    public static final Color onlineColor = new Color(0f, 1f,1f, 1f);
    public static final ModelIdentifier lanternLocation = new ModelIdentifier("minecraft","sea_lantern", "");
    public static final ModelIdentifier frameLocation = new ModelIdentifier("opencu","repulsor_frame", "");
    public static final ModelIdentifier frameLocation_back_bl = new ModelIdentifier("opencu","repulsor_frame_bl", "");

    private static final float offset = -1f / 16f;

    public static final ModelIdentifier[] back_models = new ModelIdentifier[] {
            new ModelIdentifier("opencu","repulsor_frame_bl", ""),
            new ModelIdentifier("opencu","repulsor_frame_br", ""),
            new ModelIdentifier("opencu","repulsor_frame_tl", ""),
            new ModelIdentifier("opencu","repulsor_frame_tr", "")
    };

    public static final Vector3f[] back_offsets = new Vector3f[] {
            new Vector3f(offset, offset, 0),
            new Vector3f(-offset, offset, 0),
            new Vector3f(offset, -offset, 0),
            new Vector3f(-offset, -offset, 0),
    };

    public static final int pulseTicks = 20;

    @Override
    public void render(@NotNull World world, @NotNull IDeviceContainer container, @NotNull IFramedDevice device, @NotNull IDeviceState state, @Nullable ItemStack deviceItem, float partialTick, @NotNull MatrixStack matrixStack, @NotNull VertexConsumerProvider vertexConsumerSource, int packedLight, int packedOverlay) {
        //super.render(world, container, device, state, deviceItem, partialTick, poseStack, vertexConsumerSource, packedLight, packedOverlay);
        var solid = vertexConsumerSource.getBuffer(RenderLayer.getSolid());
        matrixStack.push();
        matrixStack.translate(-0.5,-0.5,-0.5);
        renderFrame(matrixStack, state, solid, packedLight, packedOverlay);
        renderLantern(world, container, matrixStack, state, partialTick, solid, packedOverlay);
        matrixStack.pop();
    }

    public static void renderLantern(@NotNull World world, @NotNull IDeviceContainer container, @NotNull MatrixStack matrixStack, @NotNull IDeviceState state, float partialTick, @NotNull VertexConsumer vertexConsumer, int packedOverlay) {
        var animTicks = world.getTime() - container.getLastActiveTimestamp();
        var animTicksLeft = Math.max(pulseTicks - animTicks, 0);
        var animProgress = Math.max((animTicksLeft - partialTick), 0) / (float)pulseTicks;

        Color finalColor = Color.lerp(offlineColor, onlineColor, animProgress);

        float gap = 1.1f / 16f;
        float scale = 1f - gap * 2f;

        BasicBakedModel model = (BasicBakedModel) MinecraftClient.getInstance().getBakedModelManager().getModel(lanternLocation);

        matrixStack.translate(gap, gap, gap);
        matrixStack.scale(scale, scale, scale);

        RenderingUtil.renderModel(model, vertexConsumer, matrixStack, finalColor, 511, packedOverlay);
    }

    public static void renderFrame(@NotNull MatrixStack matrixStack, @NotNull IDeviceState state, @NotNull VertexConsumer vertexConsumer, int packedLight, int packedOverlay) {
        BasicBakedModel model = (BasicBakedModel) MinecraftClient.getInstance().getBakedModelManager().getModel(frameLocation);
        RenderingUtil.renderModel(model, vertexConsumer, matrixStack, new Color(1f, 1f,1f, 1f), packedLight, packedOverlay);

        for(int i=0; i<back_models.length; i++) {
            renderBackPart(matrixStack, back_models[i], vertexConsumer, back_offsets[i], -1, packedLight, packedOverlay);
        }
        //renderBackPart(matrixStack, frameLocation_back_bl, vertexConsumer, new Vector3f(v, v, 0), 1f, packedLight, packedOverlay);
    }

    private static void renderBackPart(@NotNull MatrixStack matrixStack, @NotNull ModelIdentifier modelId, @NotNull VertexConsumer vertexConsumer, @NotNull Vector3f targetOffset, float offsetRatio, int packedLight, int packedOverlay) {
        matrixStack.push();

        var offset = new Vector3f(0).lerp(targetOffset, offsetRatio);
        matrixStack.translate(offset.x, offset.y, offset.z);
        BasicBakedModel model = (BasicBakedModel) MinecraftClient.getInstance().getBakedModelManager().getModel(modelId);
        RenderingUtil.renderModel(model, vertexConsumer, matrixStack, new Color(1f, 1f,1f, 1f), packedLight, packedOverlay);

        matrixStack.pop();
    }
}
