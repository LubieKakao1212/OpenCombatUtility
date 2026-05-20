package com.LubieKakao1212.opencu.common.block.entity;

import com.LubieKakao1212.opencu.NetworkUtil;
import com.LubieKakao1212.opencu.common.device.IDeviceContainer;
import com.LubieKakao1212.opencu.common.device.IFramedDevice;
import com.LubieKakao1212.opencu.common.device.state.IDeviceState;
import com.LubieKakao1212.opencu.common.network.packet.devicecontainer.PacketC2SRequestDCState;
import com.LubieKakao1212.opencu.common.network.packet.devicecontainer.PacketS2CUpdateEnergy;
import com.LubieKakao1212.opencu.common.network.packet.generic.PacketS2CUpdateRedstoneControl;
import com.LubieKakao1212.opencu.common.screen.handler.DeviceContainerScreenHandler;
import com.LubieKakao1212.opencu.common.transaction.DeviceActivationContext;
import com.LubieKakao1212.opencu.common.util.Observer;
import com.LubieKakao1212.opencu.common.util.RedstoneControlType;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.PropertyDelegate;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.screen.slot.Slot;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3d;

import java.util.EnumSet;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;

public abstract class BlockEntityDeviceContainer extends BlockEntity implements NamedScreenHandlerFactory, IDeviceContainer {

    public static final int screenPropertyCount = 3;
    public static final int xPropertyIndex = 0;
    public static final int yPropertyIndex = 1;
    public static final int zPropertyIndex = 2;

    public static final int autoShootInterval = 10;

    private final AtomicInteger actionsToPerform = new AtomicInteger(0);

    private RedstoneControlType redstoneControlType;
    private long redstoneActivationTimer = 0;
    //TODO Write to nbt
    private boolean rsState;

    private @Nullable IFramedDevice currentDevice;
    private IDeviceState currentDeviceState;

    protected Observer<Long> energyObserver;

    private final PropertyDelegate screenProperties;

    //region Client Fields
    private long clientEnergy;
    //endregion

    public BlockEntityDeviceContainer(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
        redstoneControlType = RedstoneControlType.PULSE;

        screenProperties = new PropertyDelegate() {
            @Override
            public int get(int index) {
                return switch (index) {
                    case xPropertyIndex -> pos.getX();
                    case yPropertyIndex -> pos.getY();
                    case zPropertyIndex -> pos.getZ();
                    default -> -1;
                };
            }

            @Override
            public void set(int index, int value) {

            }

            @Override
            public int size() {
                return screenPropertyCount;
            }
        };
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
            var power= getRsState();
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

    public void pulseActivate(boolean newState) {
        if(newState != rsState) {
            rsState = newState;
            if(newState && getRedstoneControlType() == RedstoneControlType.PULSE) {
                scheduleActivation();
            }
        }
    }

    public boolean getRsState() {
        return rsState;
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
    public @Nullable IFramedDevice getDevice() {
        return currentDevice != null ? currentDevice : null; // TODO add identity device
    }

    @Override
    public @Nullable IDeviceState getState() {
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

    public abstract void scatterInventory();


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
        if(getDevice() != null) {
            assert getState() != null;
            getState().forceSync(NetworkUtil.toPlayerSender(player), pos);
        }
    }

    /**
     * Creates a slot for gui
     * @param idx slot index 0 => device; 1-9 => ammo
     */
    public abstract Slot createSlot(int idx, int x, int y);

    @Override
    public ScreenHandler createMenu(int containerId, @NotNull PlayerInventory inventory, @NotNull PlayerEntity player) {
        assert world != null;
        return new DeviceContainerScreenHandler(getScreenHandlerType(), containerId, inventory, this::createSlot, screenProperties);
    }

    public abstract ScreenHandlerType<?> getScreenHandlerType();

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
            assert getState() != null;
            getState().deserialize(nbt.getCompound("device"));
        }
    }

    @Override
    protected void writeNbt(NbtCompound nbt) {
        nbt.put("device", currentDevice != null ? Objects.requireNonNull(getState()).serialize() : new NbtCompound());
        nbt.putInt("redstoneControl", redstoneControlType.order);
    }

    protected void setCurrentDevice(@Nullable IFramedDevice newDevice) {
        if(currentDevice != null) {
            currentDeviceState.invalidate();
        }
        currentDevice = newDevice;

        if(newDevice != null) {
            currentDeviceState = newDevice.getNewState(this::markDirty);
        }else {
            currentDeviceState = null;
        }
    }
}
