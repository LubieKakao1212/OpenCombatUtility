package com.LubieKakao1212.opencu.common.network.packet;

import com.LubieKakao1212.opencu.common.OpenCUModCommon;
import com.LubieKakao1212.opencu.common.block.entity.BlockEntityDeviceContainer;
import com.LubieKakao1212.opencu.common.block.entity.BlockEntityModularFrame;
import com.LubieKakao1212.opencu.common.block.entity.IRedstoneControlled;
import com.LubieKakao1212.opencu.common.compat.valkyrienskies.VS2SoftUtil;
import com.LubieKakao1212.opencu.common.network.packet.devicecontainer.PacketC2SRequestDCState;
import com.LubieKakao1212.opencu.common.network.packet.devicecontainer.frame.PacketC2SRequestDeviceUpdate;
import com.LubieKakao1212.opencu.common.network.packet.devicecontainer.frame.PacketC2SToggleRequiresLock;
import com.LubieKakao1212.opencu.common.network.packet.generic.PacketC2SCycleRedstoneControl;
import com.LubieKakao1212.opencu.common.network.packet.screen.PacketC2SRequestAmmoSlotToggle;
import com.LubieKakao1212.opencu.common.screen.handler.DeviceContainerScreenHandler;
import com.lubiekakao1212.qulib.math.mc.Vector3m;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkSectionPos;
import net.minecraft.world.World;
import oshi.jna.platform.windows.NtDll;

public class PacketHandlersServer {

    public static void handle(PacketC2SRequestDeviceUpdate packetIn, ServerPlayerEntity sender) {
        World world = sender.getWorld();

        var position = packetIn.position();

        if(validatePacket(sender, position)) {
            BlockEntity be = world.getBlockEntity(position);

            if(be instanceof BlockEntityModularFrame) {
                ((BlockEntityModularFrame) be).sendDispenserUpdateTo(sender);
            }
        }
        else {
            OpenCUModCommon.LOGGER.warn("Potentially malicious packet received, skipping");
        }
    }

    public static void handle(PacketC2SToggleRequiresLock packetIn, ServerPlayerEntity sender) {
        var world = sender.getWorld();

        var position = packetIn.position();

        if(validatePacket(sender, position)) {
            BlockEntity be = world.getBlockEntity(position);

            if(be instanceof BlockEntityModularFrame frame) {
                frame.setRequiresLock(!frame.isRequiresLock());
            }
        }
        else {
            OpenCUModCommon.LOGGER.warn("Potentially malicious packet received, skipping");
        }
    }

    public static void handle(PacketC2SCycleRedstoneControl packetIn, ServerPlayerEntity sender) {
        var world = sender.getWorld();

        var position = packetIn.position();

        if(validatePacket(sender, position)) {
            BlockEntity be = world.getBlockEntity(position);

            if(be instanceof IRedstoneControlled rsControlled) {
                rsControlled.cycleRedstoneControl();
            }
        }
        else {
            OpenCUModCommon.LOGGER.warn("Potentially malicious packet received, skipping");
        }
    }

    public static void handle(PacketC2SRequestDCState packetIn, ServerPlayerEntity sender) {
        var world = sender.getWorld();

        var position = packetIn.position();

        if(validatePacket(sender, position)) {
            BlockEntity be = world.getBlockEntity(position);

            if(be instanceof BlockEntityDeviceContainer dc) {
                dc.sendStateTo(sender);
            }
        }
        else {
            OpenCUModCommon.LOGGER.warn("Potentially malicious packet received, skipping");
        }
    }

    public static void handle(PacketC2SRequestAmmoSlotToggle packetIn, ServerPlayerEntity sender) {
        if(sender.currentScreenHandler != null && sender.currentScreenHandler.syncId == packetIn.syncId()) {
            var handler = (DeviceContainerScreenHandler) sender.currentScreenHandler;
            handler.setAmmoSlotVisibilityServer(packetIn.visible());
        }
    }

    private static boolean validatePacket(ServerPlayerEntity player, BlockPos requestedPos) {
        return VS2SoftUtil.getDistanceSqr(player.getWorld(), new Vector3m(player.getPos()), new Vector3m(requestedPos)) < (256 * 256) /* TODO Add values to config */ &&
                player.getWorld().isChunkLoaded(ChunkSectionPos.getSectionCoord(requestedPos.getX()),ChunkSectionPos.getSectionCoord(requestedPos.getZ()));
    }
}
