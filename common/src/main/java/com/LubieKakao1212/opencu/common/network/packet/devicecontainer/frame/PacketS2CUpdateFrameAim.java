package com.LubieKakao1212.opencu.common.network.packet.devicecontainer.frame;

import com.LubieKakao1212.opencu.common.network.packet.IPositionPacket;
import com.lubiekakao1212.qulib.math.Aim;
import net.minecraft.util.math.BlockPos;

public record PacketS2CUpdateFrameAim(boolean hard, BlockPos position, float pitch, float yaw)  implements IPositionPacket {

    public static PacketS2CUpdateFrameAim create(BlockPos pos, Aim aim, boolean hard) {
        return new PacketS2CUpdateFrameAim(
                hard,
                pos,
                (float)aim.getPitch(),
                (float)aim.getYaw()
        );
    }

}
