package com.LubieKakao1212.opencu.fabric.block.entity;

import com.LubieKakao1212.opencu.OpenCUConfigCommon;
import com.LubieKakao1212.opencu.common.block.entity.BlockEntityDeviceContainer6Dir;
import com.LubieKakao1212.opencu.common.device.IDeviceContainer;
import com.LubieKakao1212.opencu.common.device.IFramedDevice;
import com.LubieKakao1212.opencu.common.screen.slot.ConstSlot;
import com.LubieKakao1212.opencu.common.transaction.DeviceActivationContext;
import com.LubieKakao1212.opencu.fabric.inventory.SlottedInventory;
import com.LubieKakao1212.opencu.fabric.transaction.AmmoContext;
import com.LubieKakao1212.opencu.fabric.transaction.RebornEnergyContext;
import com.LubieKakao1212.opencu.fabric.transaction.ScopedContext;
import com.LubieKakao1212.opencu.fabric.transaction.SimpleLeftoverContext;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.fabricmc.fabric.api.transfer.v1.item.InventoryStorage;
import net.fabricmc.fabric.api.transfer.v1.item.ItemVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.Storage;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.Slot;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.NotNull;
import team.reborn.energy.api.EnergyStorage;
import team.reborn.energy.api.base.InfiniteEnergyStorage;
import team.reborn.energy.api.base.LimitingEnergyStorage;
import team.reborn.energy.api.base.SimpleEnergyStorage;

import javax.annotation.Nullable;
import java.util.function.Supplier;

@SuppressWarnings("UnstableApiUsage")
public class BlockEntityDeviceContainer6DirImpl extends BlockEntityDeviceContainer6Dir {

    private final EnergyStorage energyStorage;
    public final EnergyStorage exposedEnergyStorage;
    public final Storage<ItemVariant> exposedAmmoStorage;
    private final SlottedInventory ammoInventory;

    public static FabricBlockEntityTypeBuilder.Factory<BlockEntityDeviceContainer6Dir> factory(Supplier<BlockEntityType<BlockEntityDeviceContainer6Dir>> type, Supplier<ItemStack> model, Supplier<IFramedDevice> device) {
        return (pos, blockState) -> new BlockEntityDeviceContainer6DirImpl(type.get(), model.get(), pos, blockState, device.get());
    }

    public BlockEntityDeviceContainer6DirImpl(BlockEntityType<BlockEntityDeviceContainer6Dir> type, @NotNull ItemStack model, BlockPos pos, BlockState blockState, IFramedDevice device) {
        super(type, model, pos, blockState);

        this.setCurrentDevice(device);

        //region energy
//        var useEnergy = OpenCUConfigCommon.repulsorDevice().energy().isEnergyEnabled();

        if (device.energyEnabled()) {
            var capacity = OpenCUConfigCommon.repulsorDevice().energy().energyCapacity();
            energyStorage = new SimpleEnergyStorage(capacity, capacity, capacity);
            exposedEnergyStorage = new LimitingEnergyStorage(energyStorage, capacity, 0);
        } else {
            energyStorage = new InfiniteEnergyStorage();
            exposedEnergyStorage = null;
        }
        //endregion

        //region ammo
        if(device.ammoEnabled()) {
            ammoInventory = new SlottedInventory(BlockEntityModularFrameImpl.ammoSlotCount) {
                @Override
                public void markDirty() {
                    BlockEntityDeviceContainer6DirImpl.this.markDirty();
                }
            };
            exposedAmmoStorage = InventoryStorage.of(ammoInventory, null);
        }
        else {
            exposedAmmoStorage = Storage.empty();
            ammoInventory = new SlottedInventory(0) {
                @Override
                public void markDirty() {
                }
            };
        }
        setupEnergyObserver(energyStorage::getAmount);
        //endregion
    }

    @Override
    protected DeviceActivationContext getNewContext() {
        return new DeviceActivationContext(
                new ScopedContext(),
                new RebornEnergyContext(energyStorage),
                new AmmoContext(exposedAmmoStorage),
                new SimpleLeftoverContext(exposedAmmoStorage, world, pos)
        );
    }

    public void scatterInventory() {
        ItemScatterer.spawn(world, pos, ammoInventory);
    }

    /**
     * Creates a slot for gui
     *
     * @param idx slot index 0 => device; 1-9 => ammo
     * @param x
     * @param y
     */
    @Override
    public Slot createSlot(int idx, int x, int y) {
        if(idx == 0) {
            return new ConstSlot(getDeviceItem(), x, y);
        }
        return new Slot(ammoInventory, idx - 1, x, y);
    }

    @Override
    public boolean isSameAs(IDeviceContainer deviceContainer) {
        return deviceContainer instanceof BlockEntityDeviceContainer6Dir be && be.getType().equals(getType()) && be.getPos().equals(getPos());
    }

    /**
     * Client Method
     */
    @Override
    public long getMaxEnergy() {
        return energyStorage.getCapacity();
    }
}