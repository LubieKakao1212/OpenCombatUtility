package com.LubieKakao1212.opencu.common.network.packet.devicecontainer;

import com.LubieKakao1212.opencu.common.network.packet.IPositionPacket;
import net.minecraft.util.math.BlockPos;

/**
 * Request DeviceContainer State
 */
public record PacketC2SRequestDCState(BlockPos position) implements IPositionPacket { }
