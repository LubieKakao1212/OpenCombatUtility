package com.LubieKakao1212.opencu.common.network.packet.device.repulsor;

import net.minecraft.util.math.BlockPos;

public record PacketS2CUpdateRepulsorBlend(BlockPos position, float value) {
}
