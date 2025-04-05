package com.LubieKakao1212.opencu.common.device.renderer;

import com.LubieKakao1212.opencu.common.block.entity.renderer.Color;
import com.LubieKakao1212.opencu.common.device.IDeviceContainer;
import com.LubieKakao1212.opencu.common.device.IFramedDevice;
import com.LubieKakao1212.opencu.common.device.state.IDeviceState;
import com.LubieKakao1212.opencu.common.device.state.RepulsorDeviceState;
import com.LubieKakao1212.opencu.common.rendering.RenderingUtil;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.LightmapTextureManager;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.BlockModelRenderer;
import net.minecraft.client.render.model.BasicBakedModel;
import net.minecraft.client.util.ModelIdentifier;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
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

    private static final Vector3f[] back_offsets = new Vector3f[] {
            new Vector3f(-offset, -offset, offset),
            new Vector3f(offset, -offset, offset),
            new Vector3f(-offset, offset, offset),
            new Vector3f(offset, offset, offset),
    };

    public static final int pulseTicks = 20;

    @Override
    public void render(@NotNull ClientWorld world, @NotNull IDeviceContainer container, @NotNull IFramedDevice device, @NotNull IDeviceState state, @NotNull Context context, float partialTick, @NotNull MatrixStack matrixStack, @NotNull VertexConsumerProvider vertexConsumerSource, int packedLight, int packedOverlay) {
        //TODO apply ambient occlusion
        var solid = vertexConsumerSource.getBuffer(RenderLayer.getSolid());
        matrixStack.push();
        matrixStack.translate(-0.5,-0.5,-0.5);
        renderFrame(world, context.blockModelRenderer(), context.worldPos(), matrixStack, (RepulsorDeviceState) state, solid, packedLight, packedOverlay);
        renderLantern(world, matrixStack, state, partialTick, solid, packedOverlay);
        matrixStack.pop();
    }

    public static void renderLantern(@NotNull World world, @NotNull MatrixStack matrixStack, @NotNull IDeviceState state, float partialTick, @NotNull VertexConsumer vertexConsumer, int packedOverlay) {
        var repulsorState = (RepulsorDeviceState) state;
        var animTicks = world.getTime() - repulsorState.getLastActivationTimestamp();
        var animTicksLeft = Math.max(pulseTicks - animTicks, 0);
        var animProgress = Math.max((animTicksLeft - partialTick), 0) / (float)pulseTicks;

        Color finalColor = Color.lerp(offlineColor, onlineColor, animProgress);

        float gap = 1.1f / 16f;
        float scale = 1f - gap * 2f;

        BasicBakedModel model = (BasicBakedModel) MinecraftClient.getInstance().getBakedModelManager().getModel(lanternLocation);

        matrixStack.translate(gap, gap, gap);
        matrixStack.scale(scale, scale, scale);

        RenderingUtil.renderModel(model, vertexConsumer, matrixStack, finalColor, LightmapTextureManager.MAX_LIGHT_COORDINATE, packedOverlay);
    }

    public static void renderFrame(@NotNull ClientWorld world, @NotNull BlockModelRenderer renderer, @NotNull BlockPos pos,
                                   @NotNull MatrixStack matrixStack, @NotNull RepulsorDeviceState state, @NotNull VertexConsumer vertexConsumer,
                                   int packedLight, int packedOverlay) {
        BasicBakedModel model = (BasicBakedModel) MinecraftClient.getInstance().getBakedModelManager().getModel(frameLocation);
        RenderingUtil.renderModel(model, vertexConsumer, matrixStack, new Color(1f, 1f,1f, 1f), packedLight, packedOverlay);

        var offsetRatio = (float) state.getDirectionBlend();

        for(int i=0; i<back_models.length; i++) {
            renderBackPart(world, renderer, pos, matrixStack, back_models[i], vertexConsumer, back_offsets[i], offsetRatio, packedLight, packedOverlay);
        }
        //renderBackPart(matrixStack, frameLocation_back_bl, vertexConsumer, new Vector3f(v, v, 0), 1f, packedLight, packedOverlay);
    }

    private static void renderBackPart(@NotNull ClientWorld world, @NotNull BlockModelRenderer renderer, @NotNull BlockPos pos,
                                       @NotNull MatrixStack matrixStack, @NotNull ModelIdentifier modelId, @NotNull VertexConsumer vertexConsumer,
                                       @NotNull Vector3f targetOffset, float offsetRatio, int packedLight, int packedOverlay) {
        matrixStack.push();
        var offset = new Vector3f(0).lerp(targetOffset, offsetRatio);
        matrixStack.translate(offset.x, offset.y, offset.z);
        var model = MinecraftClient.getInstance().getBakedModelManager().getModel(modelId);
        RenderingUtil.renderModel(model, vertexConsumer, matrixStack, new Color(1f, 1f,1f, 1f), packedLight, packedOverlay);

        //renderer.render(world, model, Blocks.AIR.getDefaultState(), position, matrixStack, vertexConsumer, false, world.random,0, packedOverlay);
        matrixStack.pop();
    }
}
