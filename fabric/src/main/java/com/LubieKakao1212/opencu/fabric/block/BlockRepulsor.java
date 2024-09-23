package com.LubieKakao1212.opencu.fabric.block;

import com.LubieKakao1212.opencu.OpenCUConfigCommon;
import com.LubieKakao1212.opencu.common.block.entity.BlockEntityRepulsor;
import com.LubieKakao1212.opencu.fabric.block.entity.BlockEntityModularFrameImpl;
import com.LubieKakao1212.opencu.fabric.block.entity.BlockEntityRepulsorImpl;
import com.LubieKakao1212.opencu.registry.CUBlockEntities;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.Properties;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class BlockRepulsor extends FacingBlock implements BlockEntityProvider {

    public BlockRepulsor(Settings settings) {
        super(settings);
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (!world.isClient) {
            //region debud
            if (player.isSneaking()) {
                var be = (BlockEntityRepulsorImpl) world.getBlockEntity(pos);
                player.sendMessage(Text.of(be.exposedEnegyStorage.getAmount() + "/" + OpenCUConfigCommon.repulsorDevice().energy().energyCapacity()));
            }
            //endregion
        }
        return ActionResult.SUCCESS;
    }

    /**
     * Appends block state properties to this block. To use this, override and call {@link
     * StateManager.Builder#add} inside the method. See {@link
     * Properties} for the list of pre-defined properties.
     *
     * @param builder
     */
    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new BlockEntityRepulsorImpl(pos, state);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
        return type == CUBlockEntities.repulsor() ? BlockEntityRepulsor::tick : null;
    }
}
