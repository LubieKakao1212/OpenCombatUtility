package com.LubieKakao1212.opencu.common.device;

import com.LubieKakao1212.opencu.common.block.entity.BlockEntityModularFrame;
import com.LubieKakao1212.opencu.common.device.event.data.IEventData;
import com.LubieKakao1212.opencu.common.device.event.IEventNode;
import com.LubieKakao1212.opencu.common.device.state.IDeviceState;
import com.LubieKakao1212.opencu.common.transaction.DeviceActivationContext;
import com.lubiekakao1212.qulib.math.Aim;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public interface IFramedDevice {

    /**
     * @param ctx used to fetch ammo, use energy, and add leftovers
     */
    void activate(IDeviceContainer container, IDeviceState state, World world, BlockPos pos, Aim aim, DeviceActivationContext ctx);

    void tick(IDeviceContainer container, IDeviceState state, World world, BlockPos pos, Aim aim, DeviceActivationContext ctx);

    /*
     * @param spread spread value that would be set
     * @return error message or null if successful
    String trySetSpread(double spread);

    /*
     *
     *  @param force force value that would be set
     * @return error message or null if successful

    String trySetForce(double force);*/

    //Deg angle per tick
    double getPitchAlignmentSpeed();

    //Deg angle per tick
    double getYawAlignmentSpeed();

    IDeviceState getNewState();

    default void handleEvent(BlockEntityModularFrame frame, IDeviceState state, IEventData data) { }
}
