package com.LubieKakao1212.opencu.forge.packet;

import com.LubieKakao1212.opencu.common.device.state.RepulsorDeviceState;
import com.LubieKakao1212.opencu.common.network.packet.device.repulsor.*;
import com.LubieKakao1212.opencu.common.network.packet.devicecontainer.PacketC2SRequestDCState;
import com.LubieKakao1212.opencu.common.network.packet.devicecontainer.PacketS2CUpdateEnergy;
import com.LubieKakao1212.opencu.common.network.packet.devicecontainer.frame.*;
import com.LubieKakao1212.opencu.common.network.packet.generic.PacketC2SCycleRedstoneControl;
import com.LubieKakao1212.opencu.common.network.packet.generic.PacketS2CUpdateRedstoneControl;
import com.LubieKakao1212.opencu.common.network.packet.projectile.PacketS2CUpdateFireball;
import com.LubieKakao1212.opencu.common.network.packet.screen.PacketC2SRequestAmmoSlotToggle;
import com.LubieKakao1212.opencu.common.util.RedstoneControlType;
import net.minecraft.network.PacketByteBuf;

public class PacketSerialize {

    //region Server to Client
    //region PacketS2CUpdateFireball
    public static void toBytes(PacketS2CUpdateFireball packet, PacketByteBuf buffer) {
        buffer.writeInt(packet.entityId());
        buffer.writeFloat(packet.powX());
        buffer.writeFloat(packet.powY());
        buffer.writeFloat(packet.powZ());
    }

    public static PacketS2CUpdateFireball fromBytes_S2CUpdateFireball(PacketByteBuf buffer) {
        return new PacketS2CUpdateFireball(buffer.readInt(),
                buffer.readFloat(),
                buffer.readFloat(),
                    buffer.readFloat());
    }
    //endregion
    //region PacketS2CUpdateEnergy
    public static void toBytes(PacketS2CUpdateEnergy packet, PacketByteBuf buffer) {
        buffer.writeBlockPos(packet.position());
        buffer.writeLong(packet.amount());
    }

    public static PacketS2CUpdateEnergy fromBytes_S2CUpdateEnergy(PacketByteBuf buffer) {
        return new PacketS2CUpdateEnergy(
                buffer.readBlockPos(),
                buffer.readLong());
    }
    //endregion
    //region PacketS2CUpdateRequiresLock
    public static void toBytes(PacketS2CUpdateRequiresLock packet, PacketByteBuf buffer) {
        buffer.writeBlockPos(packet.position());
        buffer.writeBoolean(packet.state());
    }

    public static PacketS2CUpdateRequiresLock fromBytes_S2CUpdateRequiresLock(PacketByteBuf buffer) {
        return new PacketS2CUpdateRequiresLock(
                buffer.readBlockPos(),
                buffer.readBoolean());
    }
    //endregion
    //region PacketS2CUpdateRedstoneControl
    public static void toBytes(PacketS2CUpdateRedstoneControl packet, PacketByteBuf buffer) {
        buffer.writeBlockPos(packet.position());
        buffer.writeEnumConstant(packet.type());
    }

    public static PacketS2CUpdateRedstoneControl fromBytes_S2CUpdateRedstoneControl(PacketByteBuf buffer) {
        return new PacketS2CUpdateRedstoneControl(
                buffer.readBlockPos(),
                buffer.readEnumConstant(RedstoneControlType.class));
    }

    //endregion

    //region PacketS2CUpdateDevice
    public static void toBytes(PacketS2CUpdateDevice packet, PacketByteBuf buffer) {
        buffer.writeBlockPos(packet.position());
        buffer.writeItemStack(packet.newDispenser());
    }
    public static PacketS2CUpdateDevice fromBytes_S2CUpdateDevice(PacketByteBuf buffer) {
        return new PacketS2CUpdateDevice(buffer.readBlockPos(), buffer.readItemStack());
    }
    //endregion
    //region PacketS2CUpdateFrameAim
    public static void toBytes(PacketS2CUpdateFrameAim packet, PacketByteBuf buffer) {
        buffer.writeBoolean(packet.hard());
        buffer.writeBlockPos(packet.position());
        buffer.writeFloat(packet.pitch());
        buffer.writeFloat(packet.yaw());
    }
    public static PacketS2CUpdateFrameAim fromBytes_S2CUpdateFrameAim(PacketByteBuf buffer) {
        return new PacketS2CUpdateFrameAim(buffer.readBoolean(), buffer.readBlockPos(),
                buffer.readFloat(),
                buffer.readFloat());
    }
    //endregion

    //region PacketS2CUpdateRepulsorProperty
    public static void toBytes(PacketS2CUpdateRepulsorProperty packet, PacketByteBuf buffer) {
        buffer.writeBlockPos(packet.position());
        buffer.writeEnumConstant(packet.property());
        buffer.writeFloat(packet.value());
    }
    public static PacketS2CUpdateRepulsorProperty fromBytes_S2CUpdateRepulsorProperty(PacketByteBuf buffer) {
        return new PacketS2CUpdateRepulsorProperty(buffer.readBlockPos(),
                buffer.readEnumConstant(RepulsorDeviceState.Property.class),
                buffer.readFloat());
    }
    //endregion
    //region PacketS2CRepulsorActivationTimestamp
    public static void toBytes(PacketS2CRepulsorActivationTimestamp packet, PacketByteBuf buffer) {
        buffer.writeBlockPos(packet.position());
        buffer.writeLong(packet.timestamp());
    }
    public static PacketS2CRepulsorActivationTimestamp fromBytes_S2CRepulsorActivationTimestamp(PacketByteBuf buffer) {
        return new PacketS2CRepulsorActivationTimestamp(buffer.readBlockPos(),
                buffer.readLong());
    }
    //endregion
    //region PacketS2CUpdatePulseType
    public static void toBytes(PacketS2CUpdatePulseType packet, PacketByteBuf buffer) {
        buffer.writeBlockPos(packet.position());
        buffer.writeIdentifier(packet.value());
    }
    public static PacketS2CUpdatePulseType fromBytes_S2CUpdatePulseType(PacketByteBuf buffer) {
        return new PacketS2CUpdatePulseType(buffer.readBlockPos(),
                buffer.readIdentifier());
    }
    //endregion
    //endregion

    //region Client to Server
    //region PacketC2SRequestDeviceUpdate
    public static void toBytes(PacketC2SRequestDeviceUpdate packet, PacketByteBuf buffer) {
        buffer.writeBlockPos(packet.position());
    }
    public static PacketC2SRequestDeviceUpdate fromBytes_C2SRequestDeviceUpdate(PacketByteBuf buffer) {
        return new PacketC2SRequestDeviceUpdate(buffer.readBlockPos());
    }
    //endregion
    //region PacketC2SToggleRequiresLock
    public static void toBytes(PacketC2SToggleRequiresLock packet, PacketByteBuf buffer) {
        buffer.writeBlockPos(packet.position());
    }
    public static PacketC2SToggleRequiresLock fromBytes_C2SToggleRequiresLock(PacketByteBuf buffer) {
        return new PacketC2SToggleRequiresLock(buffer.readBlockPos());
    }
    //endregion
    //region PacketC2SCycleRedstoneControl
    public static void toBytes(PacketC2SCycleRedstoneControl packet, PacketByteBuf buffer) {
        buffer.writeBlockPos(packet.position());
    }
    public static PacketC2SCycleRedstoneControl fromBytes_C2SCycleRedstoneControl(PacketByteBuf buffer) {
        return new PacketC2SCycleRedstoneControl(buffer.readBlockPos());
    }
    //endregion
    //region PacketC2SRequestDCState
    public static void toBytes(PacketC2SRequestDCState packet, PacketByteBuf buffer) {
        buffer.writeBlockPos(packet.position());
    }
    public static PacketC2SRequestDCState fromBytes_C2SRequestDCState(PacketByteBuf buffer) {
        return new PacketC2SRequestDCState(buffer.readBlockPos());
    }
    //endregion
    //region PacketC2SRequestAmmoSlotToggle
    public static void toBytes(PacketC2SRequestAmmoSlotToggle packet, PacketByteBuf buffer) {
        buffer.writeInt(packet.syncId());
        buffer.writeBoolean(packet.visible());
    }
    public static PacketC2SRequestAmmoSlotToggle fromBytes_C2SRequestAmmoSlotToggle(PacketByteBuf buffer) {
        return new PacketC2SRequestAmmoSlotToggle(buffer.readInt(), buffer.readBoolean());
    }
    //endregion
    //region PacketC2SUpdateRepulsorProperty
    public static void toBytes(PacketC2SUpdateRepulsorProperty packet, PacketByteBuf buffer) {
        buffer.writeBlockPos(packet.position());
        buffer.writeEnumConstant(packet.property());
        buffer.writeFloat(packet.value());
    }
    public static PacketC2SUpdateRepulsorProperty fromBytes_C2SUpdateRepulsorProperty(PacketByteBuf buffer) {
        return new PacketC2SUpdateRepulsorProperty(buffer.readBlockPos(),
                buffer.readEnumConstant(RepulsorDeviceState.Property.class),
                buffer.readFloat());
    }
    //endregion
    //region PacketC2SUpdateRepulsorProperty
    public static void toBytes(PacketC2SUpdatePulseType packet, PacketByteBuf buffer) {
        buffer.writeBlockPos(packet.position());
        buffer.writeIdentifier(packet.type());
    }
    public static PacketC2SUpdatePulseType fromBytes_C2SUpdatePulseType(PacketByteBuf buffer) {
        return new PacketC2SUpdatePulseType(buffer.readBlockPos(),
                buffer.readIdentifier());
    }
    //endregion
    //region PacketC2SToggleForceSign
    public static void toBytes(PacketC2SToggleForceSign packet, PacketByteBuf buffer) {
        buffer.writeBlockPos(packet.position());
    }
    public static PacketC2SToggleForceSign fromBytes_C2SToggleForceSign(PacketByteBuf buffer) {
        return new PacketC2SToggleForceSign(buffer.readBlockPos());
    }
    //endregion
    //endregion
}
