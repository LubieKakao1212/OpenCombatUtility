package com.LubieKakao1212.opencu.fabric;

import com.LubieKakao1212.opencu.common.network.packet.PacketHandlersServer;
import com.LubieKakao1212.opencu.common.network.packet.devicecontainer.PacketC2SRequestDCState;
import com.LubieKakao1212.opencu.common.network.packet.devicecontainer.PacketS2CUpdateEnergy;
import com.LubieKakao1212.opencu.common.network.packet.devicecontainer.frame.*;
import com.LubieKakao1212.opencu.common.network.packet.device.repulsor.PacketS2CRepulsorActivationTimestamp;
import com.LubieKakao1212.opencu.common.network.packet.device.repulsor.PacketS2CUpdateRepulsorBlend;
import com.LubieKakao1212.opencu.common.network.packet.generic.PacketC2SCycleRedstoneControl;
import com.LubieKakao1212.opencu.common.network.packet.generic.PacketS2CUpdateRedstoneControl;
import com.LubieKakao1212.opencu.common.network.packet.projectile.PacketS2CUpdateFireball;
import com.LubieKakao1212.opencu.registry.CUIds;
import io.wispforest.owo.network.OwoNetChannel;
import net.minecraft.entity.Entity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;

import static com.LubieKakao1212.opencu.common.network.packet.PacketHandlersClient.handle;

public class NetworkUtilImpl {

    private static final OwoNetChannel CHANNEL = OwoNetChannel.create(CUIds.MAIN);

    public static void init() {
        CHANNEL.registerClientboundDeferred(PacketS2CUpdateFireball.class);

        CHANNEL.registerClientboundDeferred(PacketS2CUpdateEnergy.class);
        CHANNEL.registerClientboundDeferred(PacketS2CUpdateRequiresLock.class);
        CHANNEL.registerClientboundDeferred(PacketS2CUpdateRedstoneControl.class);

        CHANNEL.registerClientboundDeferred(PacketS2CUpdateDevice.class);
        CHANNEL.registerClientboundDeferred(PacketS2CUpdateFrameAim.class);

        CHANNEL.registerClientboundDeferred(PacketS2CUpdateRepulsorBlend.class);
        CHANNEL.registerClientboundDeferred(PacketS2CRepulsorActivationTimestamp.class);

        CHANNEL.registerServerbound(PacketC2SRequestDeviceUpdate.class, (pkt, acc) -> PacketHandlersServer.handle(pkt, acc.player()));
        CHANNEL.registerServerbound(PacketC2SToggleRequiresLock.class, (pkt, acc) -> PacketHandlersServer.handle(pkt, acc.player()));
        CHANNEL.registerServerbound(PacketC2SCycleRedstoneControl.class, (pkt, acc) -> PacketHandlersServer.handle(pkt, acc.player()));
        CHANNEL.registerServerbound(PacketC2SRequestDCState.class, (pkt, acc) -> PacketHandlersServer.handle(pkt, acc.player()));
    }

    public static void clientInit() {
        CHANNEL.registerClientbound(PacketS2CUpdateFireball.class, (pkt, acc) -> handle(pkt));

        CHANNEL.registerClientbound(PacketS2CUpdateEnergy.class, (pkt, acc) -> handle(pkt));
        CHANNEL.registerClientbound(PacketS2CUpdateRequiresLock.class, (pkt, acc) -> handle(pkt));
        CHANNEL.registerClientbound(PacketS2CUpdateRedstoneControl.class, (pkt, acc) -> handle(pkt));

        CHANNEL.registerClientbound(PacketS2CUpdateDevice.class, (pkt, acc) -> handle(pkt));
        CHANNEL.registerClientbound(PacketS2CUpdateFrameAim.class, (pkt, acc) -> handle(pkt));

        CHANNEL.registerClientbound(PacketS2CRepulsorActivationTimestamp.class, (pkt, acc) -> handle(pkt));

        CHANNEL.registerClientbound(PacketS2CUpdateRepulsorBlend.class, (pkt, acc) -> handle(pkt));
    }

    public static <T extends Record> void sendToAllTracking(T packet, ServerWorld world, BlockPos pos) {
        CHANNEL.serverHandle(world, pos).send(packet);
    }

    public static <T extends Record> void sendToServer(T packet) {
        CHANNEL.clientHandle().send(packet);
    }

    public static <T extends Record> void sendToPlayer(T packet, ServerPlayerEntity player) {
        CHANNEL.serverHandle(player).send(packet);
    }

    public static <T extends Record> void sendToAllTracking(T packet, Entity target) {
        sendToAllTracking(packet, (ServerWorld) target.getWorld(), target.getBlockPos());
    }
}
