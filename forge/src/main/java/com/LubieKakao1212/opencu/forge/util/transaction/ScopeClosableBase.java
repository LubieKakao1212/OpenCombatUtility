package com.LubieKakao1212.opencu.forge.util.transaction;

import org.jetbrains.annotations.NotNull;

import java.util.Stack;

/**
 * Expected to live along with {@link ScopedContext} and not any longer
 * @param <TSnapshot>
 */
public abstract class ScopeClosableBase<TSnapshot> implements IScopeClosable {

    private final Stack<TSnapshot> snapshots = new Stack<>();

    public ScopeClosableBase(ScopedContext ctx) {
        snapshots.push(null);
        ctx.registerClosable(this);
    }

    @Override
    public void onPush() {
        snapshots.push(null);
    }

    @Override
    public void onPop(boolean commited) {
        var snapshot = snapshots.pop();
        if(!commited && snapshot != null) {
            restoreSnapshot(snapshot);
        }
    }

    public final void takeSnapshot() {
        var current = snapshots.peek();
        if(current == null) {
            snapshots.pop();
            snapshots.push(createSnapshot());
        }
    }

    @NotNull
    protected abstract TSnapshot createSnapshot();

    protected abstract void restoreSnapshot(@NotNull TSnapshot snapshot);

}
