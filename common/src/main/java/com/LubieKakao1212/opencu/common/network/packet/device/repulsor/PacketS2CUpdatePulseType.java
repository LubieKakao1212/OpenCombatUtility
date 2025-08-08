package com.LubieKakao1212.opencu.common.network.packet.device.repulsor;

import com.LubieKakao1212.opencu.common.network.packet.IPositionPacket;
import net.minecraft.util.math.BlockPos;

public record PacketS2CUpdatePulseType(BlockPos position, int value) implements IPositionPacket { }
