package com.LubieKakao1212.opencu.common.network.packet.devicecontainer.frame;

import com.LubieKakao1212.opencu.common.network.packet.IPositionPacket;
import net.minecraft.util.math.BlockPos;

public record PacketC2SToggleRequiresLock(BlockPos position) implements IPositionPacket {
}
