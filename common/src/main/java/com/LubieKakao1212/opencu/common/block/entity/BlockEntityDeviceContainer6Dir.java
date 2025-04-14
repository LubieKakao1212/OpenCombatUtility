package com.LubieKakao1212.opencu.common.block.entity;

import com.LubieKakao1212.opencu.registry.CUMenu;
import com.lubiekakao1212.qulib.math.extensions.Vector3dExtensions;
import net.minecraft.block.BlockState;
import net.minecraft.block.FacingBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3d;

public abstract class BlockEntityDeviceContainer6Dir extends BlockEntityDeviceContainer {

    @NotNull
    private final ItemStack deviceItem;

    public BlockEntityDeviceContainer6Dir(BlockEntityType<?> type, @NotNull ItemStack deviceItem, BlockPos pos, BlockState state) {
        super(type, pos, state);
        this.deviceItem = deviceItem;
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

    /**
     * Returns the title of this screen handler; will be a part of the open
     * screen packet sent to the client.
     */
    @Override
    public Text getDisplayName() {
        return deviceItem.getName();
    }

    @Override
    public ScreenHandlerType<?> getScreenHandlerType() {
        return CUMenu.deviceContainer();
    }

    @Override
    public @NotNull ItemStack getDeviceItem() {
        return deviceItem;
    }

    public static <T extends BlockEntity> void tick(World world, BlockPos blockPos, BlockState blockState, T be) {
        var be6Dir = (BlockEntityDeviceContainer6Dir) be;
        if(!world.isClient) {
            be6Dir.energyObserver.update();
            be6Dir.tickDeviceServer();
        }
    }
}
