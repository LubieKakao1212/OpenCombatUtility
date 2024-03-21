package com.LubieKakao1212.opencu.common.network.packet.dispenser;

import com.LubieKakao1212.opencu.common.block.entity.BlockEntityModularFrame;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.joml.Quaterniond;

public record UpdateDispenserAimPacketClientbound(boolean hard, BlockPos pos, float qx, float qy, float qz, float qw) {

    public static UpdateDispenserAimPacketClientbound create(BlockPos pos, Quaterniond aim, boolean hard) {
        return new UpdateDispenserAimPacketClientbound(
                hard,
                pos,
                (float)aim.x,
                (float)aim.y,
                (float)aim.z,
                (float)aim.w
        );
    }

    public static void execute(UpdateDispenserAimPacketClientbound packet) {
        var player = MinecraftClient.getInstance().player;
        assert player != null;
        World world = player.getWorld();

        BlockEntity te = world.getBlockEntity(packet.pos);

        var aim = new Quaterniond(packet.qx, packet.qy, packet.qz, packet.qw);

        if (te instanceof BlockEntityModularFrame) {
            ((BlockEntityModularFrame) te).setCurrentAction(aim);
            if(packet.hard) {
                //Sets last aim to aim
                ((BlockEntityModularFrame) te).setCurrentAction(aim);
            }
        }
    }

}
