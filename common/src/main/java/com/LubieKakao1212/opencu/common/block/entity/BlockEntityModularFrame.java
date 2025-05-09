package com.LubieKakao1212.opencu.common.block.entity;

import com.LubieKakao1212.opencu.NetworkUtil;
import com.LubieKakao1212.opencu.PlatformUtil;
import com.LubieKakao1212.opencu.common.device.IDeviceContainer;
import com.LubieKakao1212.opencu.common.device.event.DistributingWorldEventNode;
import com.LubieKakao1212.opencu.common.device.event.IEventNode;
import com.LubieKakao1212.opencu.common.device.event.data.ActivateEvent;
import com.LubieKakao1212.opencu.common.device.event.data.IEventData;
import com.LubieKakao1212.opencu.common.device.event.data.LookAtEvent;
import com.LubieKakao1212.opencu.common.device.event.data.SetAimEvent;
import com.LubieKakao1212.opencu.common.network.packet.devicecontainer.frame.PacketC2SRequestDeviceUpdate;
import com.LubieKakao1212.opencu.common.network.packet.devicecontainer.frame.PacketS2CUpdateDevice;
import com.LubieKakao1212.opencu.common.network.packet.devicecontainer.frame.PacketS2CUpdateFrameAim;
import com.LubieKakao1212.opencu.common.network.packet.devicecontainer.frame.PacketS2CUpdateRequiresLock;
import com.LubieKakao1212.opencu.common.util.RedstoneControlType;
import com.LubieKakao1212.opencu.registry.CUBlockEntities;
import com.LubieKakao1212.opencu.registry.CUMenu;
import com.lubiekakao1212.qulib.math.Aim;
import com.lubiekakao1212.qulib.math.Constants;
import com.lubiekakao1212.qulib.math.MathUtilKt;
import com.lubiekakao1212.qulib.math.extensions.Vector3dExtensions;
import com.lubiekakao1212.qulib.math.mc.Vector3m;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector2d;
import org.joml.Vector3d;

public abstract class BlockEntityModularFrame extends BlockEntityDeviceContainer implements IRedstoneControlled, IEventNode {

//    public static final int requiresLockPropertyIndex = 3;
//    public static final int redstoneControlPropertyIndex = 4;
//    public static final int energyPropertyIndex = 5;
//    public static final int maxEnergyPropertyIndex = 6;
    public static final double aimIdenticalityEpsilon = Constants.degToRad * 0.1;

    private static final long lateInitServerDelay = 3;

    private boolean requiresLock;

    //region Events
    private final DistributingWorldEventNode eventDistributor;
    //endregion

    //region Aim
    private Aim currentAim;
    private Aim targetAim;
    private boolean lockedOn;
    //Client
    private Aim lastAim;
    //endregion

    //region Device
    //Client
    private ItemStack currentDeviceItem = null;
    //endregion

    private long age = 0;

    //region Renderer
    //Client
    public long clientAge;
    //Client
    public long clientPrevFrameAge;
    //Client
    public float clientPrevFramePartialTick;

    //Client
    public double deltaAnglePitch;
    //Client
    public double deltaAngleYaw;
    //endregion



    public BlockEntityModularFrame(BlockPos pos, BlockState blockState) {
        super(CUBlockEntities.modularFrame(), pos, blockState);
        lastAim = new Aim(0, 0);
        setCurrentAim(new Aim(0, 0));

        targetAim = new Aim(0 ,0);
        requiresLock = false;

        eventDistributor = new DistributingWorldEventNode(pos);
    }

    protected void updateDispenser() {
        ItemStack deviceStack = getDeviceItem();

        setCurrentDevice(PlatformUtil.getDeviceFrom(deviceStack));

        if(world != null && !world.isClient) {
            BlockPos pos = getPos();
            NetworkUtil.sendToAllTracking(new PacketS2CUpdateDevice(pos, deviceStack), (ServerWorld) world, pos);
        } else {
            //TODO Mark for update
        }
    }

    private void sendDispenserAimUpdate() {
        NetworkUtil.sendToAllTracking(
                PacketS2CUpdateFrameAim.create(pos, currentAim, false),
                (ServerWorld) world, pos);
    }

    public static <T> void tick(World world, BlockPos pos, BlockState state, T blockEntity) {
        BlockEntityModularFrame be = (BlockEntityModularFrame)blockEntity;
        if (!world.isClient) {
            be.doInit(be::initServer);
            be.doAgedInit(be::lateInitServer, lateInitServerDelay);

            be.eventDistributor.validateRecipients();

            var device = be.getDevice();
            if(device != null) {
                var currentAim = be.currentAim;
                var targetAim = be.targetAim;
                if(!currentAim.equals(targetAim, aimIdenticalityEpsilon)) {
                    currentAim.stepPerAxis(targetAim,
                            device.getPitchAlignmentSpeed() * Constants.degToRad,
                            device.getYawAlignmentSpeed() * Constants.degToRad,
                            be.currentAim);

                    be.sendDispenserAimUpdate();
                    be.markDirty();
                } else {
                    be.setTargetAim(new Aim(targetAim.getPitch(), targetAim.getYaw()));
                    if(!be.lockedOn)
                    {
                        be.markDirty();
                        be.sendDispenserAimUpdate();
                        be.lockedOn = true;
                    }
                }
            }

            be.energyObserver.update();
            be.tickDeviceServer();
        }else
        {
            be.doInit(be::initClient);
            be.clientAge++;
        }
    }

    //region init
    public void doInit(Runnable init) {
        doAgedInit(init, 0);
    }

    public void doAgedInit(Runnable lateInit, long targetAge) {
        if(age++ == targetAge) {
            lateInit.run();
        }
    }

    public void initServer() {

    }

    public void lateInitServer() {
        updateDispenser();
        sendDispenserAimUpdate();
    }

    public void initClient() {
        setCurrentAim(targetAim);
        requestDispenserUpdate();
    }

    @Override
    public void setWorld(World world) {
        super.setWorld(world);
        eventDistributor.setWorld(world);
    }

    //endregion

    @Override
    public void handleEvent(IEventData data) {
        if(data instanceof SetAimEvent event) {
            setTargetAim(event.aim);
        }
        else if(data instanceof LookAtEvent event) {
            if(event.isWorldSpace) {
                aimAtWorld(event.target);
            }
            else
            {
                aimAt(event.target);
            }
        }
        else if(data instanceof ActivateEvent event) {
            scheduleActivation();
        }

        var device = getDevice();
        if(device != null)
        {
            device.handleEvent(this, getState(), data);
        }
    }

    public DistributingWorldEventNode getEventDistributor() {
        return eventDistributor;
    }

    public void aim(double pitch, double yaw) {
        setTargetAim(new Aim(pitch, yaw));
    }

    public void aimAt(Vector3d relativePos) {
        relativePos.negate();
        var yaw = Math.atan2(relativePos.x, -relativePos.z);
        var pitch = Math.atan2(-relativePos.y, new Vector2d(relativePos.x, relativePos.z).length());

        setTargetAim(new Aim(pitch, yaw));
    }

    public void aimAtWorld(Vector3d worldPos) {
        aimAt(new Vector3d(worldPos).sub(new Vector3m(pos.toCenterPos())));
    }

    public boolean isAligned() {
        return targetAim.equals(currentAim, aimIdenticalityEpsilon);
    }

    public void setTargetAim(Aim aim) {
        targetAim = aim;
        markDirty();
    }

    @Override
    protected Vector3d currentAim() {
        return currentAim.toQuaternion(Direction.EAST, Direction.UP).transform(Vector3dExtensions.INSTANCE.getSOUTH());
    }

    public boolean isUsableBy(PlayerEntity player) {
        assert world != null;
        return player.squaredDistanceTo(pos.getX(), pos.getY(), pos.getZ()) <= 64D && world.getBlockEntity(pos) == this;
    }

    @Override
    protected boolean canActivate() {
        return !(requiresLock && !isAligned());
    }

    public boolean isRequiresLock() {
        return requiresLock;
    }

    public void setRequiresLock(boolean requiresLock) {
        this.requiresLock = requiresLock;
        if(world instanceof ServerWorld sWorld) {
            NetworkUtil.sendToAllTracking(new PacketS2CUpdateRequiresLock(pos, requiresLock), sWorld, pos);
        }
    }

    //region redstone
    public RedstoneControlType getRedstoneControlType() {
        return isEmittingRedstone() ? RedstoneControlType.DISABLED : getRedstoneControlTypeRaw();
    }

    public RedstoneControlType getRedstoneControlTypeRaw() {
        return super.getRedstoneControlType();
    }

    public boolean isEmittingRedstone() {
        assert world != null;
        return world.getBlockState(pos).get(BlockProperties.EMITS_REDSTONE_SIGNAL);
    }

    public void setEmittingRedstone(boolean value) {
        assert world != null;
        var state = world.getBlockState(pos);
        state = state.with(BlockProperties.EMITS_REDSTONE_SIGNAL, value);
        world.setBlockState(pos, state);
    }
    //endregion

    @Override
    public void writeNbt(@NotNull NbtCompound compound) {
        compound.putDouble("pitch", currentAim.getPitch());
        compound.putDouble("yaw", currentAim.getYaw());
        compound.putDouble("targetPitch", targetAim.getPitch());
        compound.putDouble("targetYaw", targetAim.getYaw());
        compound.putBoolean("requiresLock", requiresLock);

        compound.put("distributor", eventDistributor.serialize());

        super.writeNbt(compound);
    }

    @Override
    public void readNbt(@NotNull NbtCompound compound) {
        super.readNbt(compound);

        var pitch = compound.getDouble("pitch");
        var yaw = compound.getDouble("yaw");
        var targetPitch = compound.getDouble("targetPitch");
        var targetYaw = compound.getDouble("targetYaw");

        setTargetAim(new Aim(targetPitch, targetYaw));
        currentAim = new Aim(pitch, yaw);

        requiresLock = compound.getBoolean("requiresLock");

        eventDistributor.deserialize(compound.getList("distributor", NbtElement.COMPOUND_TYPE));
    }

    public void sendDispenserUpdateTo(ServerPlayerEntity player) {
        NetworkUtil.sendToPlayer(new PacketS2CUpdateDevice(pos, getDeviceItem()), player);
        NetworkUtil.sendToPlayer(PacketS2CUpdateFrameAim.create(pos, currentAim, true), player);
    }

    @Override
    public void sendStateTo(ServerPlayerEntity player) {
        super.sendStateTo(player);
        NetworkUtil.sendToPlayer(new PacketS2CUpdateRequiresLock(pos, isRequiresLock()), player);
        sendDispenserUpdateTo(player);
    }

    @Override
    public Text getDisplayName() {
        return Text.translatable("block.opencu.modular_frame");
    }

    @Override
    public ItemStack getDeviceItem() {
        assert this.world != null;
        if(this.world.isClient) {
            return currentDeviceItem;
        }
        return getCurrentDeviceItemServer();
    }

    protected abstract ItemStack getCurrentDeviceItemServer();

    protected abstract int getCurrentEnergy();

    @Override
    public boolean isSameAs(IDeviceContainer deviceContainer) {
        return deviceContainer instanceof BlockEntityModularFrame frame && this.getPos().equals(frame.pos);
    }

    @Override
    public ScreenHandlerType<?> getScreenHandlerType() {
        return CUMenu.modularFrame();
    }

    //region Clinet Methods

    /**
     * Client method
     */
    public Aim getCurrentAim() {
        return currentAim;
    }

    /**
     * Client method
     */
    public Aim getLastAim() {
        return lastAim;
    }

    /**
     * Client method
     */
    public void setLastAim(Aim aim) {
        lastAim = aim;
    }

    /**
     * Client method
     */
    public void setCurrentAim(Aim newAim) {
        //this.lastAction = currentAction;
        this.currentAim = newAim;

        this.deltaAnglePitch = Math.abs(newAim.getPitch() - lastAim.getPitch());
        this.deltaAngleYaw = MathUtilKt.angleDistance(lastAim.getYaw(), newAim.getYaw());
        //QuaterniondExtensionsKt.smallAngle(lastAction.aim(), currentAction.aim());
    }

    /**
     * Client method
     */
    public void setCurrentDeviceItem(ItemStack currentDeviceItem) {
        this.currentDeviceItem = currentDeviceItem;
        setCurrentDevice(PlatformUtil.getDeviceFrom(currentDeviceItem));
    }

    /**
     * Client method
     */
    public void requestDispenserUpdate() {
        NetworkUtil.sendToServer(new PacketC2SRequestDeviceUpdate(pos));
    }
    //endregion
}
