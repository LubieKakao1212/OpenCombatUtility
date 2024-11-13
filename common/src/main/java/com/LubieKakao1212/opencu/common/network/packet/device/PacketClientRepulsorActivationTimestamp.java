package com.LubieKakao1212.opencu.common.network.packet.device;

import net.minecraft.util.math.BlockPos;

public record PacketClientRepulsorActivationTimestamp(BlockPos position, long timestamp) { }
