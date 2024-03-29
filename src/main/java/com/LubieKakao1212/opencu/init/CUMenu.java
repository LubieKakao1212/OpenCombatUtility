package com.LubieKakao1212.opencu.init;

import com.LubieKakao1212.opencu.OpenCUMod;
import com.LubieKakao1212.opencu.gui.container.OmnidispenserMenu;
import com.LubieKakao1212.opencu.proxy.Proxy;
import net.minecraft.client.Minecraft;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.ObjectHolder;

@CUInit
@ObjectHolder(OpenCUMod.MODID)
public class CUMenu {

    @ObjectHolder(ID.OMNI_DISPENSER)
    public static MenuType<OmnidispenserMenu> OMNI_DISPENSER;

    private static DeferredRegister<MenuType<?>> MENUS = DeferredRegister.create(ForgeRegistries.CONTAINERS, OpenCUMod.MODID);

    static {
        MENUS.register(ID.OMNI_DISPENSER, () -> IForgeMenuType.create((id, inv, data) -> new OmnidispenserMenu(id, inv, Proxy.getLevel(), data.readBlockPos())));

        //TODO check if this works
        //CURegister.register(MENUS);
    }

    public static void init() {
        CURegister.register(MENUS);
    }

    public static class ID {
        public static final String OMNI_DISPENSER = "adv_dispenser.json";
    }
}
