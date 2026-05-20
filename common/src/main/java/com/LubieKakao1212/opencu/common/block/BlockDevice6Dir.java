package com.LubieKakao1212.opencu.common.block;

import com.LubieKakao1212.opencu.common.OpenCUModCommon;
import com.LubieKakao1212.opencu.common.block.entity.BlockEntityDeviceContainer;
import com.LubieKakao1212.opencu.common.block.entity.BlockEntityDeviceContainer6Dir;
import com.LubieKakao1212.opencu.common.util.PlacementUtil;
import com.LubieKakao1212.opencu.registry.CUBlockEntities;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.Properties;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

public class BlockDevice6Dir extends BlockAbstractDeviceContainer {

    public BlockDevice6Dir(Settings settings, Supplier<BlockEntityType<? extends BlockEntityDeviceContainer>> beType) {
        super(settings, beType);
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        super.appendProperties(builder);
        builder.add(Properties.FACING);
    }

    /**
     * {@return the block's render type (invisible, animated, model)}
     *
     * @param state
     * @apiNote {@link BlockWithEntity} overrides this to return {@link BlockRenderType#INVISIBLE};
     * therefore, custom blocks extending that class must override it again to render the block.
     * @deprecated Consider calling {@link AbstractBlockState#getRenderType} instead. See <a href="#deprecated-methods">why these methods are deprecated</a>.
     */
    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.ENTITYBLOCK_ANIMATED;
    }

    @Override
    public @Nullable BlockState getPlacementState(ItemPlacementContext ctx) {
        var player = ctx.getPlayer();
        if(player != null) {
            var dir = PlacementUtil.getLookDirectionForPlacement(player);
            if(player.isSneaking()) {
                return getDefaultState().with(Properties.FACING, dir);
            }
            return getDefaultState().with(Properties.FACING, dir.getOpposite());
        }
        return getDefaultState().with(Properties.FACING, ctx.getPlayerLookDirection().getOpposite());
    }

    @Override
    public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
        return BlockEntityDeviceContainer6Dir::tick;
    }
}
