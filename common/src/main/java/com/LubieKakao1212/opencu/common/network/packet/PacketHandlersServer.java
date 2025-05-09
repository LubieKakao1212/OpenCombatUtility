package com.LubieKakao1212.opencu.common.network.packet;

import com.LubieKakao1212.opencu.common.OpenCUModCommon;
import com.LubieKakao1212.opencu.common.block.entity.BlockEntityDeviceContainer;
import com.LubieKakao1212.opencu.common.block.entity.BlockEntityModularFrame;
import com.LubieKakao1212.opencu.common.block.entity.IRedstoneControlled;
import com.LubieKakao1212.opencu.common.compat.valkyrienskies.VS2SoftUtil;
import com.LubieKakao1212.opencu.common.device.state.RepulsorDeviceState;
import com.LubieKakao1212.opencu.common.network.packet.device.repulsor.PacketC2SUpdateRepulsorProperty;
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

import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;

public class PacketHandlersServer {

    public static void handle(PacketC2SRequestDeviceUpdate packetIn, ServerPlayerEntity sender) {
        handleForFrame(packetIn, sender, frame -> frame.sendStateTo(sender));
    }

    public static void handle(PacketC2SToggleRequiresLock packetIn, ServerPlayerEntity sender) {
        handleForFrame(packetIn, sender, frame -> frame.setRequiresLock(!frame.isRequiresLock()));
    }

    public static void handle(PacketC2SCycleRedstoneControl packetIn, ServerPlayerEntity sender) {
        handleForRedstoneControlled(packetIn, sender, IRedstoneControlled::cycleRedstoneControl);
    }

    public static void handle(PacketC2SRequestDCState packetIn, ServerPlayerEntity sender) {
        handleForContainer(packetIn, sender, dc -> dc.sendStateTo(sender));
    }

    public static void handle(PacketC2SRequestAmmoSlotToggle packetIn, ServerPlayerEntity sender) {
        if(sender.currentScreenHandler != null && sender.currentScreenHandler.syncId == packetIn.syncId()) {
            var handler = (DeviceContainerScreenHandler) sender.currentScreenHandler;
            handler.setAmmoSlotVisibilityServer(packetIn.visible());
        }
    }

    public static void handle(PacketC2SUpdateRepulsorProperty packetIn, ServerPlayerEntity sender) {
        handleForContainer(packetIn, sender, dc -> {
            var state = dc.getState();
            if(state instanceof RepulsorDeviceState repState) {
                repState.setPropertyNormal(packetIn.property(), packetIn.value());
            }
        });
    }

    private static boolean validatePacket(ServerPlayerEntity player, BlockPos requestedPos) {
        return VS2SoftUtil.getDistanceSqr(player.getWorld(), new Vector3m(player.getPos()), new Vector3m(requestedPos)) < (256 * 256) /* TODO Add values to config */ &&
                player.getWorld().isChunkLoaded(ChunkSectionPos.getSectionCoord(requestedPos.getX()),ChunkSectionPos.getSectionCoord(requestedPos.getZ()));
    }

    //region private
    private static <T extends IPositionPacket> void handleForFrame(T packetIn, ServerPlayerEntity sender,  Consumer<BlockEntityModularFrame> body) {
        handleFor(packetIn, sender, PacketHandlersServer::castMF, body);
    }

    private static <T extends IPositionPacket> void handleForContainer(T packetIn, ServerPlayerEntity sender,  Consumer<BlockEntityDeviceContainer> body) {
        handleFor(packetIn, sender, PacketHandlersServer::castDC, body);
    }

    private static <T extends IPositionPacket> void handleForRedstoneControlled(T packetIn, ServerPlayerEntity sender, Consumer<IRedstoneControlled> body) {
        handleFor(packetIn, sender, PacketHandlersServer::castRC, body);
    }

    private static <TPacket extends IPositionPacket, BE> void handleFor(TPacket packetIn, ServerPlayerEntity sender, Function<BlockEntity, Optional<BE>> cast, Consumer<BE> body) {
        var world = sender.getWorld();

        var position = packetIn.position();

        if(validatePacket(sender, position)) {
            BlockEntity be = world.getBlockEntity(position);

            cast.apply(be).ifPresent(body);
        }
        else {
            OpenCUModCommon.LOGGER.warn("Potentially malicious packet received, skipping");
        }
    }

    private static Optional<BlockEntityModularFrame> castMF(BlockEntity be) {
        return be instanceof BlockEntityModularFrame frame ? Optional.of(frame) : Optional.empty();
    }

    private static Optional<BlockEntityDeviceContainer> castDC(BlockEntity be) {
        return be instanceof BlockEntityDeviceContainer dc ? Optional.of(dc) : Optional.empty();
    }

    private static Optional<IRedstoneControlled> castRC(BlockEntity be) {
        return be instanceof IRedstoneControlled rc ? Optional.of(rc) : Optional.empty();
    }
    //endregion
}
