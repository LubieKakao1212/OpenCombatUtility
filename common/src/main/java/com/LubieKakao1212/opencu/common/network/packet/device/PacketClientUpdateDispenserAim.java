package com.LubieKakao1212.opencu.common.network.packet.device;

import com.lubiekakao1212.qulib.math.Aim;
import net.minecraft.util.math.BlockPos;

public record PacketClientUpdateDispenserAim(boolean hard, BlockPos position, float pitch, float yaw) {

    public static PacketClientUpdateDispenserAim create(BlockPos pos, Aim aim, boolean hard) {
        return new PacketClientUpdateDispenserAim(
                hard,
                pos,
                (float)aim.getPitch(),
                (float)aim.getYaw()
        );
    }

}
