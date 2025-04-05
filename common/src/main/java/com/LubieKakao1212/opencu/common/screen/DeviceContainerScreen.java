package com.LubieKakao1212.opencu.common.screen;

import com.LubieKakao1212.opencu.NetworkUtil;
import com.LubieKakao1212.opencu.PlatformUtil;
import com.LubieKakao1212.opencu.common.OpenCUModCommon;
import com.LubieKakao1212.opencu.common.network.packet.generic.PacketC2SCycleRedstoneControl;
import com.LubieKakao1212.opencu.common.screen.handler.DeviceContainerScreenHandler;
import com.LubieKakao1212.opencu.common.screen.tabs.DeviceContainerScreenTab;
import com.LubieKakao1212.opencu.common.screen.tabs.MainTab;
import com.LubieKakao1212.opencu.common.screen.widget.ResponsiveToggleWidget;
import com.LubieKakao1212.opencu.common.util.Observer;
import com.LubieKakao1212.opencu.common.util.RedstoneControlType;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.Drawable;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.Selectable;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.tooltip.Tooltip;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TabButtonWidget;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public class DeviceContainerScreen extends HandledScreen<DeviceContainerScreenHandler> {

    public static final Identifier mainTexture = new Identifier(OpenCUModCommon.MODID, "textures/gui/device_container_gui.png");

    //GC will be angry
    @NotNull
    DeviceContainerScreenTab mainTab = new MainTab();
    @Nullable
    private DeviceContainerScreenTab deviceTab;
    @NotNull
    private DeviceContainerScreenTab currentTab = mainTab;

    private final Observer<ItemStack> deviceSlotObserver;

    public DeviceContainerScreen(DeviceContainerScreenHandler container, PlayerInventory inv, Text titleIn) {
        super(container, inv, titleIn);
        this.backgroundWidth = 176;
        this.backgroundHeight = 166;

        deviceSlotObserver = new Observer<>(
                () -> container.getSlot(DeviceContainerScreenHandler.deviceSlot).getStack(),
                (stack1, stack2) -> !stack1.equals(stack2),
                this::deviceChanged
        );
    }

    @Override
    protected void init() {
        super.init();
        initBasic();
        currentTab.init(this);
    }

    protected void initBasic() {
        var redstoneControlButton = ResponsiveToggleWidget.multiState(mainTexture,
                x + 160, y + 6,
                10, 10,
                177, 90,
                11, 11,
                () -> handler.getProperty(be -> be.getRedstoneControlType().ordinal(), 0),
                (state) -> NetworkUtil.sendToServer(new PacketC2SCycleRedstoneControl(handler.targetPosition())),
                Tooltip.of(Text.empty()),
                Tooltip.of(RedstoneControlType.LOW.tooltip),
                Tooltip.of(RedstoneControlType.HIGH.tooltip),
                Tooltip.of(RedstoneControlType.PULSE.tooltip),
                Tooltip.of(RedstoneControlType.DISABLED.tooltip)
        );

        addDrawableChild(redstoneControlButton);

        var mainTabButton = ResponsiveToggleWidget.dualState(
                mainTexture,
                x+173, y+6,
                21, 21,
                176, 138,
                22, 0,
                () -> currentTab == mainTab,
                aBoolean -> switchToTab(null),
                Text.translatable("info.opencu.gui.dc.tab.ammo_energy")
        );
        addDrawableChild(mainTabButton);

        if(deviceTab != null) {
            var deviceTabButton = ResponsiveToggleWidget.dualState(
                    mainTexture,
                    x+173, y+27,
                    21, 21,
                    176, 117,
                    22, 0,
                    () -> currentTab != mainTab,
                    aBoolean -> switchToTab(deviceTab),
                    deviceTab.getTabName()
            );

            addDrawableChild(deviceTabButton);
        }
    }

    @Override
    public <T extends Element & Drawable & Selectable> T addDrawableChild(T drawableElement) {
        return super.addDrawableChild(drawableElement);
    }

    @Override
    protected void handledScreenTick() {
        deviceSlotObserver.update();
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float partialTick) {
        this.renderBackground(context);

        currentTab.preRenderTab(this, context, mouseX, mouseY, partialTick);

        //this.drawBackground(poseStack, partialTick, mouseX, mouseY);
        super.render(context, mouseX, mouseY, partialTick);
        if(deviceTab != null) {
            deviceTab.drawIcon(this, context, x+176, y+29, partialTick);
        }
        this.drawMouseoverTooltip(context, mouseX, mouseY);
    }

    @Override
    protected void drawBackground(DrawContext context, float partialTick, int mouseX, int mouseY) {
//        RenderSystem.setShader(GameRenderer::getPositionTexProgram);
//        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
//        RenderSystem.setShaderTexture(0, mainTexture);
        currentTab.renderTabBackground(this, context, mouseX, mouseY, partialTick);
    }

    private void switchToTab(DeviceContainerScreenTab tab) {
        setTab(Objects.requireNonNullElseGet(tab, () -> mainTab));
    }

    private void setTab(DeviceContainerScreenTab newTab) {
        currentTab = newTab;
        clearAndInit();
    }

    private void deviceChanged(ItemStack newDeviceStack) {
        boolean flag = currentTab == deviceTab;

        var device = PlatformUtil.getDeviceFrom(newDeviceStack);
        if(device != null) {
            deviceTab = device.getScreenTab();
        }
        else {
            deviceTab = null;
        }

        if(flag) {
            switchToTab(deviceTab);
        }
        else {
            switchToTab(null);
        }
    }

    //region SuperClass value Getters

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getBgWidth() {
        return backgroundWidth;
    }

    public int getBgHeight() {
        return backgroundHeight;
    }

    //endregion
}
