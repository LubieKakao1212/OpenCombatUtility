package com.LubieKakao1212.opencu.block.entity;

import com.LubieKakao1212.opencu.capability.dispenser.DispenseResult;
import com.LubieKakao1212.opencu.capability.dispenser.DispenserCapability;
import com.LubieKakao1212.opencu.capability.dispenser.IDispenser;
import com.LubieKakao1212.opencu.config.OpenCUConfigCommon;
import com.LubieKakao1212.opencu.gui.container.OmnidispenserMenu;
import com.LubieKakao1212.opencu.init.CUBlockEntities;
import com.LubieKakao1212.opencu.network.NetworkHandler;
import com.LubieKakao1212.opencu.network.packet.dispenser.RequestDispenserUpdatePacket;
import com.LubieKakao1212.opencu.network.packet.dispenser.UpdateDispenserAimPacket;
import com.LubieKakao1212.opencu.network.packet.dispenser.UpdateDispenserPacket;
import com.LubieKakao1212.qulib.capability.energy.InternalEnergyStorage;
import com.LubieKakao1212.qulib.math.AimUtil;
import com.LubieKakao1212.qulib.math.MathUtil;
import com.LubieKakao1212.qulib.nbt.JomlNBT;
import com.LubieKakao1212.qulib.util.joml.QuaterniondUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Tuple;
import net.minecraft.world.Containers;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import org.joml.Quaterniond;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Random;
import java.util.concurrent.ConcurrentLinkedQueue;

public class BlockEntityOmniDispenser extends BlockEntity implements MenuProvider {

    public static final double aimIdenticalityEpsilon = MathUtil.degToRad * 0.1;

    private final ConcurrentLinkedQueue<DispenseAction> actionQueue = new ConcurrentLinkedQueue<>();

    private final ItemStackHandler inventory;
    private final LazyOptional<ItemStackHandler> inventoryCap;

    private final LazyOptional<InternalEnergyStorage> energyCap;
    /*private final InternalEnergyStorage energy;*/

    private DispenseAction currentAction;
    private Quaterniond targetAim;

    private IDispenser currentDispenser;

    private long initialiseDelay = 3;

    private boolean isInitialised = false;

    private ItemStack currentDispenserItem = null;

    private DispenseAction lastAction = null;

    public long clientAge;
    public long clientPrevFrameAge;
    public float clientPrevFramePartialTick;
    public double deltaAngle;


    public BlockEntityOmniDispenser(BlockPos pos, BlockState blockState) {
        super(CUBlockEntities.OMNI_DISPENSER, pos, blockState);
        currentAction = new DispenseAction(new Quaterniond());

        lastAction = new DispenseAction(new Quaterniond().identity());
        setCurrentAction(new Quaterniond());

        targetAim = new Quaterniond().identity();

        inventory = new ItemStackHandler(10) {
            @Override
            protected void onContentsChanged(int slot) {
                if(slot == 0) {
                    updateDispenser();
                }
                setChanged();
            }

            @Override
            public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
                if(slot == 0)
                {
                    return stack.getCapability(DispenserCapability.DISPENSER_CAPABILITY).isPresent();
                }
                return true;
            }

            @Override
            public int getSlotLimit(int slot) {
                if(slot == 0)
                {
                    return 1;
                }else {
                    return 64;
                }
            }
        };
        inventoryCap = LazyOptional.of(() -> inventory);

        energyCap = OpenCUConfigCommon.DISPENSER.energyConfig.createCapFromConfig();
    }

    private void updateDispenser() {
        ItemStack dispenserStack = inventory.getStackInSlot(0);
        currentDispenser = dispenserStack.getCapability(DispenserCapability.DISPENSER_CAPABILITY).resolve().orElseGet(() -> null);
        if(level != null && !level.isClientSide) {
            BlockPos pos = getBlockPos();
            NetworkHandler.sendToAllTracking(new UpdateDispenserPacket.FromServer(pos, dispenserStack), level, pos);
            //NetworkHandler.sendToServer(new UpdateDispenserPacket.FromClient(pos, dispenserStack));
        }else
        {
            //TODO Mark for update
        }
    }

    public static <T> void tick(Level level, BlockPos pos, BlockState state, T blockEntity) {
        BlockEntityOmniDispenser dispenser = (BlockEntityOmniDispenser)blockEntity;
        if (!level.isClientSide) {
            if(dispenser.initialiseDelay-- == 0) {
                dispenser.updateDispenser();
                NetworkHandler.sendToAllTracking(
                        new UpdateDispenserAimPacket(pos, dispenser.currentAction.aim, false),
                        level, pos);
            }
            if(dispenser.currentDispenser != null) {
                if(QuaterniondUtil.smallerAngle(dispenser.targetAim, dispenser.currentAction.aim) > aimIdenticalityEpsilon) {
                    dispenser.currentAction.aim = QuaterniondUtil.step(
                            dispenser.currentAction.aim,
                            dispenser.targetAim,
                            dispenser.currentDispenser.getAlignmentSpeed() * MathUtil.degToRad);
                    NetworkHandler.sendToAllTracking(
                            new UpdateDispenserAimPacket(pos, dispenser.currentAction.aim, false),
                            level, pos);
                } else {
                    dispenser.currentAction.aim = dispenser.targetAim;
                    if(!dispenser.currentAction.lockedOn)
                    {
                        NetworkHandler.sendToAllTracking(
                                new UpdateDispenserAimPacket(pos, dispenser.currentAction.aim, false),
                                level, pos);
                        dispenser.currentAction.lockedOn = true;
                    }
                }
            }
            while(!dispenser.actionQueue.isEmpty())
            {
                dispenser.actionQueue.poll();
                dispenser.shoot(dispenser.currentAction);
            }
        }else
        {
            if(!dispenser.isInitialised) {
                dispenser.isInitialised = true;
                dispenser.setCurrentAction(dispenser.targetAim);
                NetworkHandler.sendToServer(new RequestDispenserUpdatePacket(dispenser.getBlockPos()));
            }
            dispenser.clientAge++;
        }
    }

    public void dispense() {
        actionQueue.add(currentAction);
    }

    public void aim(double pitch, double yaw) {
        setAim(AimUtil.aimRad(MathUtil.loop(pitch, -MathUtil.piHalf, MathUtil.piHalf), MathUtil.loop(yaw, -MathUtil.pi, MathUtil.pi), Direction.EAST, Direction.UP));
    }

    public boolean isAligned() {
        return QuaterniondUtil.smallerAngle(targetAim, currentAction.aim) < aimIdenticalityEpsilon;
    }

    public double aimingStatus() {
        return QuaterniondUtil.smallerAngle(targetAim, currentAction.aim);
    }

    public String setForce(double force) {
        return currentDispenser.trySetForce(force);
    }

    public String setSpread(double spread) {
        return currentDispenser.trySetSpread(spread);
    }

    public double getMinSpread() {
        return currentDispenser.getMaxSpread();
    }

    public double getMaxSpread() {
        return currentDispenser.getMaxSpread();
    }

    public void setAim(Quaterniond aim) {
        targetAim = aim.normalize();
        currentAction.lockedOn = false;
    }

    private void shoot(DispenseAction action) {
        if(currentDispenser != null) {
            Tuple<ItemStack, Integer> ammo = findAmmo();
            if(ammo != null) {
                DispenseResult result = currentDispenser.shoot(this, level, ammo.getA(), worldPosition, action.aim);
                useAmmo(ammo.getB(), result.getLeftover());
            }
        }
    }

    private Tuple<ItemStack, Integer> findAmmo() {
        for(int i=1; i<10; i++) {
            ItemStack stack = inventory.extractItem(i, 1, true);
            if(!stack.isEmpty()) {
                return new Tuple<>(stack, i);
            }
        }
        return null;
    }

    private void useAmmo(int slot, ItemStack leftover) {
        inventory.extractItem(slot, 1, false);

        if(!leftover.isEmpty()) {
            slot = -1;

            for (int i = 9; i > 0; i--) {
                if (inventory.getStackInSlot(i).isEmpty()) {
                    if(slot == -1)
                    {
                        slot = i;
                    }
                }else
                {
                    if(inventory.insertItem(i, leftover, false).isEmpty()) {
                        slot = -1;
                        return;
                    }
                }
            }

            if(slot > 0) {
                inventory.insertItem(slot, leftover, false);
            }else
            {
                Containers.dropItemStack(level, worldPosition.getX(), worldPosition.getY(), worldPosition.getZ(), leftover);
            }
        }
    }

    public boolean isUsableBy(Player player) {
        return player.distanceToSqr(worldPosition.getX(), worldPosition.getY(), worldPosition.getZ()) <= 64D && level.getBlockEntity(worldPosition) == this;
    }

    @Override
    public void saveAdditional(@NotNull CompoundTag compound) {
        //Node
        CompoundTag nodeTag = new CompoundTag();

        //Inventory
        compound.put("inventory", inventory.serializeNBT());

        energyCap.ifPresent(energyStorage -> compound.put("energy", energyStorage.serializeNBT()));

        compound.put("aim", JomlNBT.writeQuaternion(currentAction.aim));

        compound.put("dispenser", currentDispenser != null ? currentDispenser.serializeNBT() : new CompoundTag());

        super.saveAdditional(compound);
    }

    @Override
    public void load(@NotNull CompoundTag compound) {
        super.load(compound);

        //Inventory
        CompoundTag inventoryTag = compound.getCompound("inventory");
        if(inventory != null) {
            inventory.deserializeNBT(inventoryTag);
        }

        Tag energyTag = compound.get("energy");
        if(energyTag != null) {
            energyCap.ifPresent((energy) -> energy.deserializeNBT(energyTag));
        }

        if(compound.contains("aim", Tag.TAG_LIST)) {
            setAim(JomlNBT.readQuaternion(compound.getList("aim", Tag.TAG_DOUBLE)));
        }

        if(currentDispenser != null && compound.contains("dispenser", Tag.TAG_COMPOUND)) {
            currentDispenser.deserializeNBT(compound.getCompound("dispenser"));
        }
    }

    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> capability, @Nullable Direction facing) {
        if(capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            return (LazyOptional<T>)inventoryCap;
        }
        if(capability == CapabilityEnergy.ENERGY) {
            return (LazyOptional<T>)energyCap;
        }
        return super.getCapability(capability, facing);
    }

    public void sendDispenserUpdateTo(ServerPlayer player) {
        NetworkHandler.sendTo(player, new UpdateDispenserPacket.FromServer(worldPosition, inventory.getStackInSlot(0)));
        NetworkHandler.sendTo(player, new UpdateDispenserAimPacket(worldPosition, currentAction.aim, true));
    }

    public ItemStack getCurrentDispenserItem() {
        return currentDispenserItem;
    }

    public DispenseAction getCurrentAction() {
        return currentAction;
    }

    public DispenseAction getLastAction() {
        return lastAction;
    }

    public void setLastAction(Quaterniond aim) {
        lastAction.aim = aim;
    }

    public void setCurrentAction(Quaterniond newAction) {
        //this.lastAction = currentAction;
        this.currentAction = new DispenseAction(newAction);
        this.deltaAngle = QuaterniondUtil.smallerAngle(lastAction.aim(), currentAction.aim());
    }

    public void setCurrentDispenserItem(ItemStack currentDispenserItem) {
        this.currentDispenserItem = currentDispenserItem;
    }

    @Override
    public Component getDisplayName() {
        //TODO Translation
        return new TextComponent("dispenser");
    }

    @Override
    public AbstractContainerMenu createMenu(int containerId, @NotNull Inventory inventory, @NotNull Player player) {
        return new OmnidispenserMenu(containerId, inventory, level, getBlockPos());
    }

    public static class DispenseAction {
        private Quaterniond aim;

        private boolean lockedOn;

        public DispenseAction(Quaterniond aim) {
            this.aim = aim;
        }

        public Quaterniond aim() {
            return aim;
        }
    }

}
