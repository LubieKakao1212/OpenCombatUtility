package com.LubieKakao1212.opencu.common.screen.tabs;

import com.LubieKakao1212.opencu.NetworkUtil;
import com.LubieKakao1212.opencu.common.OpenCUModCommon;
import com.LubieKakao1212.opencu.common.device.state.RepulsorDeviceState;
import com.LubieKakao1212.opencu.common.network.packet.device.repulsor.PacketC2SUpdateRepulsorProperty;
import com.LubieKakao1212.opencu.common.screen.DeviceContainerScreen;
import com.LubieKakao1212.opencu.common.screen.handler.DeviceContainerScreenHandler;
import com.LubieKakao1212.opencu.common.screen.widget.SliderWidget;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.util.function.Consumer;
import java.util.function.Function;

public class RepulsorTab extends DeviceContainerScreenTab {

    public static final Identifier backgroundTexture = new Identifier(OpenCUModCommon.MODID, "textures/gui/repulsor_gui_tab.png");

    public RepulsorTab() { }

    @Override
    public void init(DeviceContainerScreen screen) {
        var handler = screen.getScreenHandler();
        addSlider(screen, handler, 10, 10, RepulsorDeviceState.Property.DirectionBlend);
        addSlider(screen, handler, 40, 10, RepulsorDeviceState.Property.Force);
        addSlider(screen, handler, 70, 10, RepulsorDeviceState.Property.Radius);
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

    private void addSlider(DeviceContainerScreen screen, DeviceContainerScreenHandler handler, int x, int y, RepulsorDeviceState.Property property) {
        screen.addDrawableChild(
                new SliderWidget(
                        DeviceContainerScreen.mainTexture,
                        screen.getX() + x, screen.getY() + y,
                        10, 50,
                        10, 6,
                        200, 77,
                        () -> forRepulsorState(handler, state -> { return state.getPropertyNormal(property); } ),
                        value -> {
                            forRepulsorState(handler, state -> { state.setPropertyNormal(property, value); });
                            sendPropertyChange(handler, property, value);
                        },
                        4
                ));
    }

    private double forRepulsorState(DeviceContainerScreenHandler handler, Function<RepulsorDeviceState, Double> action) {
        return handler.getProperty(be -> {
            var state = be.getState();
            assert state instanceof RepulsorDeviceState;
            return action.apply((RepulsorDeviceState) state);
        }, 0.0);
    }

    private void forRepulsorState(DeviceContainerScreenHandler handler, Consumer<RepulsorDeviceState> action) {
        forRepulsorState(handler, state -> { action.accept(state); return 0.; } );
    }

    private void sendPropertyChange(DeviceContainerScreenHandler handler, RepulsorDeviceState.Property property, double value) {
        var bp = handler.targetPosition();
        NetworkUtil.sendToServer(new PacketC2SUpdateRepulsorProperty(bp, property, (float)value));
    }

    //endregion
}
