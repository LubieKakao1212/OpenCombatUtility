package com.LubieKakao1212.opencu.common.block.entity;

import com.LubieKakao1212.opencu.common.device.IDeviceContainer;
import com.LubieKakao1212.opencu.registry.CUBlockEntities;
import com.LubieKakao1212.opencu.registry.CUFramedDevices;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

public abstract class BlockEntityRepulsor extends BlockEntityDeviceContainer6Dir {

    public int pulseTicksLeft;
    public int pulseTicks;

    public static final int pulseTicksLow = 10;
    public static final int pulseTicksHigh = 20;

    public BlockEntityRepulsor(BlockPos pos, BlockState blockState) {
        super(CUBlockEntities.repulsor(), pos, blockState);

        setCurrentDevice(CUFramedDevices.REPULSOR);
    }

    public static <T> void tick(@NotNull World world, BlockPos pos, BlockState state, T blockEntity) {
        if (world.isClient) {
            var be = ((BlockEntityRepulsor)blockEntity);
            be.pulseTicksLeft--;
            /*if(be.pulseTicksLeft-- <= (be.pulseTicks - 30)) {
                //be.pulseTicksLeft = pulseTicksLow;
                be.pulseTicks = world.random.nextBetween(0, 1) == 0 ? pulseTicksHigh : pulseTicksLow;
                be.pulseTicksLeft = be.pulseTicks;
            }*/
        } else {
            ((BlockEntityRepulsor)blockEntity).tickDeviceServer();
        }
    }

    public void setPulseTimer() {
        /*assert world != null;
        pulseTicksLeft = world.random.nextBetween(0, 1) == 0 ? pulseTicksHigh : pulseTicksLow;*/
        pulseTicksLeft = 20;
        pulseTicks = 20;
    }

    @Override
    public boolean isSameAs(IDeviceContainer deviceContainer) {
        return deviceContainer instanceof BlockEntityRepulsor rep && pos.equals(rep.pos);
    }
}