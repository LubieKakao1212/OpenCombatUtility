package com.LubieKakao1212.opencu.common.network.packet.devicecontainer.frame;

import com.LubieKakao1212.opencu.common.network.packet.IPositionPacket;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;

public record PacketS2CUpdateDevice(BlockPos position, ItemStack newDispenser) implements IPositionPacket {
}
