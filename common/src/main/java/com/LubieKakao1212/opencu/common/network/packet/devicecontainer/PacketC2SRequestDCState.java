package com.LubieKakao1212.opencu.common.network.packet.devicecontainer;

import net.minecraft.util.math.BlockPos;

/**
 * Request DeviceContainer State
 */
public record PacketC2SRequestDCState(BlockPos position) { }
