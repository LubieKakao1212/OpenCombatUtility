package com.LubieKakao1212.opencu.forge.event;

import com.LubieKakao1212.opencu.registry.forge.CUItems;
import net.minecraft.item.ItemUsageContext;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class PlayerEventHandler {

    @SubscribeEvent
    public static void interactHandler(PlayerInteractEvent.RightClickBlock event) {
        var stack = event.getItemStack();
        var item = stack.getItem();
        if(item == CUItems.AIM_TOOL.get() || item == CUItems.LINK_TOOL.get()) {
            var iuc = new ItemUsageContext(event.getEntity(), event.getHand(), event.getHitVec());
            event.setUseItem(Event.Result.ALLOW);
            event.setUseBlock(Event.Result.DENY);
        }
    }

}
