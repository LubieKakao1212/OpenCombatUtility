package com.LubieKakao1212.opencu.forge.packet;

import com.LubieKakao1212.opencu.common.network.packet.devicecontainer.frame.PacketS2CUpdateDevice;
import com.LubieKakao1212.opencu.common.network.packet.devicecontainer.frame.PacketS2CUpdateFrameAim;
import com.LubieKakao1212.opencu.common.network.packet.devicecontainer.frame.PacketC2SRequestDeviceUpdate;
import com.LubieKakao1212.opencu.common.network.packet.projectile.PacketS2CUpdateFireball;
import net.minecraft.network.PacketByteBuf;

public class PacketSerialize {

    public static void toBytes(PacketS2CUpdateDevice packet, PacketByteBuf buffer) {
        buffer.writeBlockPos(packet.position());
        buffer.writeItemStack(packet.newDispenser());
    }

    public static void toBytes(PacketS2CUpdateFrameAim packet, PacketByteBuf buffer) {
        buffer.writeBoolean(packet.hard());
        buffer.writeBlockPos(packet.position());
        buffer.writeFloat(packet.pitch());
        buffer.writeFloat(packet.yaw());
    }

    public static void toBytes(PacketC2SRequestDeviceUpdate packet, PacketByteBuf buffer) {
        buffer.writeBlockPos(packet.position());
    }

    public static void toBytes(PacketS2CUpdateFireball packet, PacketByteBuf buffer) {
        buffer.writeInt(packet.entityId());
        buffer.writeFloat(packet.powX());
        buffer.writeFloat(packet.powY());
        buffer.writeFloat(packet.powZ());
    }

    public static class ClientUpdateDispenser {
        public static PacketS2CUpdateDevice fromBytes(PacketByteBuf buffer) {
            return new PacketS2CUpdateDevice(buffer.readBlockPos(), buffer.readItemStack());
        }
    }

    public static class ClientUpdateDispenserAim {
        public static PacketS2CUpdateFrameAim fromBytes(PacketByteBuf buffer) {
            return new PacketS2CUpdateFrameAim(buffer.readBoolean(), buffer.readBlockPos(),
                    buffer.readFloat(),
                    buffer.readFloat());
        }
    }

    public static class ServerRequestDispenserUpdate {
        public static PacketC2SRequestDeviceUpdate fromBytes(PacketByteBuf buffer) {
            return new PacketC2SRequestDeviceUpdate(buffer.readBlockPos());
        }
    }

    public static class ClientUpdateFireball {
        public static PacketS2CUpdateFireball fromBytes(PacketByteBuf buffer) {
            return new PacketS2CUpdateFireball(buffer.readInt(),
                    buffer.readFloat(),
                    buffer.readFloat(),
                    buffer.readFloat());
        }
    }

}
