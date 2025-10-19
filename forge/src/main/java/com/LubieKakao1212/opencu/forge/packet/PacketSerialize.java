package com.LubieKakao1212.opencu.forge.packet;

import com.LubieKakao1212.opencu.common.network.packet.PacketClientRepulsorPulse;
import com.LubieKakao1212.opencu.common.network.packet.devicecontainer.frame.PacketS2CUpdateDevice;
import com.LubieKakao1212.opencu.common.network.packet.devicecontainer.frame.PacketS2CUpdateFrameAim;
import com.LubieKakao1212.opencu.common.network.packet.devicecontainer.frame.PacketC2SRequestDeviceUpdate;
import com.LubieKakao1212.opencu.common.network.packet.projectile.PacketS2CUpdateFireball;
import net.minecraft.network.PacketByteBuf;

public class PacketSerialize {

    public static void toBytes(PacketClientPlayerAddVelocity packet, PacketByteBuf buffer) {
        buffer.writeInt(packet.x());
        buffer.writeInt(packet.y());
        buffer.writeInt(packet.z());
    }

    public static void toBytes(PacketClientPlayerScaleVelocity packet, PacketByteBuf buffer) {
        buffer.writeFloat(packet.scale());
    }

    public static void toBytes(PacketS2CUpdateDevice packet, PacketByteBuf buffer) {
        buffer.writeBlockPos(packet.position());
        buffer.writeItemStack(packet.newDispenser());
    }

    public static void toBytes(PacketS2CUpdateFrameAim packet, PacketByteBuf buffer) {
        buffer.writeBoolean(packet.hard());
        buffer.writeBlockPos(packet.position());
        buffer.writeFloat(packet.qx());
        buffer.writeFloat(packet.qy());
        buffer.writeFloat(packet.qz());
        buffer.writeFloat(packet.qw());
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

    public static void toBytes(PacketClientRepulsorPulse packet, PacketByteBuf buffer) {
        buffer.writeBlockPos(packet.position());
    }

    public static class ClientPlayerAddVelocity {
        public static PacketClientPlayerAddVelocity fromBytes(PacketByteBuf buffer) {
            return new PacketClientPlayerAddVelocity(buffer.readInt(), buffer.readInt(), buffer.readInt());
        }
    }

    public static class ClientPlayerScaleVelocity {
        public static PacketClientPlayerScaleVelocity fromBytes(PacketByteBuf buffer) {
            return new PacketClientPlayerScaleVelocity(buffer.readFloat());
        }
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
                    buffer.readFloat(),
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

    public static class ClientRepulsorPulse {
        public static PacketClientRepulsorPulse fromBytes(PacketByteBuf buffer) {
            return new PacketClientRepulsorPulse(buffer.readBlockPos());
        }
    }

}
