package com.LubieKakao1212.opencu.common.screen.tabs;

import com.LubieKakao1212.opencu.NetworkUtil;
import com.LubieKakao1212.opencu.common.OpenCUModCommon;
import com.LubieKakao1212.opencu.common.device.state.RepulsorDeviceState;
import com.LubieKakao1212.opencu.common.network.packet.device.repulsor.PacketC2SUpdatePulseType;
import com.LubieKakao1212.opencu.common.network.packet.device.repulsor.PacketC2SUpdateRepulsorProperty;
import com.LubieKakao1212.opencu.common.pulse.EntityPulseType;
import com.LubieKakao1212.opencu.common.screen.DeviceContainerScreen;
import com.LubieKakao1212.opencu.common.screen.handler.DeviceContainerScreenHandler;
import com.LubieKakao1212.opencu.common.screen.widget.ResponsiveToggleWidget;
import com.LubieKakao1212.opencu.common.screen.widget.SlicedSprite;
import com.LubieKakao1212.opencu.common.screen.widget.SliderWidget;
import com.LubieKakao1212.opencu.registry.CUPulse;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.util.function.Consumer;
import java.util.function.Function;

public class RepulsorTab extends DeviceContainerScreenTab {

    public static final Identifier backgroundTexture = new Identifier(OpenCUModCommon.MODID, "textures/gui/repulsor_gui_tab.png");
    public static final int pulseTypeToggleSize = 10;

    public RepulsorTab() { }

    @Override
    public void init(DeviceContainerScreen screen) {
        var handler = screen.getScreenHandler();
        addPropertySlider(screen, handler, 42, 26, RepulsorDeviceState.Property.Force);
        addPropertySlider(screen, handler, 42, 46, RepulsorDeviceState.Property.Radius);

        addTypeToggle(screen, handler, 112, 36, 178, CUPulse.REPULSOR_ID);
        addTypeToggle(screen, handler, 124, 36, 201, CUPulse.VECTOR_ID);
        addTypeToggle(screen, handler, 136, 36, 224, CUPulse.STASIS_ID);
    }

    @Override
    public Identifier getDefaultBackgroundTexture() {
        return backgroundTexture;
    }

    @Override
    public ItemStack icon(DeviceContainerScreen screen) {
        return new ItemStack(Items.DISPENSER);
    }

    @Override
    public Text getTabName() {
        return Text.translatable("info.opencu.gui.dc.tab.repulsor");
    }

    //region private utils

    private void addPropertySlider(DeviceContainerScreen screen, DeviceContainerScreenHandler handler, int x, int y, RepulsorDeviceState.Property property) {
        x += screen.getX();
        y += screen.getY();

        screen.addSlider(x, y, 50, 50, SliderWidget.Axis.Horizontal,
                () -> forRepulsorState(handler, state -> { return state.getPropertyNormal(property); }, 0.),
                value -> {
                    forRepulsorState(handler, state -> { state.setPropertyNormal(property, value); });
                    sendPropertyChange(handler, property, value);
                });
    }

    private void addTypeToggle(DeviceContainerScreen screen, DeviceContainerScreenHandler handler, int x, int y, int u, Identifier pulseType) {
        x += screen.getX();
        y += screen.getY();

        screen.addDrawableChild(ResponsiveToggleWidget.dualState(
                backgroundTexture,
                x, y,
                pulseTypeToggleSize, pulseTypeToggleSize,
                u, 4,
                11, 11,
                () -> pulseType.equals(forRepulsorState(handler, RepulsorDeviceState::getPulseTypeId, null)),
                aBoolean -> {
                    forRepulsorState(handler, state -> state.setPulseTypeId(pulseType));
                    sendTypeChange(handler, pulseType);
                },
                Text.translatable("info.opencu.gui.repulsor.pulse."+pulseType.toString())
        ));
    }

    private <T> T forRepulsorState(DeviceContainerScreenHandler handler, Function<RepulsorDeviceState, T> action, T fallback) {
        return handler.getProperty(be -> {
            var state = be.getState();
            assert state instanceof RepulsorDeviceState;
            return action.apply((RepulsorDeviceState) state);
        }, fallback);
    }

    private void forRepulsorState(DeviceContainerScreenHandler handler, Consumer<RepulsorDeviceState> action) {
        forRepulsorState(handler, state -> { action.accept(state); return 0.; }, 0.0);
    }

    private void sendPropertyChange(DeviceContainerScreenHandler handler, RepulsorDeviceState.Property property, double value) {
        var bp = handler.targetPosition();
        NetworkUtil.sendToServer(new PacketC2SUpdateRepulsorProperty(bp, property, (float)value));
    }

    private void sendTypeChange(DeviceContainerScreenHandler handler, Identifier value) {
        var bp = handler.targetPosition();
        NetworkUtil.sendToServer(new PacketC2SUpdatePulseType(bp, value));
    }

    //endregion
}
