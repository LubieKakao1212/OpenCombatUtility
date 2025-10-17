package com.LubieKakao1212.opencu.common.network.packet;

import com.LubieKakao1212.opencu.common.OpenCUModCommon;
import com.LubieKakao1212.opencu.common.block.entity.BlockEntityDeviceContainer;
import com.LubieKakao1212.opencu.common.block.entity.BlockEntityModularFrame;
import com.LubieKakao1212.opencu.common.device.state.RepulsorDeviceState;
import com.LubieKakao1212.opencu.common.network.packet.device.repulsor.PacketS2CRepulsorActivationTimestamp;
import com.LubieKakao1212.opencu.common.network.packet.device.repulsor.PacketS2CUpdatePulseType;
import com.LubieKakao1212.opencu.common.network.packet.devicecontainer.PacketS2CUpdateEnergy;
import com.LubieKakao1212.opencu.common.network.packet.devicecontainer.frame.PacketS2CUpdateDevice;
import com.LubieKakao1212.opencu.common.network.packet.devicecontainer.frame.PacketS2CUpdateFrameAim;
import com.LubieKakao1212.opencu.common.network.packet.device.repulsor.PacketS2CUpdateRepulsorProperty;
import com.LubieKakao1212.opencu.common.network.packet.devicecontainer.frame.PacketS2CUpdateRequiresLock;
import com.LubieKakao1212.opencu.common.network.packet.generic.PacketS2CUpdateRedstoneControl;
import com.LubieKakao1212.opencu.common.network.packet.projectile.PacketS2CUpdateFireball;
import com.lubiekakao1212.qulib.math.Aim;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.projectile.AbstractFireballEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;

public class PacketHandlersClient {

    public static void handle(PacketS2CUpdateFireball packetIn) {
        var player = MinecraftClient.getInstance().player;
        assert player != null;
        World level = player.getWorld();

        Entity entity = level.getEntityById(packetIn.entityId());

        if(entity instanceof AbstractFireballEntity) {
            AbstractFireballEntity fireball = ((AbstractFireballEntity) entity);
            fireball.powerX = packetIn.powX();
            fireball.powerY = packetIn.powY();
            fireball.powerZ = packetIn.powZ();
        }
    }

    //region Device Containers
    public static void handle(PacketS2CUpdateEnergy packetIn) {
        handleForContainer(packetIn, (frame, packet) -> frame.setClientEnergy(packetIn.amount()), PacketHandlersClient::logFailedCastDC);
    }

    public static void handle(PacketS2CUpdateRedstoneControl packetIn) {
        handleForContainer(packetIn, (frame, packet) -> frame.setRedstoneControlType(packetIn.type()), PacketHandlersClient::logFailedCastDC);
    }

    //region Modular Frame
    public static void handle(PacketS2CUpdateRequiresLock packetIn) {
        handleForFrame(packetIn, (frame, packet) -> frame.setRequiresLock(packet.state()), PacketHandlersClient::logFailedCastMF);
    }

    public static void handle(PacketS2CUpdateDevice packet) {
        handleForFrame(packet, (frame, packetS2CUpdateDevice) -> frame.setCurrentDeviceItem(packet.newDispenser()), PacketHandlersClient::logFailedCastMF);
    }

    public static void handle(PacketS2CUpdateFrameAim packetIn) {
        handleForFrame(packetIn, (frame, packet) -> {
            var aim = new Aim(packet.pitch(), packet.yaw());
            frame.setCurrentAim(aim);
            if(packet.hard()) {
                frame.setCurrentAim(aim);
            }
        }, PacketHandlersClient::logFailedCastMF);
    }
    //endregion

    //endregion

    //region Repulsor
    //OpenCUModCommon.LOGGER.warn("No device container with Repulsor device found at: " + packet.position());

    public static void handle(PacketS2CRepulsorActivationTimestamp packet) {
        handleForContainer(packet, (container, p) -> {
            var state = container.getState();
            if(state instanceof RepulsorDeviceState repState) {
                repState.setLastActivationTimestamp(packet.timestamp());
            }
        }, PacketHandlersClient::logFailedCastDC);
    }

    public static void handle(PacketS2CUpdateRepulsorProperty packet) {
        handleForContainer(packet, (container, p) -> {
            var state = container.getState();
            if(state instanceof RepulsorDeviceState repState) {
                repState.setPropertyNormal(packet.property(), packet.value());
            }
        }, PacketHandlersClient::logFailedCastDC);
    }

    public static void handle(PacketS2CUpdatePulseType packet) {
        handleForContainer(packet, (container, p) -> {
            var state = container.getState();
            if(state instanceof RepulsorDeviceState repState) {
                repState.setPulseTypeId(p.value());
            }
        }, PacketHandlersClient::logFailedCastDC);
    }
    //endregion

    //region private

    private static <T extends IPositionPacket> void handleForFrame(T packetIn, BiConsumer<BlockEntityModularFrame, T> body, Consumer<BlockPos> onCastFailed) {
        handleFor(packetIn, PacketHandlersClient::castMF, body, onCastFailed);
    }

    private static <T extends IPositionPacket> void handleForContainer(T packetIn, BiConsumer<BlockEntityDeviceContainer, T> body, Consumer<BlockPos> onCastFailed) {
        handleFor(packetIn, PacketHandlersClient::castDC, body, onCastFailed);
    }

    private static <TPacket extends IPositionPacket, BE extends BlockEntity> void handleFor(TPacket packetIn, Function<BlockEntity, Optional<BE>> cast, BiConsumer<BE, TPacket> body, Consumer<BlockPos> onCastFailed) {
        var player = MinecraftClient.getInstance().player;
        assert player != null;
        World level =  player.getWorld();

        BlockEntity be = level.getBlockEntity(packetIn.position());

        cast.apply(be).ifPresentOrElse(be1 -> body.accept(be1, packetIn), () -> onCastFailed.accept(packetIn.position()));
//        if(be instanceof BlockEntityModularFrame frame) {
//            body.accept(frame, packetIn);
//        }
    }

    private static Optional<BlockEntityModularFrame> castMF(BlockEntity be) {
        return be instanceof BlockEntityModularFrame frame ? Optional.of(frame) : Optional.empty();
    }

    private static Optional<BlockEntityDeviceContainer> castDC(BlockEntity be) {
        return be instanceof BlockEntityDeviceContainer frame ? Optional.of(frame) : Optional.empty();
    }

    private static void logFailedCastDC(BlockPos pos) {
        OpenCUModCommon.LOGGER.warn("No device container found at: {}", pos);
    }

    private static void logFailedCastMF(BlockPos pos) {
        OpenCUModCommon.LOGGER.warn("No modular frame found at: {}", pos);
    }

    //endregion
}
