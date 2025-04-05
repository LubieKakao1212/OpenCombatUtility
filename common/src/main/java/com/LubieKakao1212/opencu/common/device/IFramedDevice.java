package com.LubieKakao1212.opencu.common.device;

import com.LubieKakao1212.opencu.common.block.entity.BlockEntityModularFrame;
import com.LubieKakao1212.opencu.common.device.event.data.IEventData;
import com.LubieKakao1212.opencu.common.device.state.IDeviceState;
import com.LubieKakao1212.opencu.common.screen.tabs.DeviceContainerScreenTab;
import com.LubieKakao1212.opencu.common.transaction.DeviceActivationContext;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3d;

public interface IFramedDevice {

    /**
     * @param ctx used to fetch ammo, use energy, and add leftovers
     */
    void activate(IDeviceContainer container, IDeviceState state, World world, BlockPos pos, Vector3d aimForward, DeviceActivationContext ctx);

    void tick(IDeviceContainer container, IDeviceState state, World world, BlockPos pos, Vector3d aimForward, DeviceActivationContext ctx);

    //Deg angle per tick
    double getPitchAlignmentSpeed();

    //Deg angle per tick
    double getYawAlignmentSpeed();

    @NotNull
    IDeviceState getNewState();

    default void handleEvent(BlockEntityModularFrame frame, IDeviceState state, IEventData data) { }

    boolean ammoEnabled();

    boolean energyEnabled();

    //region Client Methods

    /**
     * Client Method
     */
    @Nullable
    DeviceContainerScreenTab getScreenTab();
    //endregion
}
