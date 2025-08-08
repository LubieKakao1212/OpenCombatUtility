package com.LubieKakao1212.opencu.common.screen;

import com.LubieKakao1212.opencu.NetworkUtil;
import com.LubieKakao1212.opencu.PlatformUtil;
import com.LubieKakao1212.opencu.common.OpenCUModCommon;
import com.LubieKakao1212.opencu.common.network.packet.generic.PacketC2SCycleRedstoneControl;
import com.LubieKakao1212.opencu.common.screen.handler.DeviceContainerScreenHandler;
import com.LubieKakao1212.opencu.common.screen.tabs.DeviceContainerScreenTab;
import com.LubieKakao1212.opencu.common.screen.tabs.MainTab;
import com.LubieKakao1212.opencu.common.screen.widget.ResponsiveToggleWidget;
import com.LubieKakao1212.opencu.common.screen.widget.SlicedSprite;
import com.LubieKakao1212.opencu.common.screen.widget.SliderWidget;
import com.LubieKakao1212.opencu.common.util.Observer;
import com.LubieKakao1212.opencu.common.util.RedstoneControlType;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.Drawable;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.Selectable;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.tooltip.Tooltip;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Supplier;

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

        setTab(mainTab);
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
                Text.translatable("info.opencu.gui.dc.tab.inventory")
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

        addDrawable(new SlicedSprite(
                mainTexture,
                x + 15, y - 10,
                3, 10,
                224, 92,
                17,15,
                7, 9, 5, 9
        ));

        setInitialFocus(redstoneControlButton);
    }

    @Override
    public <T extends Element & Drawable & Selectable> T addDrawableChild(T drawableElement) {
        return super.addDrawableChild(drawableElement);
    }

    @Override
    public <T extends Drawable> T addDrawable(T drawable) {
        return super.addDrawable(drawable);
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
        currentTab.renderTabBackground(this, context, mouseX, mouseY, partialTick);
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
        return (getFocused() != null && isDragging() && button == 0 && getFocused().mouseDragged(mouseX, mouseY, button, deltaX, deltaY)) || super.mouseDragged(mouseX, mouseY, button, deltaX, deltaY);
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        boolean flag = false;
        if(getFocused() != null && button == 0) {
            if(isDragging()) {
                if(getFocused().mouseReleased(mouseX, mouseY, button)) {
                    flag = true;
                }
            }
            setFocused(null);
        }
        return flag || super.mouseReleased(mouseX, mouseY, button);
    }

    public void addSlider(int x, int y, int length, int resolution, SliderWidget.Axis axis, Supplier<Double> valueProvider, Consumer<Double> valueReceiver) {
        var xOffset = 0;
        var yOffset = 0;

        int u, v, w, h, totalW, totalH, bgW, bgH;

        if(axis == SliderWidget.Axis.Vertical) {
            xOffset = 4;
            u = 200;
            v = 77;
            w = 10;
            h = 6;
            totalW = 10;
            totalH = length;
            bgW = 2;
            bgH = length;
        }
        else {
            yOffset = 4;
            u = 211;
            v = 77;
            w = 6;
            h = 10;
            totalW = length;
            totalH = 10;
            bgW = length;
            bgH = 2;
        }

        addDrawable(
                new SlicedSprite(
                        DeviceContainerScreen.mainTexture,
                        x + xOffset, y + yOffset,
                        bgW, bgH,
                        218, 77,
                        3, 3,
                        1, 1, 1,  1
                )
        );
        addDrawableChild(
                new SliderWidget(
                        DeviceContainerScreen.mainTexture,
                        x, y,
                        totalW, totalH,
                        w, h,
                        u, v,
                        valueProvider,
                        valueReceiver,
                        resolution
                ));
    }

    private void switchToTab(DeviceContainerScreenTab tab) {
        setTab(Objects.requireNonNullElseGet(tab, () -> mainTab));
    }

    private void setTab(DeviceContainerScreenTab newTab) {
        currentTab = newTab;
        handler.requestAmmoSlotsVisibility(newTab == mainTab);
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
