package com.LubieKakao1212.opencu.network.packet.dispenser;

import com.LubieKakao1212.opencu.OpenCUMod;
import com.LubieKakao1212.opencu.block.entity.BlockEntityOmniDispenser;
import com.LubieKakao1212.opencu.compat.valkyrienskies.VS2SoftUtil;
import com.LubieKakao1212.opencu.network.IOCUPacket;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.network.NetworkEvent;
import org.joml.Vector3d;

import java.util.function.Supplier;

public class RequestDispenserUpdatePacket implements IOCUPacket {

    private BlockPos position;

    public RequestDispenserUpdatePacket() { }

    public RequestDispenserUpdatePacket(BlockPos position) {
        this.position = position;
    }


    public void toBytes(FriendlyByteBuf buf) {
        buf.writeInt(position.getX());
        buf.writeInt(position.getY());
        buf.writeInt(position.getZ());
    }

    public static RequestDispenserUpdatePacket fromBytes(ByteBuf buf) {
        return new RequestDispenserUpdatePacket(new BlockPos(
                buf.readInt(),
                buf.readInt(),
                buf.readInt()
        ));
    }

    public void handle(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ServerPlayer sender = ctx.get().getSender();
            Level level = sender.getLevel();

            if(VS2SoftUtil.getDistanceSqr(level, new Vector3d(sender.getX(), sender.getY(), sender.getY()), new Vector3d(position.getX(), position.getY(), position.getZ())) < (64 * 64)) {
                BlockEntity be = level.getBlockEntity(position);

                if(be instanceof BlockEntityOmniDispenser) {
                    ((BlockEntityOmniDispenser) be).sendDispenserUpdateTo(sender);
                }
            }else
            {
                OpenCUMod.LOGGER.warn("Potentially malicious packet received, skipping");
            }
        });
        ctx.get().setPacketHandled(true);
    }
}
