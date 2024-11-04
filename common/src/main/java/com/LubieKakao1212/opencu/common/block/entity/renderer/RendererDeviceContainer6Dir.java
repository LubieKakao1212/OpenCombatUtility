package com.LubieKakao1212.opencu.common.block.entity.renderer;

import com.LubieKakao1212.opencu.common.block.BlockDevice6Dir;
import com.LubieKakao1212.opencu.common.block.entity.BlockEntityDeviceContainer6Dir;
import com.LubieKakao1212.opencu.common.device.renderer.IDeviceRenderer;
import com.LubieKakao1212.opencu.registry.CUDeviceRanderers;
import net.minecraft.block.FacingBlock;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.BlockModelRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.math.Direction;
import org.joml.Quaternionf;

import java.util.EnumMap;
import java.util.Objects;

public class RendererDeviceContainer6Dir implements BlockEntityRenderer<BlockEntityDeviceContainer6Dir> {

    private static final EnumMap<Direction, Quaternionf> rotations = new EnumMap<>(Direction.class);

    static {
        rotations.put(Direction.NORTH, new Quaternionf().identity());
        rotations.put(Direction.WEST, new Quaternionf().rotateLocalY((float) (Math.PI / 2f)));
        rotations.put(Direction.SOUTH, new Quaternionf().rotateLocalY((float) (Math.PI)));
        rotations.put(Direction.EAST, new Quaternionf().rotateLocalY((float) -(Math.PI / 2f)));
        rotations.put(Direction.UP, new Quaternionf().rotateLocalX((float) (Math.PI / 2f)));
        rotations.put(Direction.DOWN, new Quaternionf().rotateLocalX((float) -(Math.PI / 2f)));
    }

    private final BlockModelRenderer blockModelRenderer;
    private final ItemRenderer itemRenderer;

    public RendererDeviceContainer6Dir(BlockEntityRendererFactory.Context ctx) {
        this.blockModelRenderer = ctx.getRenderManager().getModelRenderer();
        this.itemRenderer = ctx.getItemRenderer();
    }

    @Override
    public void render(BlockEntityDeviceContainer6Dir entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
        var device = entity.getDevice();
        var renderer = CUDeviceRanderers.getRenderer(device);
        var world = (ClientWorld) Objects.requireNonNull(entity.getWorld());

        var state = world.getBlockState(entity.getPos());
        if(!(state.getBlock() instanceof BlockDevice6Dir)) {
            return;
        }
        var rotation = rotations.get(state.get(FacingBlock.FACING));

        matrices.push();
        matrices.translate(0.5f,0.5f,0.5f);
        matrices.multiply(rotation);
        var context = new IDeviceRenderer.Context(state, entity.getPos(), blockModelRenderer, itemRenderer);
        renderer.render(world, entity, device, entity.getState(), context, tickDelta, matrices, vertexConsumers, light, overlay);
        matrices.pop();
    }
}
