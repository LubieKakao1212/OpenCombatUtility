package com.LubieKakao1212.opencu.common.device;

import com.LubieKakao1212.opencu.common.block.entity.BlockEntityModularFrame;
import com.LubieKakao1212.opencu.common.device.event.data.ActivateEvent;
import com.LubieKakao1212.opencu.common.device.event.data.IEventData;
import com.LubieKakao1212.opencu.common.device.state.ArrayControllerDeviceState;
import com.LubieKakao1212.opencu.common.device.state.IDeviceState;
import com.LubieKakao1212.opencu.common.screen.tabs.DeviceContainerScreenTab;
import com.LubieKakao1212.opencu.common.transaction.DeviceActivationContext;
import com.lubiekakao1212.qulib.math.Aim;
import com.lubiekakao1212.qulib.random.RandomEx;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3d;

public class ArrayControllerDevice implements IFramedDevice {

    private final RandomEx randomEx = new RandomEx();

    /**
     * @param container
     * @param state
     * @param world
     * @param pos
     * @param aimForward
     * @param ctx       used to fetch ammo, use energy, and add leftovers
     */
    @Override
    public void activate(IDeviceContainer container, IDeviceState state, World world, BlockPos pos, Vector3d aimForward, DeviceActivationContext ctx) {
        if(container instanceof BlockEntityModularFrame frame) {
            frame.getEventDistributor().handleEvent(new ActivateEvent());
        }
    }

    @Override
    public void tick(IDeviceContainer container, IDeviceState state, World world, BlockPos pos, Vector3d aimForward, DeviceActivationContext ctx) {
        if(container instanceof BlockEntityModularFrame frame) {
            frame.aimAt(randomEx.nextOnSphere(1));
        }
    }

    @Override
    public double getPitchAlignmentSpeed() {
        return 15;
    }

    @Override
    public double getYawAlignmentSpeed() {
        return 15;
    }

    @Override
    public @NotNull IDeviceState getNewState(Runnable markDirtyDelegate) {
        return new ArrayControllerDeviceState(markDirtyDelegate);
    }

    @Override
    public void handleEvent(BlockEntityModularFrame frame, IDeviceState state, IEventData data) {
        frame.getEventDistributor().handleEvent(data);
    }

    @Override
    public boolean ammoEnabled() {
        return false;
    }

    @Override
    public boolean energyEnabled() {
        return true;
    }

    /**
     * Client Method
     */
    @Override
    public @Nullable DeviceContainerScreenTab getScreenTab() {
        return null;
    }
}
