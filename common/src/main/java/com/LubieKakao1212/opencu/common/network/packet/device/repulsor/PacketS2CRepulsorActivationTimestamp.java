package com.LubieKakao1212.opencu.common.network.packet.device.repulsor;

import com.LubieKakao1212.opencu.common.network.packet.IPositionPacket;
import net.minecraft.util.math.BlockPos;

public record PacketS2CRepulsorActivationTimestamp(BlockPos position, long timestamp) implements IPositionPacket { }
