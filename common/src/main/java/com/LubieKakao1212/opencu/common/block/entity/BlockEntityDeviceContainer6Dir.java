package com.LubieKakao1212.opencu.common.block.entity;

import com.lubiekakao1212.qulib.math.Aim;
import net.minecraft.block.BlockState;
import net.minecraft.block.DispenserBlock;
import net.minecraft.block.FacingBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public abstract class BlockEntityDeviceContainer6Dir extends BlockEntityDeviceContainer {

    @Nullable
    private final ItemStack displayModel;

    public BlockEntityDeviceContainer6Dir(BlockEntityType<?> type, @Nullable ItemStack displayModel, BlockPos pos, BlockState state) {
        super(type, pos, state);
        this.displayModel = displayModel;
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

    @Override
    public @Nullable ItemStack getDeviceItem() {
        return displayModel;
    }

    public static <T extends BlockEntity> void tick(World world, BlockPos blockPos, BlockState blockState, T be) {
        var be6Dir = (BlockEntityDeviceContainer6Dir) be;
        if(!world.isClient) {
            be6Dir.tickDeviceServer();
        }
    }
}
