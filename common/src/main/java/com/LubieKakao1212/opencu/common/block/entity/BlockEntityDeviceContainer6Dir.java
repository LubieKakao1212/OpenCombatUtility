package com.LubieKakao1212.opencu.common.block.entity;

import com.lubiekakao1212.qulib.math.extensions.Vector3dExtensions;
import net.minecraft.block.BlockState;
import net.minecraft.block.FacingBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3d;

public abstract class BlockEntityDeviceContainer6Dir extends BlockEntityDeviceContainer {

    @Nullable
    private final ItemStack displayModel;

    public BlockEntityDeviceContainer6Dir(BlockEntityType<?> type, @Nullable ItemStack displayModel, BlockPos pos, BlockState state) {
        super(type, pos, state);
        this.displayModel = displayModel;
    }

    @Override
    protected Vector3d currentAim() {
        assert world != null;

        //TODO replace with vector
        var direciton = world.getBlockState(pos).get(FacingBlock.FACING);

        switch (direciton) {
            case DOWN -> {
                return Vector3dExtensions.INSTANCE.getDOWN();
            }
            case UP -> {
                return Vector3dExtensions.INSTANCE.getUP();
            }
            case NORTH -> {
                return Vector3dExtensions.INSTANCE.getNORTH();
            }
            case SOUTH -> {
                return Vector3dExtensions.INSTANCE.getSOUTH();
            }
            case WEST -> {
                return Vector3dExtensions.INSTANCE.getWEST();
            }
            case EAST -> {
                return Vector3dExtensions.INSTANCE.getEAST();
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
            be6Dir.energyObserver.update();
            be6Dir.tickDeviceServer();
        }
    }
}
