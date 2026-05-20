package com.LubieKakao1212.opencu.common.block;

import com.LubieKakao1212.opencu.common.OpenCUModCommon;
import com.LubieKakao1212.opencu.common.block.entity.BlockEntityDeviceContainer;
import com.LubieKakao1212.opencu.registry.CUBlockEntities;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

public abstract class BlockAbstractDeviceContainer extends BlockWithEntity {

    private final Supplier<BlockEntityType<? extends BlockEntityDeviceContainer>> beType;

    public BlockAbstractDeviceContainer(AbstractBlock.Settings properties, Supplier<BlockEntityType<? extends BlockEntityDeviceContainer>> beType) {
        super(properties);
        this.beType = beType;
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if(!world.isClient) {
            NamedScreenHandlerFactory factory = state.createScreenHandlerFactory(world, pos);

            if(factory != null) {
                player.openHandledScreen(factory);
            }
        }
        return ActionResult.SUCCESS;
    }

    @Override
    public void onStateReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved) {
        if(!state.isOf(newState.getBlock())) {
            var blockEntity = (BlockEntityDeviceContainer) CUBlockEntities.modularFrame().get(world, pos);
            if(blockEntity != null) {
                blockEntity.scatterInventory();
            }
        }
        super.onStateReplaced(state, world, pos, newState, moved);
    }

    @Override
    public @Nullable BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return beType.get().instantiate(pos, state);
    }

    @Override
    public void neighborUpdate(BlockState state, World world, BlockPos pos, Block sourceBlock, BlockPos sourcePos, boolean notify) {
        if(world.isClient) {
            return;
        }

        var dc = beType.get().get(world, pos);

        if(dc == null) {
            OpenCUModCommon.LOGGER.warn("wrong BlockEntity at: {}", pos);
            return;
        }

        var rs = world.getReceivedRedstonePower(pos) > 0;

        if(rs != dc.getRsState()) {
            world.scheduleBlockTick(pos, this,1);
        }
    }

    @Override
    public void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        var dc = beType.get().get(world, pos);

        if(dc == null) {
            OpenCUModCommon.LOGGER.warn("wrong BlockEntity at: {}", pos);
            return;
        }

        var rs = world.getReceivedRedstonePower(pos) > 0;
        dc.pulseActivate(rs);
    }

}
