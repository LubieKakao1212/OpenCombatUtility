package com.LubieKakao1212.opencu.common.block.entity.renderer;

import com.LubieKakao1212.opencu.common.block.entity.BlockEntityModularFrame;
import com.LubieKakao1212.opencu.common.device.renderer.IDeviceRenderer;
import com.LubieKakao1212.opencu.registry.CUDeviceRanderers;
import com.lubiekakao1212.qulib.math.Aim;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.BlockModelRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.item.ItemStack;
import org.joml.Quaterniond;
import org.joml.Quaternionf;

public class RendererModularFrame implements BlockEntityRenderer<BlockEntityModularFrame> {

    private static final Quaternionf y180;
    private static final Quaterniond miry;

    static {
        y180 = new Quaternionf().fromAxisAngleRad(0, 1.f, 0, (float)Math.PI);
        miry = new Quaterniond().set(1, -1, 1, -1);
    }

    private final BlockModelRenderer blockModelRenderer;
    private final ItemRenderer itemRenderer;

    public RendererModularFrame(BlockEntityRendererFactory.Context ctx) {
        this.blockModelRenderer = ctx.getRenderManager().getModelRenderer();
        this.itemRenderer = ctx.getItemRenderer();
    }

    @Override
    public void render(BlockEntityModularFrame blockEntity, float partialTick, MatrixStack poseStack, VertexConsumerProvider bufferSource, int packedLight, int packedOverlay) {
        //super.render(te, x, y, z, partialTicks, destroyStage, alpha);

        var device = blockEntity.getDevice();
        var renderer = CUDeviceRanderers.getRenderer(device);

        ItemStack displayStack = blockEntity.getDeviceItem();
        if(displayStack != null && !displayStack.isEmpty()) {

            float step = partialTick;
            if(blockEntity.clientAge == blockEntity.clientPrevFrameAge) {
                step -= blockEntity.clientPrevFramePartialTick;
            }
            blockEntity.clientPrevFramePartialTick = partialTick;
            blockEntity.clientPrevFrameAge = blockEntity.clientAge;

            Aim current = blockEntity.getCurrentAim();
            Aim last = blockEntity.getLastAim();

            Aim partial = new Aim(0, 0).set(last);

            if(!blockEntity.isAligned()) {
                //Quaterniond partial = last.slerp(current, partialTick, new Quaterniond());
                partial.stepPerAxis(current, blockEntity.deltaAnglePitch * step, blockEntity.deltaAngleYaw * step, partial);
                blockEntity.setLastAim(new Aim(0,0).set(partial));
            }

            var world = (ClientWorld)blockEntity.getWorld();
            assert world != null;

            var pos = blockEntity.getPos();
            var context = new IDeviceRenderer.Context(world.getBlockState(pos), pos, blockModelRenderer, itemRenderer);

            poseStack.push();
            poseStack.translate(0.5, 0.5, 0.5);
            poseStack.multiply(y180);
            poseStack.multiply(new Quaternionf().set(partial.toQuaternion()));
            poseStack.scale(0.5f, 0.5f, 0.5f);
            renderer.render(world, blockEntity,device, blockEntity.getState(), context, partialTick, poseStack, bufferSource, packedLight, packedOverlay);
            poseStack.pop();
        }
    }
}
