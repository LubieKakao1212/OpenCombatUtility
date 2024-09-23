package com.LubieKakao1212.opencu.common.block.entity;

import com.lubiekakao1212.qulib.math.Aim;
import net.minecraft.block.BlockState;
import net.minecraft.block.DispenserBlock;
import net.minecraft.block.FacingBlock;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.math.BlockPos;

public abstract class BlockEntityDeviceContainer6Dir extends BlockEntityDeviceContainer {

    public BlockEntityDeviceContainer6Dir(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    @Override
    protected Aim currentAim() {
        assert world != null;

        //TODO replace with vector
        var direciton = world.getBlockState(pos).get(FacingBlock.FACING);

        switch (direciton) {
            case DOWN -> {
                return new Aim(- Math.PI / 2, 0);
            }
            case UP -> {
                return new Aim(Math.PI / 2, 0);
            }
            case NORTH -> {
                return new Aim(0, 0);
            }
            case SOUTH -> {
                return new Aim(0, Math.PI);
            }
            case WEST -> {
                return new Aim(0, Math.PI / 2);
            }
            case EAST -> {
                return new Aim(0, -Math.PI / 2);
            }
            default -> {
                return null;
            }
        }
    }
}
