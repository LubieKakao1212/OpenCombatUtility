package com.LubieKakao1212.opencu.common.network.packet.device.repulsor;

import com.LubieKakao1212.opencu.common.device.state.RepulsorDeviceState;
import net.minecraft.util.math.BlockPos;

public record PacketS2CUpdateRepulsorProperty(BlockPos position, RepulsorDeviceState.Property property, float value) { }
