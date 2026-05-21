package com.LubieKakao1212.opencu.forge.util.transaction;

import com.LubieKakao1212.opencu.common.transaction.IAmmoContext;
import com.LubieKakao1212.opencu.common.transaction.IContext;
import com.LubieKakao1212.opencu.common.transaction.ILeftoverItemContext;
import com.LubieKakao1212.opencu.forge.util.ItemHandlerUtil;
import net.minecraft.item.ItemStack;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class AmmoLeftoverContext extends ScopeClosableBase<AmmoLeftoverContext.Snapshot> implements IAmmoContext, ILeftoverItemContext {

    private final IItemHandlerModifiable inventory;
    private final IItemHandlerModifiable leftovers;

    private final World scatterTragetWorld;
    private final BlockPos scatterTargetPos;

    private boolean commited;

    public AmmoLeftoverContext(ScopedContext ctx, IItemHandlerModifiable handler, World scatterTragetWorld, BlockPos scatterTargetPos) {
        super(ctx);
        leftovers = new ItemStackHandler(1024);
        this.inventory = handler;
        this.scatterTragetWorld = scatterTragetWorld;
        this.scatterTargetPos = scatterTargetPos;
    }

    @Override
    public ItemStack useAmmoFirst(IContext ctx) {
        takeSnapshot();
        for (int i = 0; i < inventory.getSlots(); i++) {
            var stack = inventory.extractItem(i, 1, false);
            if(!stack.isEmpty()) {
                return stack;
            }
        }
        return ItemStack.EMPTY;
    }

    @Override
    public ItemStack useAmmoRandom(Random random, IContext ctx) {
        takeSnapshot();
        var slots = new ArrayList<Integer>();
        for (int i = 0; i < inventory.getSlots(); i++) {
            slots.add(i);
        }
        while(!slots.isEmpty()) {
            var idx = random.nextInt(slots.size());
            var slot = slots.get(idx);
            slots.remove(idx);

            var stack = inventory.extractItem(slot, 1, true);
            if(!stack.isEmpty()) {
                inventory.extractItem(slot, 1, false);
                return stack;
            }
        }
        return ItemStack.EMPTY;
    }

    @Override
    public List<ItemStack> availableAmmo() {
        return ItemHandlerUtil.copyAsDefaultedList(inventory);
    }

    @Override
    public void handleLeftover(ItemStack stack, IContext ctx) {
        takeSnapshot();
        var leftover = ItemHandlerHelper.insertItem(inventory, stack, false);
        if(!leftover.isEmpty()) {
            ItemHandlerHelper.insertItem(leftovers, leftover, false);
        }
    }

    @Override
    public void close() {
        if(commited) {
            ItemHandlerUtil.scatterAndEmpty(leftovers, scatterTragetWorld, scatterTargetPos.getX(), scatterTargetPos.getY(), scatterTargetPos.getZ());
        }
    }

    @Override
    public void onPop(boolean commited) {
        super.onPop(commited);
        this.commited = commited;
    }

    @Override
    protected @NotNull Snapshot createSnapshot() {
        return new Snapshot();
    }

    @Override
    protected void restoreSnapshot(@NotNull AmmoLeftoverContext.Snapshot snapshot) {
        ItemHandlerUtil.setFromDefaultedList(inventory, snapshot.content);
        ItemHandlerUtil.setFromDefaultedList(leftovers, snapshot.leftovers);
    }

    public class Snapshot {
        public final DefaultedList<ItemStack> content;
        public final DefaultedList<ItemStack> leftovers;

        public Snapshot() {
            this.content = ItemHandlerUtil.copyAsDefaultedList(AmmoLeftoverContext.this.inventory);
            this.leftovers = ItemHandlerUtil.copyAsDefaultedList(AmmoLeftoverContext.this.leftovers);
        }
    }
}
