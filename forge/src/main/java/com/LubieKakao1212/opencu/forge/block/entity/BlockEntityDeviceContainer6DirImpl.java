package com.LubieKakao1212.opencu.forge.block.entity;

import com.LubieKakao1212.opencu.OpenCUConfigCommon;
import com.LubieKakao1212.opencu.capability.InternalEnergyStorage;
import com.LubieKakao1212.opencu.common.block.entity.BlockEntityDeviceContainer6Dir;
import com.LubieKakao1212.opencu.common.device.IDeviceContainer;
import com.LubieKakao1212.opencu.common.device.IFramedDevice;
import com.LubieKakao1212.opencu.common.screen.slot.ConstSlot;
import com.LubieKakao1212.opencu.common.transaction.DeviceActivationContext;
import com.LubieKakao1212.opencu.forge.util.ItemHandlerUtil;
import com.LubieKakao1212.opencu.forge.util.transaction.AmmoLeftoverContext;
import com.LubieKakao1212.opencu.forge.util.transaction.EnergyContext;
import com.LubieKakao1212.opencu.forge.util.transaction.ScopedContext;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.Slot;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.EmptyEnergyStorage;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.SlotItemHandler;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.function.Supplier;

public class BlockEntityDeviceContainer6DirImpl extends BlockEntityDeviceContainer6Dir {

    public static final int slotCount = 9;
    public static final int ammoSlotCount = 9;
    public static final int ammoSlotsStart = 0;
    public static final int ammoSlotsEnd = 9;

    private final LazyOptional<IItemHandler> inventoryCapability;
    private final LazyOptional<IEnergyStorage> energyCapabilty;
    private final ItemStackHandler ammoInventory;
    private final InternalEnergyStorage energy;

    public static BlockEntityType.BlockEntityFactory<BlockEntityDeviceContainer6Dir> factory(Supplier<BlockEntityType<BlockEntityDeviceContainer6Dir>> type, Supplier<ItemStack> model, Supplier<IFramedDevice> device) {
        return (pos, blockState) -> new BlockEntityDeviceContainer6DirImpl(type.get(), model.get(), pos, blockState, device.get());
    }

    public BlockEntityDeviceContainer6DirImpl(BlockEntityType<BlockEntityDeviceContainer6Dir> type, @NotNull ItemStack model, BlockPos pos, BlockState blockState, IFramedDevice device) {
        super(type, model, pos, blockState);

        this.setCurrentDevice(device);

        this.ammoInventory = new ItemStackHandler(slotCount) {
            @Override
            protected void onContentsChanged(int slot) {
                BlockEntityDeviceContainer6DirImpl.this.markDirty();
            }

            @Override
            public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
                return true;
            }
        };
        this.inventoryCapability = LazyOptional.of(() -> this.ammoInventory);

        //region energy
        var useEnergy = device.energyEnabled();
        if(useEnergy) {
            var capacity = OpenCUConfigCommon.repulsorDevice().energy().energyCapacity();
            energy = new InternalEnergyStorage(capacity, capacity, capacity);
            energyCapabilty = LazyOptional.of(() -> energy);
        } else {
            energy = new InternalEnergyStorage(0,0,0,0);
            energyCapabilty = LazyOptional.of(() -> EmptyEnergyStorage.INSTANCE);
        }
        setupEnergyObserver(() -> (long)energy.getEnergyStored());
        //endregion
    }

    @Override
    protected DeviceActivationContext getNewContext() {
        var scopeCtx = new ScopedContext();
        var energyCtx = new EnergyContext(scopeCtx, energy);
        var ammoLeftoverCtx = new AmmoLeftoverContext(scopeCtx, ammoInventory, world, pos);
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
        ItemHandlerUtil.scatterAndEmpty(ammoInventory, world, pos.getX(), pos.getY(), pos.getZ());
    }

    @Override
    public Slot createSlot(int idx, int x, int y) {
        if(idx == 0) {
            return new ConstSlot(getDeviceItem(), x, y);
        }
        return new SlotItemHandler(ammoInventory, idx - 1, x, y);
    }

    @Override
    public long getMaxEnergy() {
        return 0;
    }

    @Override
    public boolean isSameAs(IDeviceContainer deviceContainer) {
        return deviceContainer instanceof BlockEntityDeviceContainer6Dir be && be.getType().equals(getType()) && be.getPos().equals(getPos());
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
