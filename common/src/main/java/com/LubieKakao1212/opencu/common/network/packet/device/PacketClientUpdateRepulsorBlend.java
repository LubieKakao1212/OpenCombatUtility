package com.LubieKakao1212.opencu.common.network.packet.device;

import net.minecraft.util.math.BlockPos;

public record PacketClientUpdateRepulsorBlend(BlockPos position, float value) {
}
