package com.LubieKakao1212.opencu.common.device.renderer;

import com.LubieKakao1212.opencu.common.device.IDeviceContainer;
import com.LubieKakao1212.opencu.common.device.IFramedDevice;
import com.LubieKakao1212.opencu.common.device.state.IDeviceState;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface IDeviceRenderer<TDevice extends IFramedDevice, TState extends IDeviceState> {

    void render(@NotNull World world, @NotNull IDeviceContainer container, @NotNull TDevice device, @NotNull TState state, @Nullable ItemStack deviceItem, float partialTick, @NotNull MatrixStack poseStack, @NotNull VertexConsumerProvider bufferSource, int packedLight, int packedOverlay);

}
