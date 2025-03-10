package com.LubieKakao1212.opencu.common.gui.container;

import com.LubieKakao1212.opencu.PlatformUtil;
import com.LubieKakao1212.opencu.common.OpenCUModCommon;
import com.LubieKakao1212.opencu.common.block.entity.BlockEntityModularFrame;
import com.LubieKakao1212.opencu.registry.CUBlocks;
import com.LubieKakao1212.opencu.registry.CUMenu;
import net.minecraft.block.Block;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ArrayPropertyDelegate;
import net.minecraft.screen.PropertyDelegate;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.screen.slot.Slot;
import net.minecraft.util.math.BlockPos;

public abstract class DeviceContainerScreenHandler extends ScreenHandler {

    protected static final int deviceSlot = 0;
    protected static final int ammoSlotCount = 9;

    protected static final int blockSlotsStart = 0;
    protected static final int blockSlotCount = 10;
    protected static final int dispenserSlotsEnd = blockSlotsStart + blockSlotCount;

    //private static final int playerSlotsStart = dispenserSlotsEnd;
    protected static final int playerSlotCount = 36;
    //private static final int playerSlotsEnd = playerSlotsStart + playerSlotCount;

    protected static final int slotSize = 18;
    protected final int slotCount;

    private final ScreenHandlerContext context;
    private final PropertyDelegate properties;

    private final int playerSlotsStart;
    private final int ammoSlotStart;
    private final boolean hasDevice;

    private final Block validBlock;

    /*public DeviceContainerScreenHandler(int id, PlayerInventory playerInventory) {
        this(id, playerInventory, SlotProvider.dummy(blockSlotCount), ScreenHandlerContext.EMPTY, new ArrayPropertyDelegate(BlockEntityModularFrame.screenPropertyCount));
    }*/

    public DeviceContainerScreenHandler(Block validBlock, int id, PlayerInventory playerInventory, SlotProvider deviceContainerSlots, ScreenHandlerContext context, PropertyDelegate properties, boolean useDeviceSlot) {
        super(CUMenu.modularFrame(), id);

        this.validBlock = validBlock;

        var slotsCount = 0;

        if(useDeviceSlot) {
            this.addSlot(deviceContainerSlots.createSlot( 0, 43, 33));
            slotsCount += 1;
        }
        hasDevice = useDeviceSlot;

        this.context = context;

        int startX = 80;
        int startY = 15;
        final int[] index = {1};

        AddSlotBlock(startX, startY, 3, 3, slotSize, (int x, int y) -> deviceContainerSlots.createSlot(index[0]++, x, y));
        ammoSlotStart = slotsCount;
        slotsCount += 9;

        playerSlotsStart = slotsCount;

        index[0] = 0;

        SlotFactory playerSlotFactory = (int x, int y) -> new Slot(playerInventory, index[0]++, x, y);

        //Hotbar
        AddSlotBlock(8, 142, 9, 1, slotSize, playerSlotFactory);

        //Main player Inventory
        AddSlotBlock(8, 84, 9, 3, slotSize, playerSlotFactory);

        this.slotCount = this.slots.size();

        this.properties = properties;
        this.addProperties(properties);
    }

    public void AddSlotBlock(int startX, int startY, int blockWidth, int blockHeight, int slotSize, SlotFactory slotFactory) {
            for(int y = 0; y < blockHeight; y++)
                for(int x = 0; x < blockWidth; x++) {
                    this.addSlot(slotFactory.get(startX + x * slotSize,  startY + y * slotSize));
                }
    }

    @Override
    public ItemStack quickMove(PlayerEntity player, int index) {
        Slot slot = this.slots.get(index);

        ItemStack stack = ItemStack.EMPTY;

        if(slot.hasStack()) {
            ItemStack stackCpy = slot.getStack();
            if (!stackCpy.isEmpty()) {
                stack = stackCpy.copy();
                //Is in dispenser inventory
                if (slot.id < dispenserSlotsEnd) {
                    if (!this.insertItem(stackCpy, playerSlotsStart, slotCount, false)) {
                        return ItemStack.EMPTY;
                    }
                }
                else //Is in player inventory
                {
                    var flag = false;
                    if(hasDevice && PlatformUtil.getDeviceFrom(stackCpy) != null) {
                        var deviceSlotSlot = slots.get(deviceSlot);
                        var currentDevice = deviceSlotSlot.getStack();
                        if(currentDevice.isEmpty()) {
                            deviceSlotSlot.setStack(stackCpy.split(1));
                            flag = true;
                        }
                    }
                    if(!this.insertItem(stackCpy, ammoSlotStart, ammoSlotStart + ammoSlotCount, false) && !flag) {
                        return ItemStack.EMPTY;
                    }
                }

                if (stackCpy.isEmpty())
                {
                    slot.setStack(ItemStack.EMPTY);
                }
                else
                {
                    slot.markDirty();
                }

                if (stackCpy.getCount() == stack.getCount())
                {
                    OpenCUModCommon.LOGGER.warn("Impossible situation detected (Probably)");
                    return ItemStack.EMPTY;
                }

                slot.onTakeItem(player, stackCpy);
            }
        }
        return stack;
    }

    @Override
    public boolean canUse(PlayerEntity pPlayer) {
        return ScreenHandler.canUse(this.context, pPlayer, validBlock);
    }

    public boolean isRequiresLock() {
        return properties.get(BlockEntityModularFrame.requiresLockPropertyIndex) > 0;
    }

    public int getRedstoneControlTypeIndex() {
        return properties.get(BlockEntityModularFrame.redstoneControlPropertyIndex);
    }

    public int getEnergy() {
        return properties.get(BlockEntityModularFrame.energyPropertyIndex);
    }

    public int getMaxEnergy() {
        return properties.get(BlockEntityModularFrame.maxEnergyPropertyIndex);
    }

    public float getEnergyRatio() {
        return (float) getEnergy() / (float) getMaxEnergy();
    }

    public BlockPos targetPosition() {
        return new BlockPos(
                properties.get(BlockEntityModularFrame.xPropertyIndex),
                properties.get(BlockEntityModularFrame.yPropertyIndex),
                properties.get(BlockEntityModularFrame.zPropertyIndex)
        );
    }
}
