package com.LubieKakao1212.opencu.forge.block.entity;

import com.LubieKakao1212.opencu.OpenCUConfigCommon;
import com.LubieKakao1212.opencu.capability.IInternalEnergyStorage;
import com.LubieKakao1212.opencu.capability.InfiniteEnergyStorage;
import com.LubieKakao1212.opencu.capability.InternalEnergyStorage;
import com.LubieKakao1212.opencu.capability.OffsetItemHandler;
import com.LubieKakao1212.opencu.common.block.entity.BlockEntityModularFrame;
import com.LubieKakao1212.opencu.PlatformUtil;
import com.LubieKakao1212.opencu.common.transaction.DeviceActivationContext;
import com.LubieKakao1212.opencu.forge.util.ItemHandlerUtil;
import com.LubieKakao1212.opencu.forge.util.transaction.AmmoLeftoverContext;
import com.LubieKakao1212.opencu.forge.util.transaction.EnergyContext;
import com.LubieKakao1212.opencu.forge.util.transaction.ScopedContext;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtInt;
import net.minecraft.screen.slot.Slot;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.EmptyEnergyStorage;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.SlotItemHandler;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class BlockEntityModularFrameImpl extends BlockEntityModularFrame {

    public static final int slotCount = 10;
    public static final int ammoSlotCount = 9;
    public static final int ammoSlotsStart = 1;
    public static final int ammoSlotsEnd = 10;

    private static final int deviceSlotIdx = 0;

    private final LazyOptional<IItemHandler> inventoryCapability;
    private final LazyOptional<IEnergyStorage> energyCapabilty;
    private final IItemHandlerModifiable ammo;
    private final ItemStackHandler inventory;
    private final IInternalEnergyStorage energy;

    public BlockEntityModularFrameImpl(BlockPos pos, BlockState blockState) {
        super(pos, blockState);
        this.inventory = new ItemStackHandler(slotCount) {
            @Override
            protected void onContentsChanged(int slot) {
                BlockEntityModularFrameImpl.this.markDirty();
                if(slot == deviceSlotIdx) {
                    updateDispenser();
                }
            }

            @Override
            public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
                if(slot == deviceSlotIdx)
                {
                    return PlatformUtil.getDeviceFrom(stack) != null;
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
        this.inventoryCapability = LazyOptional.of(() -> this.inventory);

            ammo = new OffsetItemHandler(inventory, ammoSlotsStart);

        //region energy
        var useEnergy = OpenCUConfigCommon.general().energyEnabled();
        if(useEnergy) {
            var capacity = OpenCUConfigCommon.modularFrame().energy().energyCapacity();
            energy = new InternalEnergyStorage(capacity, capacity, 0);
            energyCapabilty = LazyOptional.of(() -> energy);
        } else {
            energy = InfiniteEnergyStorage.DUAL;
            energyCapabilty = LazyOptional.of(() -> EmptyEnergyStorage.INSTANCE);
        }
        setupEnergyObserver(() -> (long)energy.getEnergyStored());
        //endregion
    }

    @Override
    protected DeviceActivationContext getNewContext() {
        var scopeCtx = new ScopedContext();
        var energyCtx = new EnergyContext(scopeCtx, energy);
        var ammoLeftoverCtx = new AmmoLeftoverContext(scopeCtx, ammo, world, pos);
        return new DeviceActivationContext(
                scopeCtx,
                energyCtx,
                ammoLeftoverCtx,
                ammoLeftoverCtx
        );
    }

    @Override
    public void scatterInventory() {
        assert world != null;
        ItemHandlerUtil.scatterAndEmpty(inventory, world, pos.getX(), pos.getY(), pos.getZ());
    }

    /**
     * Creates a slot for gui
     *
     * @param idx slot index 0 => device; 1-9 => ammo
     */
    @Override
    public Slot createSlot(int idx, int x, int y) {
        return new SlotItemHandler(inventory, idx, x, y);
    }

    @Override
    public long getMaxEnergy() {
        return energy.getMaxEnergyStored();
    }

    public boolean isUsableBy(PlayerEntity player) {
        assert world != null;
        return player.squaredDistanceTo(pos.getX(), pos.getY(), pos.getZ()) <= 64D && world.getBlockEntity(pos) == this;
    }

    @Override
    public void writeNbt(@NotNull NbtCompound compound) {
        super.writeNbt(compound);

        compound.put("inventory", inventory.serializeNBT());
        if(energy instanceof InternalEnergyStorage ies) {
            compound.put("energy", ies.serializeNBT());
        }
        else {
            assert energy instanceof InfiniteEnergyStorage;
        }
    }

    @Override
    public void readNbt(@NotNull NbtCompound compound) {
        super.readNbt(compound);

        var inventoryNbt = compound.getCompound("inventory");
        if(inventoryNbt != null) {
            inventory.deserializeNBT(inventoryNbt);
        }

        var energyNbt = compound.get("energy");
        if(energyNbt instanceof NbtInt && energy instanceof InternalEnergyStorage ies) {
            ies.deserializeNBT(energyNbt);
        }
        else {
            assert energy instanceof InfiniteEnergyStorage;
        }

    }

    @Override
    protected @NotNull ItemStack getCurrentDeviceItemServer() {
        return inventory.getStackInSlot(deviceSlotIdx);
    }

    @Override
    protected int getCurrentEnergy() {
        return energy.getEnergyStored();
    }

    @Override
    public <T> @NotNull LazyOptional<T> getCapability(@NotNull Capability<T> capability, @Nullable Direction facing) {
        if(capability == ForgeCapabilities.ITEM_HANDLER) {
            return (LazyOptional<T>)inventoryCapability;
        }
        if(capability == ForgeCapabilities.ENERGY) {
            return (LazyOptional<T>)energyCapabilty;
        }
        return super.getCapability(capability, facing);
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        inventoryCapability.invalidate();
        energyCapabilty.invalidate();
    }
}
