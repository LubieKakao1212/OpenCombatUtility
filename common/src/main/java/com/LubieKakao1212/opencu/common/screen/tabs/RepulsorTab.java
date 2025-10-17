package com.LubieKakao1212.opencu.common.screen.tabs;

import com.LubieKakao1212.opencu.NetworkUtil;
import com.LubieKakao1212.opencu.common.OpenCUModCommon;
import com.LubieKakao1212.opencu.common.device.state.RepulsorDeviceState;
import com.LubieKakao1212.opencu.common.network.packet.device.repulsor.PacketC2SToggleForceSign;
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
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;
import java.util.function.Function;

public class RepulsorTab extends DeviceContainerScreenTab {

    public static final Identifier backgroundTexture = new Identifier(OpenCUModCommon.MODID, "textures/gui/repulsor_gui_tab.png");
    public static final int pulseTypeToggleSize = 10;

    public RepulsorTab() { }

    @Override
    public void init(DeviceContainerScreen screen) {
        var handler = screen.getScreenHandler();
        addPropertySlider(screen, handler, 42, 26, RepulsorDeviceState.Property.ForceMagnitude);
        addPropertySlider(screen, handler, 42, 46, RepulsorDeviceState.Property.Radius);

        addTypeToggle(screen, handler, 112, 36, 178, CUPulse.REPULSOR_ID);
        addTypeToggle(screen, handler, 124, 36, 201, CUPulse.VECTOR_ID);
        addTypeToggle(screen, handler, 136, 36, 224, CUPulse.STASIS_ID);

        addDirectionToggle(screen, handler, 58, 60);
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

    private void addTypeToggle(DeviceContainerScreen screen, DeviceContainerScreenHandler handler, int x, int y, int u, @NotNull Identifier pulseType) {
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

    private void addDirectionToggle(DeviceContainerScreen screen, DeviceContainerScreenHandler handler, int x, int y) {
        x += screen.getX();
        y += screen.getY();

        screen.addDrawableChild(ResponsiveToggleWidget.dualState(
                DeviceContainerScreen.mainTexture,
                x, y,
                17, 7,
                220, 117,
                18, 8,
                () -> forRepulsorState(handler, RepulsorDeviceState::getForceSign, null) > 0,
                aBoolean -> {
                    forRepulsorState(handler, RepulsorDeviceState::toggleForceSign);
                    sendForceToggle(handler);
                },
                "info.opencu.gui.repulsor.force.sign"
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

    private void sendForceToggle(DeviceContainerScreenHandler handler) {
        var bp = handler.targetPosition();
        NetworkUtil.sendToServer(new PacketC2SToggleForceSign(bp));
    }

    //endregion
}
