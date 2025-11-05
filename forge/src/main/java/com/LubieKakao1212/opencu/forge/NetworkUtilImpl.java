package com.LubieKakao1212.opencu.forge;

import com.LubieKakao1212.opencu.NetworkUtil;
import com.LubieKakao1212.opencu.common.OpenCUModCommon;
import com.LubieKakao1212.opencu.common.network.packet.*;
import com.LubieKakao1212.opencu.common.network.packet.device.repulsor.*;
import com.LubieKakao1212.opencu.common.network.packet.devicecontainer.PacketC2SRequestDCState;
import com.LubieKakao1212.opencu.common.network.packet.devicecontainer.PacketS2CUpdateEnergy;
import com.LubieKakao1212.opencu.common.network.packet.devicecontainer.frame.*;
import com.LubieKakao1212.opencu.common.network.packet.generic.PacketC2SCycleRedstoneControl;
import com.LubieKakao1212.opencu.common.network.packet.generic.PacketS2CUpdateRedstoneControl;
import com.LubieKakao1212.opencu.common.network.packet.projectile.PacketS2CUpdateFireball;
import com.LubieKakao1212.opencu.common.network.packet.screen.PacketC2SRequestAmmoSlotToggle;
import com.LubieKakao1212.opencu.forge.packet.PacketSerialize;
import net.minecraft.entity.Entity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.chunk.WorldChunk;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;

import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;

@Mod.EventBusSubscriber
public class NetworkUtilImpl {

    private static final String version = "2";

    private static SimpleChannel CHANNEL;

    public static void init() {

        CHANNEL = NetworkRegistry.ChannelBuilder.named(new Identifier(OpenCUModCommon.MODID, "network"))
                .networkProtocolVersion(() -> version)
                .clientAcceptedVersions(version::equals)
                .serverAcceptedVersions(version::equals)
                .simpleChannel();

        int id = 0;
        //region Server to Client
        registerServer2Client(id++, PacketS2CUpdateFireball.class, PacketSerialize::toBytes, PacketSerialize::fromBytes_S2CUpdateFireball, PacketHandlersClient::handle);
        registerServer2Client(id++, PacketS2CUpdateEnergy.class, PacketSerialize::toBytes, PacketSerialize::fromBytes_S2CUpdateEnergy, PacketHandlersClient::handle);
        registerServer2Client(id++, PacketS2CUpdateRequiresLock.class, PacketSerialize::toBytes, PacketSerialize::fromBytes_S2CUpdateRequiresLock, PacketHandlersClient::handle);
        registerServer2Client(id++, PacketS2CUpdateRedstoneControl.class, PacketSerialize::toBytes, PacketSerialize::fromBytes_S2CUpdateRedstoneControl, PacketHandlersClient::handle);

        registerServer2Client(id++, PacketS2CUpdateDevice.class, PacketSerialize::toBytes, PacketSerialize::fromBytes_S2CUpdateDevice, PacketHandlersClient::handle);
        registerServer2Client(id++, PacketS2CUpdateFrameAim.class, PacketSerialize::toBytes, PacketSerialize::fromBytes_S2CUpdateFrameAim, PacketHandlersClient::handle);

        registerServer2Client(id++, PacketS2CUpdateRepulsorProperty.class, PacketSerialize::toBytes, PacketSerialize::fromBytes_S2CUpdateRepulsorProperty, PacketHandlersClient::handle);
        registerServer2Client(id++, PacketS2CRepulsorActivationTimestamp.class, PacketSerialize::toBytes, PacketSerialize::fromBytes_S2CRepulsorActivationTimestamp, PacketHandlersClient::handle);
        registerServer2Client(id++, PacketS2CUpdatePulseType.class, PacketSerialize::toBytes, PacketSerialize::fromBytes_S2CUpdatePulseType, PacketHandlersClient::handle);
        //endregion

        //region Client to Server
        registerClient2Server(id++, PacketC2SRequestDeviceUpdate.class, PacketSerialize::toBytes, PacketSerialize::fromBytes_C2SRequestDeviceUpdate, PacketHandlersServer::handle);
        registerClient2Server(id++, PacketC2SToggleRequiresLock.class, PacketSerialize::toBytes, PacketSerialize::fromBytes_C2SToggleRequiresLock, PacketHandlersServer::handle);
        registerClient2Server(id++, PacketC2SCycleRedstoneControl.class, PacketSerialize::toBytes, PacketSerialize::fromBytes_C2SCycleRedstoneControl, PacketHandlersServer::handle);
        registerClient2Server(id++, PacketC2SRequestDCState.class, PacketSerialize::toBytes, PacketSerialize::fromBytes_C2SRequestDCState, PacketHandlersServer::handle);
        registerClient2Server(id++, PacketC2SRequestAmmoSlotToggle.class, PacketSerialize::toBytes, PacketSerialize::fromBytes_C2SRequestAmmoSlotToggle, PacketHandlersServer::handle);
        registerClient2Server(id++, PacketC2SUpdateRepulsorProperty.class, PacketSerialize::toBytes, PacketSerialize::fromBytes_C2SUpdateRepulsorProperty, PacketHandlersServer::handle);
        registerClient2Server(id++, PacketC2SUpdatePulseType.class, PacketSerialize::toBytes, PacketSerialize::fromBytes_C2SUpdatePulseType, PacketHandlersServer::handle);
        registerClient2Server(id++, PacketC2SToggleForceSign.class, PacketSerialize::toBytes, PacketSerialize::fromBytes_C2SToggleForceSign, PacketHandlersServer::handle);
        //endregion
    }

    public static <T extends Record> void sendToAllTracking(T message, ServerWorld world, BlockPos pos) {
        sendToAllTracking(message, world.getWorldChunk(pos));
    }

    public static <T extends Record> void sendToAllTracking(T message, WorldChunk chunk) {
        CHANNEL.send(PacketDistributor.TRACKING_CHUNK.with(() -> chunk), message);
    }

    public static <T extends Record> void sendToAllTracking(T message, Entity entity) {
        CHANNEL.send(PacketDistributor.TRACKING_ENTITY_AND_SELF.with(() -> entity), message);
    }

    public static <T extends Record> void sendToServer(T packet) {
        CHANNEL.sendToServer(packet);
    }

    public static <T extends Record> void sendToPlayer(T packet, ServerPlayerEntity player) {
        CHANNEL.send(PacketDistributor.PLAYER.with(() -> player), packet);
    }

    @SubscribeEvent
    @SuppressWarnings("unused")
    public static void serverTick(TickEvent.ServerTickEvent event) {
        NetworkUtil.tick();
    }

    private static <MSG extends Record> void registerServer2Client(int idx, Class<MSG> clazz, BiConsumer<MSG, PacketByteBuf> encoder, Function<PacketByteBuf, MSG> decoder, Consumer<MSG> handler) {
        CHANNEL.messageBuilder(clazz, idx, NetworkDirection.PLAY_TO_CLIENT)
                .encoder(encoder)
                .decoder(decoder)
                .consumerMainThread((msg, contextSupplier) -> handler.accept(msg))
                .add();
    }

    private static <MSG> void registerClient2Server(int idx, Class<MSG> clazz, BiConsumer<MSG, PacketByteBuf> encoder, Function<PacketByteBuf, MSG> decoder, BiConsumer<MSG, ServerPlayerEntity> handler) {
        CHANNEL.messageBuilder(clazz, idx, NetworkDirection.PLAY_TO_SERVER)
                .encoder(encoder)
                .decoder(decoder)
                .consumerMainThread((msg, contextSupplier) -> handler.accept(msg, contextSupplier.get().getSender()))
                .add();
    }
}
