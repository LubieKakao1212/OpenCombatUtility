package com.LubieKakao1212.opencu.common.screen.tabs;

import com.LubieKakao1212.opencu.common.block.entity.BlockEntityDeviceContainer;
import com.LubieKakao1212.opencu.common.screen.DeviceContainerScreen;
import com.LubieKakao1212.opencu.common.screen.widget.FillableBarWidget;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.tooltip.Tooltip;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;

public class MainTab extends DeviceContainerScreenTab {

    public static final String energyBarTooltipKey = "info.opencu.gui.dc.energy";

    private FillableBarWidget energyWidget;

    @Override
    public void init(DeviceContainerScreen screen) {
        super.init(screen);

        energyWidget = new FillableBarWidget(DeviceContainerScreen.mainTexture,
                screen.getX() + 15, screen.getY() + 33,
                13, 16,
                226, 58,
                FillableBarWidget.FillDirection.DOWN,
                () -> screen.getScreenHandler().getProperty(BlockEntityDeviceContainer::getEnergyRatio, 0.0f));
        screen.addDrawableChild(energyWidget);
    }

    @Override
    public void preRenderTab(DeviceContainerScreen screen, DrawContext context, int mouseX, int mouseY, float partialTick) {
        energyWidget.setTooltip(Tooltip.of(Text.translatable(energyBarTooltipKey,
                    screen.getScreenHandler().getProperty(BlockEntityDeviceContainer::getEnergy, 0),
                    screen.getScreenHandler().getProperty(BlockEntityDeviceContainer::getMaxEnergy, 0)
                )
        ));
    }


    @Override
    public ItemStack icon(DeviceContainerScreen screen) {
        return ItemStack.EMPTY;
    }

    @Override
    public Text getTabName() {
        return null; // this name is fixed in DeviceContainerScreen
    }
}
