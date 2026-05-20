package com.LubieKakao1212.opencu.forge.event;

import com.LubieKakao1212.opencu.common.OpenCUModCommon;
import com.LubieKakao1212.opencu.common.device.IFramedDevice;
import com.LubieKakao1212.opencu.forge.registry.CUCapabilities;
import com.LubieKakao1212.opencu.registry.CUFramedDevices;
import com.LubieKakao1212.opencu.registry.forge.CUBlocks;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Mod.EventBusSubscriber()
public class CapabilityHandler {

    @SubscribeEvent
    public static void attachCapabilities(AttachCapabilitiesEvent<ItemStack> event) {
        ItemStack stack = event.getObject();

        IFramedDevice device = null;
        Item item = stack.getItem();
        if (item == Items.ENDER_EYE) {
            device = CUFramedDevices.SIMPLE_TRACKER;
        } else if (item == Items.DISPENSER) {
            device = CUFramedDevices.VANILLA_DISPENSER;
        } else if (item == Items.DROPPER) {
            device = CUFramedDevices.VANILLA_DROPPER;
        } else if (item instanceof BlockItem blockItem) {
            var block = blockItem.getBlock();
            if(block == CUBlocks.DISPENSER_GOLD.get()) {
                device = CUFramedDevices.GOLD_DISPENSER;
            } else if (block == CUBlocks.DISPENSER_DIAMOND.get()) {
                device = CUFramedDevices.DIAMOND_DISPENSER;
            } else if (block == CUBlocks.DISPENSER_NETHERITE.get()) {
                device = CUFramedDevices.NETHERITE_DISPENSER;
            } else if (block == CUBlocks.REPULSOR.get()) {
                device = CUFramedDevices.REPULSOR;
            }
        }

        if(device != null) {
            event.addCapability(Identifier.of(OpenCUModCommon.MODID, "device"), deviceCapability(device));
        }
    }

    private static ICapabilityProvider deviceCapability(IFramedDevice device) {
        var cap = LazyOptional.of(() -> device);
        return new ICapabilityProvider() {
            @Override
            public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> capability, @Nullable Direction arg) {
                if(capability == CUCapabilities.FRAMED_DEVICE) {
                    return (LazyOptional<T>) cap;
                }
                return LazyOptional.empty();
            }

        };
    }
}
