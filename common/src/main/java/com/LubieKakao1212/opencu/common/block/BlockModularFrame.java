package com.LubieKakao1212.opencu.common.block;

import com.LubieKakao1212.opencu.common.OpenCUModCommon;
import com.LubieKakao1212.opencu.common.block.entity.BlockEntityDeviceContainer;
import com.LubieKakao1212.opencu.common.block.entity.BlockEntityModularFrame;
import com.LubieKakao1212.opencu.common.block.entity.BlockProperties;
import com.LubieKakao1212.opencu.registry.CUBlockEntities;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class BlockModularFrame extends BlockAbstractDeviceContainer {

    public BlockModularFrame(AbstractBlock.Settings properties) {
        super(properties, CUBlockEntities::modularFrame);
        setDefaultState(getDefaultState().with(BlockProperties.EMITS_REDSTONE_SIGNAL, false));
    }

    @Override
    public BlockRenderType getRenderType(BlockState pState) {
        return BlockRenderType.MODEL;
    }

    /**
     * {@return whether the block is capable of emitting redstone power}
     *
     * <p>This does not return whether the block is currently emitting redstone power.
     * Use {@link World#isEmittingRedstonePower} in that case.
     *
     * @param state
     * @see World#isEmittingRedstonePower
     */
    @Override
    public boolean emitsRedstonePower(BlockState state) {
        return true;
    }
    @Override
    public int getWeakRedstonePower(BlockState state, BlockView world, BlockPos pos, Direction direction) {
        return state.get(BlockProperties.EMITS_REDSTONE_SIGNAL) ? 15 : 0;
    }

    @Override
    protected void appendProperties(StateManager.@NotNull Builder<Block, BlockState> builder) {
        super.appendProperties(builder);
        builder.add(BlockProperties.EMITS_REDSTONE_SIGNAL);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World pLevel, BlockState pState, BlockEntityType<T> pBlockEntityType) {
        return BlockEntityModularFrame::tick;
    }
}
