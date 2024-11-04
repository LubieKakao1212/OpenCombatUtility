package com.LubieKakao1212.opencu.common.device.renderer;

import com.LubieKakao1212.opencu.common.device.IDeviceContainer;
import com.LubieKakao1212.opencu.common.device.IFramedDevice;
import com.LubieKakao1212.opencu.common.device.state.IDeviceState;
import net.minecraft.block.BlockState;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.BlockModelRenderer;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface IDeviceRenderer {

    void render(@NotNull ClientWorld world, @NotNull IDeviceContainer container, @NotNull IFramedDevice device, @NotNull IDeviceState state, @NotNull Context context, float partialTick, @NotNull MatrixStack matrixStack, @NotNull VertexConsumerProvider bufferSource, int packedLight, int packedOverlay);

    record Context(@Nullable BlockState blockState, @NotNull BlockPos worldPos, @NotNull BlockModelRenderer blockModelRenderer, @NotNull ItemRenderer itemRenderer) { }
}
