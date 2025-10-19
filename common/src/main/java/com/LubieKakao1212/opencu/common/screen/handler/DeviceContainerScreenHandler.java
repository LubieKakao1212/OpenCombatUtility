package com.LubieKakao1212.opencu.common.screen.handler;

import com.LubieKakao1212.opencu.NetworkUtil;
import com.LubieKakao1212.opencu.PlatformUtil;
import com.LubieKakao1212.opencu.common.OpenCUModCommon;
import com.LubieKakao1212.opencu.common.block.entity.BlockEntityDeviceContainer;
import com.LubieKakao1212.opencu.common.block.entity.BlockEntityModularFrame;
import com.LubieKakao1212.opencu.common.network.packet.screen.PacketC2SRequestAmmoSlotToggle;
import com.LubieKakao1212.opencu.common.screen.slot.SlotProvider;
import com.LubieKakao1212.opencu.common.screen.slot.ToggleableSlot;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.*;
import net.minecraft.screen.slot.Slot;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Function;

public class DeviceContainerScreenHandler extends ScreenHandler {

    public static final int playerSlotCount = 36;
    public static final int playerSlotStart = 0;
    public static final int playerSlotEnd = playerSlotStart + playerSlotCount;

//    protected static final int blockSlotsStart = 0;

    public static final int ammoSlotCount = 9;
    public static final int deviceSlot = playerSlotEnd;

    public static final int ammoSlotsStart = deviceSlot + 1;
    public static final int ammoSlotsEnd = ammoSlotsStart + ammoSlotCount;

    public static final int slotSize = 18;
//    protected final int slotCount;

//    private final ScreenHandlerContext context;
    private final PropertyDelegate properties;

    private final Property slotsHiddenProperty;

    private final PlayerInventory playerInventor;
    private final SlotProvider deviceContainerSlots;

    public DeviceContainerScreenHandler(ScreenHandlerType<?> type, int id, PlayerInventory playerInventory, SlotProvider slotProvider, int propertyCount) {
        this(type, id, playerInventory, slotProvider, new ArrayPropertyDelegate(propertyCount));
    }

    public DeviceContainerScreenHandler(ScreenHandlerType<?> type, int id, PlayerInventory playerInventory, SlotProvider deviceContainerSlots, PropertyDelegate properties) {
        super(type, id);

        this.playerInventor = playerInventory;
        this.deviceContainerSlots = deviceContainerSlots;
        createSlots(playerInventory, deviceContainerSlots);

//        this.context = context;
        slotsHiddenProperty = Property.create();
        this.addProperty(slotsHiddenProperty);
        this.properties = properties;
        this.addProperties(properties);
    }

    public void addSlotBlock(int startX, int startY, int blockWidth, int blockHeight, int slotSize, SlotFactory slotFactory) {
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
                if (slot.id == deviceSlot || slot.id >= ammoSlotsStart) {
                    if (!this.insertItem(stackCpy, 0, playerSlotEnd, false)) {
                        return ItemStack.EMPTY;
                    }
                }
                else //Is in player inventory
                {
                    if(areSlotsHidden()) {
                        return ItemStack.EMPTY;
                    }
                    var flag = false;
                    if(PlatformUtil.getDeviceFrom(stackCpy) != null) {
                        var deviceSlotSlot = slots.get(deviceSlot);
                        var currentDevice = deviceSlotSlot.getStack();
                        if(currentDevice.isEmpty()) {
                            deviceSlotSlot.setStack(stackCpy.split(1));
                            flag = true;
                        }
                    }
                    if(!this.insertItem(stackCpy, ammoSlotsStart, ammoSlotsEnd, false) && !flag) {
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
    public boolean canUse(PlayerEntity player) {
        //TODO
        return true;//context.get((world, position) -> !(world.getBlockEntity(position) instanceof BlockEntityDeviceContainer) && player.squaredDistanceTo(position.toCenterPos()) <= 64.0, true);
    }

    /**
     * Client method
     */
    public BlockPos targetPosition() {
        return new BlockPos(
                properties.get(BlockEntityModularFrame.xPropertyIndex),
                properties.get(BlockEntityModularFrame.yPropertyIndex),
                properties.get(BlockEntityModularFrame.zPropertyIndex)
        );
    }

    /**
     * Client method
     */
    public <T> T getProperty(Function<BlockEntityDeviceContainer, T> getter, T fallback) {
        assert MinecraftClient.getInstance().world != null;
        var pos = targetPosition();
        var be = MinecraftClient.getInstance().world.getBlockEntity(pos);
        if(be instanceof BlockEntityDeviceContainer bedc) {
            return getter.apply(bedc);
        }
        return fallback;
    }

    public void setAmmoSlotVisibilityServer(boolean visible) {
        setProperty(0, visible ? 0 : 1);
    }

    public boolean areSlotsHidden() {
        return slotsHiddenProperty.get() != 0;
    }

    /**
     * Client method
     */
    public void requestAmmoSlotsVisibility(boolean visible) {
        NetworkUtil.sendToServer(new PacketC2SRequestAmmoSlotToggle(syncId, visible));
        for (int i = playerSlotEnd; i<ammoSlotsEnd; i++) {
            ((ToggleableSlot)getSlot(i)).setEnabled(visible);
        }
    }

    private void createSlots(@NotNull PlayerInventory playerInventory, @Nullable SlotProvider deviceContainerSlots) {
        createPlayerSlots(playerInventory);
        if(deviceContainerSlots != null) {
            this.addSlot(deviceContainerSlots.createSlot( 0, 43, 33));
            createAmmoSlots(deviceContainerSlots);
        }
    }

    private void createPlayerSlots(@NotNull PlayerInventory playerInventory) {
        final int[] index = {0};

        SlotFactory playerSlotFactory = (int x, int y) -> new Slot(playerInventory, index[0]++, x, y);

        //Hotbar
        addSlotBlock(8, 142, 9, 1, slotSize, playerSlotFactory);

        //Main player Inventory
        addSlotBlock(8, 84, 9, 3, slotSize, playerSlotFactory);
    }

    private void createAmmoSlots(@NotNull SlotProvider deviceContainerSlots) {
        int startX = 80;
        int startY = 15;
        final int[] index = {1};

        addSlotBlock(startX, startY, 3, 3, slotSize, (int x, int y) -> deviceContainerSlots.createSlot(index[0]++, x, y));
    }
}
