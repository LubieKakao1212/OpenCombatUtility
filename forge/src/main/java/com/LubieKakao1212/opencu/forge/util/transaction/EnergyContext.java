package com.LubieKakao1212.opencu.forge.util.transaction;

import com.LubieKakao1212.opencu.capability.InternalEnergyStorage;
import com.LubieKakao1212.opencu.common.transaction.IContext;
import com.LubieKakao1212.opencu.common.transaction.IEnergyContext;
import org.jetbrains.annotations.NotNull;

public class EnergyContext extends ScopeClosableBase<Integer> implements IEnergyContext {

    private final InternalEnergyStorage storage;

    public EnergyContext(ScopedContext ctx, InternalEnergyStorage storage) {
        super(ctx);
        this.storage = storage;
    }

    @Override
    public long useEnergy(long amount, IContext ctx) {
        takeSnapshot();
        return storage.extractEnergyInternal((int)amount, false);
    }

    @Override
    protected @NotNull Integer createSnapshot() {
        return storage.getEnergyStored();
    }

    @Override
    protected void restoreSnapshot(@NotNull Integer snapshot) {
        storage.setEnergyInternal(snapshot);
    }
}
