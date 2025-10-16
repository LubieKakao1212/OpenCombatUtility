package com.LubieKakao1212.opencu.common.network.packet.device.repulsor;

import com.LubieKakao1212.opencu.common.network.packet.IPositionPacket;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;

public record PacketC2SUpdatePulseType(BlockPos position, Identifier type) implements IPositionPacket { }
