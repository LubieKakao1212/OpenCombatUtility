package com.LubieKakao1212.opencu.common.block;

import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.FacingBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.Nullable;

public class BlockDevice6Dir extends FacingBlock implements BlockEntityProvider {

    protected BlockDevice6Dir(Settings settings) {
        super(settings);
    }

    /**
     * {@return a new block entity instance}
     *
     * <p>For example:
     * <pre>{@code
     * @Override
     * public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
     *   return new MyBlockEntity(pos, state);
     * }
     * }</pre>
     *
     * @param pos
     * @param state
     * @implNote While this is marked as nullable, in practice this should never return
     * {@code null}. {@link net.minecraft.block.PistonExtensionBlock} is the only block in vanilla that
     * returns {@code null} inside the implementation.
     */
    @Override
    public @Nullable BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        //TODO Commit first
        return null;
    }
}
