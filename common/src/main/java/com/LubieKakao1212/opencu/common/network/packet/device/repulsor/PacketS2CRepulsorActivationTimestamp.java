package com.LubieKakao1212.opencu.common.network.packet.device.repulsor;

import net.minecraft.util.math.BlockPos;

public record PacketS2CRepulsorActivationTimestamp(BlockPos position, long timestamp) { }
