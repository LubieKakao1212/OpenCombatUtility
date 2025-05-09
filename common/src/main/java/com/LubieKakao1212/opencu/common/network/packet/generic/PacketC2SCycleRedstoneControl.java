package com.LubieKakao1212.opencu.common.network.packet.generic;

import com.LubieKakao1212.opencu.common.network.packet.IPositionPacket;
import net.minecraft.util.math.BlockPos;

public record PacketC2SCycleRedstoneControl(BlockPos position) implements IPositionPacket { }
