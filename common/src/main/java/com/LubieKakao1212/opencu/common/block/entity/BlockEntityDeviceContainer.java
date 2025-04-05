package com.LubieKakao1212.opencu.common.block.entity;

import com.LubieKakao1212.opencu.NetworkUtil;
import com.LubieKakao1212.opencu.common.device.IDeviceContainer;
import com.LubieKakao1212.opencu.common.device.IFramedDevice;
import com.LubieKakao1212.opencu.common.device.state.IDeviceState;
import com.LubieKakao1212.opencu.common.network.packet.devicecontainer.PacketC2SRequestDCState;
import com.LubieKakao1212.opencu.common.network.packet.devicecontainer.PacketS2CUpdateEnergy;
import com.LubieKakao1212.opencu.common.network.packet.generic.PacketS2CUpdateRedstoneControl;
import com.LubieKakao1212.opencu.common.transaction.DeviceActivationContext;
import com.LubieKakao1212.opencu.common.util.Observer;
import com.LubieKakao1212.opencu.common.util.RedstoneControlType;
import com.lubiekakao1212.qulib.math.Aim;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3d;

import java.util.EnumSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;

public abstract class BlockEntityDeviceContainer extends BlockEntity implements IDeviceContainer {

    public static final int autoShootInterval = 10;

    private final AtomicInteger actionsToPerform = new AtomicInteger(0);

    private RedstoneControlType redstoneControlType;
    private long redstoneActivationTimer = 0;
    private final Set<Direction> rsState = EnumSet.noneOf(Direction.class);

    private IFramedDevice currentDevice;
    private IDeviceState currentDeviceState;

    protected Observer<Long> energyObserver;

    //region Client Fields
    private long clientEnergy;
    //endregion

    public BlockEntityDeviceContainer(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
        redstoneControlType = RedstoneControlType.PULSE;
    }

    protected void tickDeviceServer() {
        assert world != null;
        assert !world.isClient;

        if(currentDevice == null) {
            return;
        }

        try(var ctx = getNewContext()) {
            currentDevice.tick(this, currentDeviceState, world, pos, currentAim(), ctx);
        }

        if(++redstoneActivationTimer % autoShootInterval == 0) {
            var power = world.isReceivingRedstonePower(pos);
            var rsct = getRedstoneControlType();
            if(power && rsct == RedstoneControlType.HIGH) {
                actionsToPerform.getAndIncrement();
            }
            else if(!power && rsct == RedstoneControlType.LOW) {
                actionsToPerform.getAndIncrement();
            }
        }

        var atp = actionsToPerform.get();
        actionsToPerform.set(0);
        if(canActivate()) {
            while (atp > 0) {
                atp--;
                activate();
            }
        }

        //TODO optimise?
        markDirty();
    }

    public void pulseActivate(Direction direction, boolean state) {
        var lastState = rsState.contains(direction);

        if(state) {
            rsState.add(direction);
        }
        else {
            rsState.remove(direction);
        }

        if(getRedstoneControlType() == RedstoneControlType.PULSE && !lastState && state) {
            scheduleActivation();
        }
    }

    @Override
    public void cycleRedstoneControl() {
        setRedstoneControlType(redstoneControlType.cycleNext());
    }

    public void setRedstoneControlType(RedstoneControlType type) {
        this.redstoneControlType = type;
        if(world instanceof ServerWorld serverWorld) {
            NetworkUtil.sendToAllTracking(new PacketS2CUpdateRedstoneControl(pos, type), serverWorld, pos);
        }
    }

    @Override
    public RedstoneControlType getRedstoneControlType() {
        return redstoneControlType;
    }

    @Override
    public @NotNull IFramedDevice getDevice() {
        return currentDevice;
    }

    @Override
    public @NotNull IDeviceState getState() {
        return currentDeviceState;
    }

    @Override
    public void scheduleActivation() {
        actionsToPerform.getAndIncrement();
    }

    public void activate() {
        if(!canActivate()) {
            return;
        }
        assert world != null;
        if(currentDevice != null) {
            try(DeviceActivationContext ctx = getNewContext()) {
                currentDevice.activate(this, currentDeviceState, world, pos, currentAim(), ctx);
            }
        }
    }

    protected void setupEnergyObserver(Supplier<Long> energyValueSupplier) {
        energyObserver = new Observer<>(energyValueSupplier,
                (a, b) -> !a.equals(b),
                value -> NetworkUtil.sendToAllTracking(
                        new PacketS2CUpdateEnergy(pos, value), (ServerWorld) world, pos)
        );
    }

    protected abstract Vector3d currentAim();

    protected abstract DeviceActivationContext getNewContext();

    protected boolean canActivate() {
        return true;
    }

    @Override
    public void setWorld(World world) {
        super.setWorld(world);

        if(world.isClient) {
            NetworkUtil.sendToServer(new PacketC2SRequestDCState(pos));
        }
    }

    public void sendStateTo(ServerPlayerEntity player) {
        energyObserver.forceMarkDirty();
        NetworkUtil.sendToPlayer(new PacketS2CUpdateRedstoneControl(pos, redstoneControlType), player);
    }

    //region Client Methods
    /**
     * Client Method
     */
    public float getEnergyRatio() {
        return (float) getEnergy() / (float) getMaxEnergy();
    }

    /**
     * Client Method
     */
    public final long getEnergy() { return clientEnergy; }

    /**
     * Client Method
     */
    public abstract long getMaxEnergy();

    /**
     * Client Method
     */
    public void setClientEnergy(long amount) { clientEnergy = amount; }
    //endregion

    @Override
    public void readNbt(NbtCompound nbt) {
        setRedstoneControlType(RedstoneControlType.fromIndex(nbt.getInt("redstoneControl")));

        if(currentDevice != null && nbt.contains("device", NbtElement.COMPOUND_TYPE)) {
            currentDevice.getNewState().deserialize(nbt.getCompound("device"));
        }
    }

    @Override
    protected void writeNbt(NbtCompound nbt) {
        nbt.put("device", currentDevice != null ? currentDevice.getNewState().serialize() : new NbtCompound());
        nbt.putInt("redstoneControl", redstoneControlType.order);
    }

    protected void setCurrentDevice(@Nullable IFramedDevice newDevice) {
        if(currentDevice != null) {
            currentDeviceState.invalidate();
        }
        currentDevice = newDevice;

        if(newDevice != null) {
            currentDeviceState = newDevice.getNewState();
        }else {
            currentDeviceState = null;
        }
    }
}
