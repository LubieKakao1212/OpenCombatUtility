package com.LubieKakao1212.opencu.common.network.packet.devicecontainer;

import com.LubieKakao1212.opencu.common.network.packet.IPositionPacket;
import net.minecraft.util.math.BlockPos;

public record PacketS2CUpdateEnergy(BlockPos position, long amount) implements IPositionPacket { }
