package com.LubieKakao1212.opencu.common.network.packet.generic;

import com.LubieKakao1212.opencu.common.network.packet.IPositionPacket;
import com.LubieKakao1212.opencu.common.util.RedstoneControlType;
import net.minecraft.util.math.BlockPos;

public record PacketS2CUpdateRedstoneControl(BlockPos position, RedstoneControlType type) implements IPositionPacket { }
